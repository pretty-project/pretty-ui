
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.file-drop-area.helpers
    (:require [elements.element.helpers :as element.helpers]
              [re-frame.api             :as r]
              [x.app-environment.api    :as x.environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn area-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) area-id
  ; @param (map) area-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [card-id {:keys [disabled? on-click style]}]
  (if disabled? {:disabled        true
                 :style           style}
                {:data-clickable  true
                 :data-selectable false
                 :on-click       #(r/dispatch on-click)
                 :on-mouse-up    #(x.environment/blur-element!)
                 :style           style}))

(defn area-attributes
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
