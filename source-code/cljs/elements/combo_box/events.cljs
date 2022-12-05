
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.combo-box.events
    (:require [elements.input.events  :as input.events]
              [elements.input.helpers :as input.helpers]
              [re-frame.api           :as r :refer [r]]
              [x.db.api               :as x.db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn combo-box-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  [db [_ box-id box-props]]
  (r input.events/use-initial-options! db box-id box-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:option-value-f (function)
  ;  :value-path (vector)}
  ; @param (*) selected-option
  ;
  ; @return (map)
  [db [_ _ {:keys [option-value-f value-path]} selected-option]]
  (let [option-value (option-value-f selected-option)]
       (if (input.helpers/value-path->vector-item? value-path)
           (r x.db/set-vector-item! db value-path option-value)
           (r x.db/set-item!        db value-path option-value))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.combo-box/combo-box-did-mount combo-box-did-mount)
