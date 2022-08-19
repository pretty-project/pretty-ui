

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.collect-handler.subs
    (:require [mid-fruits.candy                 :refer [param]]
              [mid-fruits.vector                :as vector]
              [x.app-core.api                   :as a :refer [r]]
              [x.app-db.api                     :as db]
              [x.app-elements.engine.element    :as element]
              [x.app-elements.engine.input      :as input]
              [x.app-elements.engine.selectable :as selectable]
              [x.app-environment.api            :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-collectable-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [db [_ input-id]]
  ; BUG#7633
  (if-let [options-path (r element/get-element-prop db input-id :options-path)]
          (get-in db options-path)))

(defn get-collected-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [db [_ input-id]]
  (vec (r input/get-input-value db input-id)))

(defn option-collected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (boolean)
  [db [_ input-id option]]
  (let [get-value-f     (r element/get-element-prop db input-id :get-value-f)
        value-path      (r element/get-element-prop db input-id :value-path)
        collected-value (r get-collected-value      db input-id)
        value           (get-value-f option)]
       (vector/contains-item? collected-value value)))

(defn collectable-collected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [collected-value (r get-collected-value db input-id)]
       (vector/nonempty? collected-value)))

(defn collectable-noncollected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (not (r collectable-collected? db input-id)))

(defn get-collectable-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  ;  {:collected? (boolean)
  ;   :color (keyword)
  ;   :helper (keyword)
  ;   :collected-value (vector)}
  [db [_ input-id]]
  (merge {:collected?      (r collectable-collected?  db input-id)
          :collected-value (r get-collected-value     db input-id)
          :options         (r get-collectable-options db input-id)}
         (if (r input/input-required-warning? db input-id)
             {:border-color :warning
              :helper       :please-select-an-option})))
