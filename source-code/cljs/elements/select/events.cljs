
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.select.events
    (:require [elements.input.events :as input.events]
              [mid-fruits.candy      :refer [return]]
              [mid-fruits.vector     :as vector]
              [re-frame.api          :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-will-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  [db [_ select-id select-props]]
  (as-> db % (r input.events/use-initial-value!   % select-id select-props)
             (r input.events/use-initial-options! % select-id select-props)))



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
  [db [_ select-id {:keys [layout option-value-f value-path] :as select-props} option]]
  ; XXX#8706
  ; A {:layout :select :required? true} beállítással használt select elem esetlegesen
  ; megjeleníti a select-required-warning komponenst, amiért szükséges beállítani
  ; a {:visited? ...} tulajdonságot!
  (let [option-value (option-value-f option)]
       (as-> db % (case layout :select (r input.events/mark-as-visited! % select-id)
                                       (return                          %))
                  (assoc-in % value-path option-value))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clear-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  [db [_ select-id select-props]]
  (r input.events/clear-value! db select-id select-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ _ {:keys [options-path]} option]]
  (update-in db options-path vector/cons-item-once option))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.select/select-will-mount select-will-mount)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.select/clear-value! clear-value!)
