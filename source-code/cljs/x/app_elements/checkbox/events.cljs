
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.checkbox.events
    (:require [mid-fruits.vector         :as vector]
              [x.app-core.api            :as a :refer [r]]
              [x.app-db.api              :as db]
              [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-checkbox!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  [db [_ checkbox-id checkbox-props]]
  (as-> db % (r engine/use-input-initial-value!   % checkbox-id checkbox-props)
             (r engine/use-input-initial-options! % checkbox-id checkbox-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;  {:get-value-f (function)
  ;   :value-path (vector)}
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ checkbox-id {:keys [get-value-f value-path] :as checkbox-props} option]]
  ; XXX#7234
  ; Ha a checkbox elem ...
  ; ... több opciót jelenít meg, akkor az egyes kiválaszott opciók értéke
  ;     egy vektorban felsorolva kerül a value-path Re-Frame adatbázis útvonalra.
  ; ... egy opciót jelenít meg, akkor az egy opció esetlegesen kiválasztott
  ;     értéke kerül a value-path Re-Frame adatbázis útvonalra.
  (let [options      (r engine/get-input-options db checkbox-id checkbox-props)
        option-value (get-value-f option)]
       (as-> db % (r engine/mark-input-as-visited! % checkbox-id)
                  (if (vector/min? options 2)
                      (r db/apply-item!        % value-path vector/toggle-item option-value)
                      (r db/toggle-item-value! % value-path option-value)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.checkbox/init-checkbox! init-checkbox!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.checkbox/toggle-option! toggle-option!)
