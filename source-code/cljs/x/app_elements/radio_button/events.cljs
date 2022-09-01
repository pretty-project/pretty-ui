
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.radio-button.events
    (:require [x.app-core.api              :as a :refer [r]]
              [x.app-elements.engine.api   :as engine]
              [x.app-elements.input.events :as input.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-radio-button!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  [db [_ button-id button-props]]
  (as-> db % (r input.events/use-initial-value!   % button-id button-props)
             (r input.events/use-initial-options! % button-id button-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  [db [_ button-id button-props option]]
  (r engine/select-option! db button-id button-props option))

(defn clear-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  [db [_ button-id button-props]]
  (r input.events/clear-value! db button-id button-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.radio-button/init-radio-button! init-radio-button!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.radio-button/select-option! select-option!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.radio-button/clear-value! clear-value!)
