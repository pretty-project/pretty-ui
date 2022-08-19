
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.02
; Description:
; Version: v0.2.2
; Compatibility: x3.9.9



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.form
    (:require [mid-fruits.candy              :refer [param return]]
              [mid-fruits.vector             :as vector]
              [x.app-core.api                :as a :refer [r]]
              [x.app-elements.engine.element :as element]
              [x.app-elements.engine.input   :as input]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-input-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) form-id
  ;
  ; @return (keywords in vector)
  [db [_ form-id]]
  (r element/get-element-prop db form-id :input-ids))

(defn inputs-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) form-id
  ;
  ; @return (boolean)
  ;  A formhoz tartozó inputok értékei nem NIL, FALSE vagy "" értékek
  [db [_ form-id]]
  (let [input-ids (r get-input-ids db form-id)]
       (vector/all-items-match? input-ids #(r input/input-passed? db %1))))

(defn form-completed?
  ; @param (keyword) form-id
  ;
  ; @return (boolean)
  [db [_ form-id]]
  (or (r inputs-passed?         db form-id)
      (r a/debug-mode-detected? db)))





(defn reg-form-input!
  [db [_ input-id]]
  (if-let [form-id (get-in db [:elements/primary :data-items input-id :form-id])]
          (update-in db [:elemets/primary :data-items form-id :input-ids] vector/conj-item input-id)
          (return    db)))

(defn remove-form-input!
  [db [_ input-id]]
  (if-let [form-id (get-in db [:elements/primary :data-items input-id :form-id])]
          (update-in db [:elemets/primary :data-items form-id :input-ids] vector/remove-item input-id)
          (return    db)))
