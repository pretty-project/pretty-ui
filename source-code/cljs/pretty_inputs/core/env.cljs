
(ns pretty-inputs.core.env
    (:require [pretty-inputs.core.state :as core.state]
              [fruits.vector.api :as vector]
              [fruits.mixed.api :as mixed]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-focused?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [input-id]
  (= input-id @core.state/FOCUSED-INPUT))

(defn input-changed?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [input-id]
  (-> @core.state/CHANGED-INPUTS input-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-input-options
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ;
  ; @return (vector)
  [_ {:keys [get-options-f]}]
  (if get-options-f (let [options (get-options-f)]
                         (-> options mixed/to-vector))))

(defn get-input-internal-value
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (*)
  [input-id _]
  (get @core.state/INPUT-INTERNAL-VALUES input-id))

(defn get-input-external-value
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ;
  ; @return (*)
  [_ {:keys [get-value-f]}]
  (if get-value-f (get-value-f)))

(defn get-input-displayed-value
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ;
  ; @return (*)
  [input-id {:keys [projected-value] :as input-props}]
  (if-let [input-internal-value (get-input-internal-value input-id input-props)]
          (-> input-internal-value)
          (if-not (input-changed? input-id)
                  (-> projected-value))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn single-option-selected?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ; @param (*) option
  ;
  ; @return (boolean)
  [input-id {:keys [option-value-f] :as input-props} option]
  (let [option-value          (option-value-f option)
        input-displayed-value (get-input-displayed-value input-id input-props)]
       (= input-displayed-value option-value)))

(defn multi-option-selected?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ; @param (*) option
  ;
  ; @return (boolean)
  [input-id {:keys [option-value-f] :as input-props} option]
  (let [option-value          (option-value-f option)
        input-displayed-value (get-input-displayed-value input-id input-props)]
       (vector/contains-item? input-displayed-value option-value)))

(defn option-selected?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ; @param (*) option
  ;
  ; @return (boolean)
  [input-id {:keys [options] :as input-props} option]
  (if (vector/count-min? options 2)
      (multi-option-selected?  input-id input-props option)
      (single-option-selected? input-id input-props option)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-rendered?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [input-id]
  (= input-id @core.state/RENDERED-POPUP))
