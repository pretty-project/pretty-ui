
(ns pretty-elements.plain-field.env
    (:require [pretty-elements.plain-field.config :as plain-field.config]
              [pretty-elements.plain-field.state  :as plain-field.state]
              [time.api                           :as time]))

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

(defn type-ended?
  ; @ignore
  ;
  ; @description
  ; Typing only considered as ended if at least X ms elapsed after the last key pressed.
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  (let [timestamp  (time/elapsed)
        changed-at (get-in @plain-field.state/FIELD-STATES [field-id :changed-at])]
       (> timestamp (+ changed-at plain-field.config/TYPE-ENDED-AFTER))))
