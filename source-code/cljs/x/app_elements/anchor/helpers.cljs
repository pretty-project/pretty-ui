
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.anchor.helpers
    (:require [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]
              [x.app-environment.api     :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn anchor-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [disabled? href on-click]}]
  (if disabled? {:disabled       true}
                {:data-clickable true
                 :href           href
                 :on-click       #(a/dispatch on-click)
                 :on-mouse-up    #(environment/blur-element!)}))

(defn anchor-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ;  {:color (keyword or string)
  ;   :font-size (keyword)}
  ;
  ; @return (map)
  ;  {:data-font-size (keyword)}
  [anchor-id {:keys [color font-size] :as anchor-props}]
  (merge (engine/element-default-attributes anchor-id anchor-props)
         (engine/element-indent-attributes  anchor-id anchor-props)
         (engine/apply-color {} :color :data-color color)
         {:data-font-size font-size}))
