
(ns app-extensions.storage.media-picker.views
    (:require [app-extensions.storage.media-browser.views]
              [app-plugins.item-browser.views]
              [mid-fruits.css     :as css]
              [mid-fruits.keyword :as keyword]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [x.app-media.api    :as media]
              [app-plugins.item-browser.api :as item-browser]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-extensions.storage.media-browser.views
(def directory-item app-extensions.storage.media-browser.views/directory-item)
(def file-item      app-extensions.storage.media-browser.views/file-item)

; app-plugins.item-browser.views
(def menu-mode-header   app-plugins.item-browser.views/menu-mode-header)
(def search-mode-header app-plugins.item-lister.views/search-mode-header)



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [elements/button :header-cancel-button
                   {:preset :cancel-button :indent :both :keypress {:key-code 27}
                    :on-click [:ui/close-popup! :storage.media-picker/view]}])

(defn header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [s (a/state [:item-browser/get-item-label :storage :media])]
       [elements/label ::header-label
                       {:content s}]))

(defn header-select-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  (let [no-items-selected? (a/state [:storage.media-picker/no-items-selected?])]
       [elements/button :header-select-button
                        {:disabled? no-items-selected?
                         :preset :select-button :indent :both :keypress {:key-code 13}
                         :on-click [:storage.media-picker/save-selected-items! picker-id]}]))

(defn header-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [elements/horizontal-polarity ::header-label-bar
                                {:start-content  [header-cancel-button picker-id]
                                 :middle-content [header-label         picker-id]
                                 :end-content    [header-select-button picker-id]}])

(defn header-selection-bar-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  (let [s (a/state [:storage.media-picker/get-selection-props])]
       [:<> [elements/label {:content {:content :n-items-selected :replacements [(:selected-item-count s)]}
                             :color :muted :min-height :s :font-size :xs}]
            [elements/icon-button {:color :default :preset :close :height :s :disabled? (:no-items-selected? s)
                                   :on-click [:storage.media-picker/discard-selection! picker-id]}]]))

(defn header-selection-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [elements/row {:content [header-selection-bar-structure picker-id]
                 :horizontal-align :right}])

(defn header-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [:div#item-lister--header--structure
    [menu-mode-header   :storage :media]
    [search-mode-header :storage :media]])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [:<> [header-label-bar     picker-id]
       [header-menu          picker-id]
       [header-selection-bar picker-id]])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [mime-type] :as item}]
  (let [s (a/state [:storage.media-picker/get-media-item-props item-dex item])]
       (case mime-type "storage/directory" [directory-item item-dex item {:icon :navigate_next}]
                                           [file-item      item-dex item {:icon (if (:selected? s) :check_circle_outline :radio_button_unchecked)}])))

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [item-browser/body :storage :media {:list-element #'media-item}])



;; -- Element components ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- n-items-selected-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [multiple?]}]
  (let [s (a/state [:storage.media-picker/get-element-props])]
       [elements/label {:color :muted :min-height :s
                        :content (cond (and multiple? (:no-items-selected? s))
                                       :no-items-selected
                                       (:no-items-selected? s)
                                       :no-item-selected
                                       :default {:content :n-items-selected :replacements [(:selected-item-count s)]})}]))

(defn media-picker-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [indent label]}]
  (if label [elements/label {:content label :min-height :s :indent indent}]))

(defn media-picker-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id {:keys [indent] :as picker-props}]
  [elements/toggle {:content  [n-items-selected-label picker-id]
                    :on-click [:storage.media-picker/load-picker! picker-id picker-props]
                    :indent   indent}])

(defn media-picker
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id picker-props]
  [:<> [media-picker-label  picker-id picker-props]
       [media-picker-button picker-id picker-props]])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :mime-types (strings in vector)(opt)
  ;    Default: TODO
  ;   :multiple? (boolean)(opt)
  ;    Default: false
  ;   :value-path (item-path vector)}
  ;
  ; @usage
  ;  [storage/media-picker {...}]
  ;
  ; @usage
  ;  [storage/media-picker :my-picker {...}]
  ;
  ; @return (component)
  ([picker-props]
   [element (a/id) picker-props])

  ([picker-id picker-props]
   [media-picker picker-id picker-props]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-picker/render-picker!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ picker-id]]
      [:ui/add-popup! :storage.media-picker/view
                      {:body   [body   picker-id]
                       :header [header picker-id]
                       :stretch-orientation :vertical}]))
