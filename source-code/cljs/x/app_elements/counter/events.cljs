
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.counter.events
    (:require [mid-fruits.candy            :refer [return]]
              [x.app-core.api              :as a :refer [r]]
              [x.app-db.api                :as db]
              [x.app-elements.counter.subs :as counter.subs]
              [x.app-elements.input.events :as input.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-box-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  [db [_ counter-id counter-props]]
  (r input.events/use-initial-value! db counter-id counter-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn decrease-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {}
  ;
  ; @return (map)
  [db [_ counter-id {:keys [value-path] :as counter-props}]]
  (if (r counter.subs/value-decreasable? db counter-id counter-props)
      (r db/apply-item! db value-path dec)
      (return           db)))

(defn increase-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {}
  ;
  ; @return (map)
  [db [_ counter-id {:keys [value-path] :as counter-props}]]
  (if (r counter.subs/value-increasable? db counter-id counter-props)
      (r db/apply-item! db value-path inc)
      (return           db)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.counter/counter-box-did-mount counter-box-did-mount)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.counter/decrease-value! decrease-value!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.counter/increase-value! increase-value!)