
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.23
; Description:
; Version: v0.9.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.file-drop-area
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- area-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) area-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :font-size (keyword)
  ;   :label (metamorphic-content)
  ;   :on-click (metamorphic-event)}
  [area-props]
  (merge {:color     :primary
          :font-size :s
          :label     :drop-files-here-to-upload}
         (param area-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-drop-area-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) area-id
  ; @param (map) area-props
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
  ; @param (map) area-props
  ;
  ; @return (hiccup)
  [area-id area-props]
  [:button.x-file-drop-area--body (engine/clickable-body-attributes area-id area-props)
                                  [:i.x-file-drop-area--icon :cloud_upload]
                                  [file-drop-area-label area-id area-props]])

(defn- file-drop-area
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) area-id
  ; @param (map) area-props
  ;
  ; @return (hiccup)
  [area-id area-props]
  [:div.x-file-drop-area (engine/element-attributes   area-id area-props)
                         [file-drop-area-body         area-id area-props]
                         [engine/element-helper       area-id area-props]
                         [engine/element-info-tooltip area-id area-props]])

(defn element
  ; @param (keyword)(opt) area-id
  ; @param (map) area-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription-vector)(opt)
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :info-tooltip (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;    Default: :drop-files-here-to-upload
  ;   :on-click (metamorphic-event)(constant)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/file-drop-area {...}]
  ;
  ; @usage
  ;  [elements/file-drop-area :my-file-drop-area {...}]
  ;
  ; @return (component)
  ([area-props]
   [element (a/id) area-props])

  ([area-id area-props]
   (let [area-props (area-props-prototype area-props)]
        [file-drop-area area-id area-props])))
