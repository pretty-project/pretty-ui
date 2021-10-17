
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.02
; Description:
; Version: v0.2.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.form
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
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
       (vector/all-items-match? input-ids #(r input/input-value-passed? db %1))))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-input!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) form-id
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ form-id input-id]]
  (let [input-ids (r get-input-ids db form-id)]
       (r element/set-element-prop! db form-id :input-ids
          (vector/conj-item input-ids input-id))))
