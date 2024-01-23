
(ns pretty-inputs.core.env
    (:require [pretty-elements.core.env]
              [fruits.mixed.api  :as mixed]
              [fruits.vector.api :as vector]
              [fruits.string.api :as string]
              [metamorphic-content.api :as metamorphic-content]
              [pretty-inputs.core.utils :as core.utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-elements.core.env/*)
(def get-input-state pretty-elements.core.env/get-element-state)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-focused?
  ; @ignore
  ;
  ; @param (map) input-props
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [input-id _]
  (get-input-state input-id :focused?))

(defn input-changed?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [input-id _]
  (get-input-state input-id :changed?))

(defn input-empty?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [input-id _]
  (let [internal-value (get-input-state input-id :internal-value)]
       (and (seqable? internal-value)
            (empty?   internal-value))))

(defn input-not-empty?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [input-id input-props]
  (-> (input-empty? input-id input-props) not))

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
  (get-input-state input-id :internal-value))

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
  (if-some [input-internal-value (get-input-internal-value input-id input-props)]
           (-> input-internal-value)
           (if-not (input-changed? input-id input-props)
                   (-> projected-value))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-option?
  ; @ignore
  ;
  ; @description
  ; Returns TRUE if ...
  ; ... the input has a text-field as a subitem,
  ; ... the label of the given option starts with the content of the text-field.
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:option-label-f (function)(opt)}
  ; @param (*) option
  ;
  ; @return (boolean)
  [input-id {:keys [option-label-f] :as input-props} option]
  (let [field-id      (core.utils/input-id->subitem-id input-id :text-field)
        field-content (get-input-displayed-value field-id {})
        option-label  (-> option option-label-f metamorphic-content/compose)]
       (and ; (string/not-matches-with? option-label field-content {:case-sensitive? false}) ; <- Deprecated, due to the select input's behaviour.
            (string/starts-with? option-label field-content {:case-sensitive? false}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn multiple-option-selectable?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ;
  ; @return (boolean)
  [input-id {:keys [max-selection] :as input-props}]
  (let [input-options (get-input-options input-id input-props)]
       (and (vector/count-min? input-options 2)
            (or (-> max-selection integer? not)
                (-> max-selection (> 1))))))

(defn max-selection-not-reached?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ;
  ; @return (boolean)
  [input-id {:keys [max-selection] :as input-props}]
  (let [input-displayed-value (get-input-displayed-value input-id input-props)]
       (or (-> input-displayed-value vector? not)
           (-> input-displayed-value count (not= max-selection)))))

(defn get-picked-option
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (metamorphic-content)
  [input-id input-props]
  (if-not (multiple-option-selectable? input-id input-props)
          (get-input-displayed-value   input-id input-props)))

(defn option-picked?
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

(defn option-toggled?
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
  ; @param (*) option
  ;
  ; @return (boolean)
  [input-id input-props option]
  (if (multiple-option-selectable? input-id input-props)
      (option-toggled?             input-id input-props option)
      (option-picked?              input-id input-props option)))

(defn get-option-color
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; @param (*) option
  ;
  ; @return (keyword or string)
  [_ {:keys [option-color-f]} option]
  (if option-color-f (-> option option-color-f)
                     (-> :default)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-popup-rendered?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [input-id _]
  (get-input-state input-id :popup-rendered?))
