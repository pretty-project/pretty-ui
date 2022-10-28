
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.file-drop-area.views
    (:require [mid-fruits.random                        :as random]
              [x.app-components.api                     :as x.components]
              [x.app-elements.file-drop-area.helpers    :as file-drop-area.helpers]
              [x.app-elements.file-drop-area.prototypes :as file-drop-area.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-drop-area-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) area-id
  ; @param (map) area-props
  ;  {:label (metamorphic-content)}
  [_ {:keys [label]}]
  [:div.x-file-drop-area--label (x.components/content label)])

(defn- file-drop-area-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) area-id
  ; @param (map) area-props
  [area-id area-props]
  [:button.x-file-drop-area--body (file-drop-area.helpers/area-body-attributes area-id area-props)
                                  [:i.x-file-drop-area--icon {:data-icon-family :material-icons-filled} :cloud_upload]
                                  [file-drop-area-label area-id area-props]])

(defn- file-drop-area
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) area-id
  ; @param (map) area-props
  [area-id area-props]
  [:div.x-file-drop-area (file-drop-area.helpers/area-attributes area-id area-props)
                         [file-drop-area-body                    area-id area-props]])

(defn element
  ; @param (keyword)(opt) area-id
  ; @param (map) area-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
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
  ;   :on-click (metamorphic-event)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [file-drop-area {...}]
  ;
  ; @usage
  ;  [file-drop-area :my-file-drop-area {...}]
  ([area-props]
   [element (random/generate-keyword) area-props])

  ([area-id area-props]
   (let [area-props (file-drop-area.prototypes/area-props-prototype area-props)]
        [file-drop-area area-id area-props])))
