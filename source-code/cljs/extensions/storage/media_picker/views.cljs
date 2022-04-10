
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-picker.views
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- media-picker-toggle-auto-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:multiple? (boolean)(opt)}
  [picker-id {:keys [multiple?] :as picker-props}]
  (if-let [no-items-picked? @(a/subscribe [:storage.media-picker/no-items-picked? picker-id picker-props])]
          (if multiple? :no-items-selected :no-item-selected)
          (let [picked-item-count @(a/subscribe [:storage.media-picker/get-picked-item-count picker-id picker-props])]
               {:content :n-items-selected :replacements [picked-item-count]})))

(defn- media-picker-toggle-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:toggle-label (metamorphic-content)(opt)}
  [picker-id {:keys [toggle-label] :as picker-props}]
  (let [toggle-label (or toggle-label (media-picker-toggle-auto-label picker-id picker-props))]
       [elements/label {:color     :muted
                        :content   toggle-label
                        :font-size :xs}]))

(defn media-picker-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? label]}]
  (if label [elements/label {:content   label
                             :disabled? disabled?}]))

(defn media-picker-toggle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)}
  [picker-id {:keys [disabled?] :as picker-props}]
  [elements/toggle {:content   [media-picker-toggle-label              picker-id picker-props]
                    :on-click  [:storage.media-selector/load-selector! picker-id picker-props]
                    :disabled? disabled?}])

(defn- media-picker-thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :thumbnails (map)
  ;    {:size (keyword)(opt)}}
  ; @param (string) thumbnail-uri
  [picker-id {:keys [disabled?] :as picker-props} thumbnail-uri]
  [elements/thumbnail ::selected-thumbnail-button
                      {:disabled? disabled?
                       :on-click  [:storage.media-selector/load-selector! picker-id picker-props]
                       :size      :xxl
                       :uri       thumbnail-uri}])

(defn media-picker-thumbnail-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:thumbnails (map)
  ;    {:max-count (integer)(opt)}}
  [picker-id {:keys [thumbnails] :as picker-props}]
  (letfn [(f [thumbnail-list thumbnail-uri] (conj thumbnail-list [media-picker-thumbnail picker-id picker-props thumbnail-uri]))]
         (let [picked-items @(a/subscribe [:storage.media-picker/get-picked-items picker-id picker-props])]
              (reduce f [:<>] (if-let [max-count (:max-count thumbnails)]
                                      (vector/first-items picked-items max-count)
                                      (param              picked-items))))))

(defn- media-picker-thumbnails
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  (if-let [no-items-picked? @(a/subscribe [:storage.media-picker/no-items-picked? picker-id picker-props])]
          [media-picker-toggle         picker-id picker-props]
          [media-picker-thumbnail-list picker-id picker-props]))

(defn media-picker
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:thumbnails (map)(opt)}
  [picker-id {:keys [thumbnails] :as picker-props}]
  [:<> [media-picker-label picker-id picker-props]
       (if thumbnails [media-picker-thumbnails picker-id picker-props]
                      [media-picker-toggle     picker-id picker-props])])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :extensions (strings in vector)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :multiple? (boolean)(opt)
  ;    Default: false
  ;   :thumbnails (map)(opt)
  ;    {:max-count (integer)(opt)
  ;     :size (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :toggle-label (metamorphic-content)(opt)
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [storage/media-picker {...}]
  ;
  ; @usage
  ;  [storage/media-picker :my-picker {...}]
  ([picker-props]
   [element (a/id) picker-props])

  ([picker-id picker-props]
   [media-picker picker-id picker-props]))
