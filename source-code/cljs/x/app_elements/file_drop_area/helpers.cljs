
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.file-drop-area.helpers
    (:require [x.app-core.api                 :as a]
              [x.app-elements.element.helpers :as element.helpers]
              [x.app-environment.api          :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-drop-area-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) area-id
  ; @param (map) area-props
  ;  {}
  ;
  ; @return (map)
  [area-id {:keys [color] :as area-props}]
  (merge (element.helpers/element-default-attributes area-id area-props)
         (element.helpers/element-indent-attributes  area-id area-props)
         (element.helpers/apply-color {} :color :data-color color)))

(defn file-drop-area-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) area-id
  ; @param (map) area-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [card-id {:keys [disabled? on-click]}]
  (if disabled? {:disabled        true}
                {:data-clickable  true
                 :data-selectable false
                 :on-click       #(a/dispatch on-click)
                 :on-mouse-up    #(environment/blur-element!)}))
