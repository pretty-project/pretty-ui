
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.events
    (:require [mid-fruits.map    :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ;
  ; @return (map)
  [db [_ select-id {:keys [form-id initial-options initial-value options-path value-path]}]]
  ; Az init-select! függvény csak abban az esetben alkalmazza ...
  ; ... az initial-options értékét, ha az options-path útvonalon tárolt érték még üres!
  ; ... az initial-value értékét, ha a value-path útvonalon tárolt érték még üres!
  (let [options (get-in db options-path)
        value   (get-in db value-path)]
       (cond-> db (and initial-options (empty? options)) (assoc-in options-path initial-options)
                  (and initial-value   (empty? value))   (assoc-in value-path   initial-value)
                  form-id (update-in [:elements :element-handler/meta-items form-id] vector/conj-item-once select-id))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ _ {:keys [get-value-f value-path]} option]]
  (let [option-value (get-value-f option)]
       (assoc-in db value-path option-value)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clear-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ;
  ; @return (map)
  [db [_ _ {:keys [value-path]}]]
  (dissoc-in db value-path))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.select/init-element! init-element!)


; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.select/clear-value! clear-value!)
