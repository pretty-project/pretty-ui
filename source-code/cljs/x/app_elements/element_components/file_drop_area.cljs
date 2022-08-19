
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.file-drop-area
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
  ;  {:color (keyword or string)
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
  [_ {:keys [label]}]
  (if label [:div.x-file-drop-area--label [components/content label]]))

(defn- file-drop-area-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) area-id
  ; @param (map) area-props
  [area-id area-props]
  [:button.x-file-drop-area--body (engine/clickable-body-attributes area-id area-props)
                                  [:i.x-file-drop-area--icon :cloud_upload]
                                  [file-drop-area-label area-id area-props]])

(defn- file-drop-area
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) area-id
  ; @param (map) area-props
  [area-id area-props]
  [:div.x-file-drop-area (engine/element-attributes area-id area-props)
                         [file-drop-area-body       area-id area-props]
                         [engine/element-helper     area-id area-props]])

(defn element
  ; @param (keyword)(opt) area-id
  ; @param (map) area-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
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
  ([area-props]
   [element (a/id) area-props])

  ([area-id area-props]
   (let [area-props (area-props-prototype area-props)]
        [file-drop-area area-id area-props])))
