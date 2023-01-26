
(ns elements.plain-field.env
    (:require [elements.plain-field.state :as plain-field.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-field-content
  ; @ignore
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [field-id]
  ; HACK#9910
  (get @plain-field.state/FIELD-CONTENTS field-id))

(defn get-field-output
  ; @ignore
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [field-id]
  ; HACK#9910
  (get @plain-field.state/FIELD-OUTPUTS field-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-empty?
  ; @ignore
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  (let [field-content (get-field-content field-id)]
       (empty? field-content)))

(defn field-filled?
  ; @ignore
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  (let [field-content (get-field-content field-id)]
       (-> field-content empty? not)))

(defn surface-visible?
  ; @ignore
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  (= field-id @plain-field.state/VISIBLE-SURFACE))
