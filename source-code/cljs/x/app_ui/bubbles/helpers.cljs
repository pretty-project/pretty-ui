
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.bubbles.helpers
    (:require [mid-fruits.hiccup :as hiccup]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn primary-button-on-click
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;  {:primary-button (map)
  ;    {:on-click (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:dispatch-n (vector)}
  [bubble-id {:keys [primary-button]}]
  {:dispatch-n [(:on-click primary-button)
                [:ui/remove-bubble! bubble-id]]})

(defn bubble-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ;
  ; @return (map)
  ;  {}
  [bubble-id]
  {:class          :x-app-bubbles--element
   :data-animation :reveal
   :data-nosnippet true
   :id             (hiccup/value bubble-id)
   :key            (hiccup/value bubble-id)})
