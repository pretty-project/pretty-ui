
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.23
; Description:
; Version: v0.7.6
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.file-drop-area
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.keyword        :as keyword]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- area-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) area-props
  ;  {:request-id (keyword)(constant)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :font-size (keyword)
  ;   :label (metamorphic-content)
  ;   :on-click (metamorphic-event)
  ;   :status-animation? (boolean)}
  [{:keys [request-id] :as area-props}]
  (merge {:color     :primary
          :font-size :s
          :label     :drop-files-here-to-upload}
         (if (some? request-id) {:status-animation? true})
         (param area-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) area-id
  ;
  ; @return (map)
  [db [_ area-id]]
  (merge (r engine/get-element-view-props db area-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-drop-area-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) area-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label]}]
  (if (some? label)
      [:div.x-file-drop-area--label [components/content {:content label}]]))

(defn- file-drop-area-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) area-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [area-id view-props]
  [:button.x-file-drop-area--body
    (engine/clickable-body-attributes area-id view-props)
    [:i.x-file-drop-area--icon (keyword/to-dom-value :cloud_upload)]
    [file-drop-area-label area-id view-props]])

(defn- file-drop-area
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) area-id
  ; @param (map) area-props
  ;
  ; @return (hiccup)
  [area-id view-props]
  [:div.x-file-drop-area
    (engine/element-attributes area-id view-props)
    [file-drop-area-body area-id view-props]])

(defn view
  ; @param (keyword)(opt) area-id
  ; @param (map) area-props
  ;  {:class (string or vector)(opt)
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :muted
  ;    Default: :primary
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :helper (metamorphic-content)(opt)
  ;   :info-tooltip (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;    Default: :drop-files-here-to-upload
  ;   :on-click (metamorphic-event)(constant)(opt)
  ;   :request-id (keyword)(constant)(opt)
  ;   :status-animation? (boolean)(opt)
  ;    Default: true
  ;    Only w/ {:request-id ...}
  ;   :style (map)(opt)
  ;   :tooltip (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [elements/file-drop-area {...}]
  ;
  ; @usage
  ;  [elements/file-drop-area :my-file-drop-area {...}]
  ;
  ; @return (component)
  ([area-props]
   [view nil area-props])

  ([area-id area-props]
   (let [area-id    (a/id   area-id)
         area-props (a/prot area-props area-props-prototype)]
        [engine/container area-id
          {:base-props area-props
           :component  file-drop-area
           :subscriber [::get-view-props area-id]}])))
