
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.chip.helpers
    (:require [re-frame.api                   :as r]
              [x.app-elements.element.helpers :as element.helpers]
              [x.app-environment.api          :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [style]}]
  {:style style})

(defn chip-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [chip-id {:keys [background-color color on-click] :as chip-props}]
  (merge (element.helpers/element-default-attributes chip-id chip-props)
         (element.helpers/element-indent-attributes  chip-id chip-props)
         (element.helpers/apply-color {} :background-color :data-background-color background-color)
         (element.helpers/apply-color {}            :color            :data-color            color)
         {:data-selectable false}
         (if on-click {:data-clickable true})))

(defn primary-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;  {:disabled? (boolean)(opt)
  ;   :primary-button-event (metamorphic-event)}
  ;
  ; @return (map)
  ;  {}
  [chip-id {:keys [disabled? primary-button-event] :as chip-props}]
  (if disabled? {:disabled       true}
                {:data-clickable true
                 :on-click       #(r/dispatch primary-button-event)
                 :on-mouse-up    #(environment/blur-element!)}))
