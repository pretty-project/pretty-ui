
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.combo-box.events
    (:require [x.app-core.api              :as a :refer [r]]
              [x.app-db.api                :as db]
              [x.app-elements.input.events :as input.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-combo-box!
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
  ;  {:option-value-f (function)
  ;   :value-path (vector)}
  ; @param (*) selected-option
  ;
  ; @return (map)
  [db [_ _ {:keys [option-value-f value-path]} selected-option]]
  (let [option-value (option-value-f selected-option)]
       (r db/set-item! db value-path option-value)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.combo-box/init-combo-box! init-combo-box!)
