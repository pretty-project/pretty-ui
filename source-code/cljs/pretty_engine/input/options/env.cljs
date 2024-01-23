
(ns pretty-engine.input.options.env
    (:require [fruits.mixed.api  :as mixed]
              [fruits.vector.api :as vector]
              [fruits.string.api :as string]
              [metamorphic-content.api :as metamorphic-content]
              [pretty-engine.input.utils :as input.utils]
              [pretty-engine.input.value.env :as input.value.env]))

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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-input-option?
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
  (let [field-id      (input.utils/input-id->subitem-id input-id :text-field)
        field-content (input.value.env/get-input-displayed-value field-id {})
        option-label  (-> option option-label-f metamorphic-content/compose)]
       (and ; (string/not-matches-with? option-label field-content {:case-sensitive? false}) ; <- Deprecated, due to the select input's behaviour.
            (string/starts-with? option-label field-content {:case-sensitive? false}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn multiple-input-option-selectable?
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
                (-> max-selection (> 1))
                (-> max-selection (< 0))))))

(defn max-input-selection-not-reached?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ;
  ; @return (boolean)
  [input-id {:keys [max-selection] :as input-props}]
  (let [input-displayed-value (input.value.env/get-input-displayed-value input-id input-props)]
       (or (-> input-displayed-value vector? not)
           (-> input-displayed-value count (not= max-selection)))))

(defn get-picked-input-option
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (metamorphic-content)
  [input-id input-props]
  (if-not (multiple-input-option-selectable?               input-id input-props)
          (input.value.env/get-input-displayed-value input-id input-props)))

(defn input-option-picked?
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
        input-displayed-value (input.value.env/get-input-displayed-value input-id input-props)]
       (= input-displayed-value option-value)))

(defn input-option-toggled?
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
        input-displayed-value (input.value.env/get-input-displayed-value input-id input-props)]
       (vector/contains-item? input-displayed-value option-value)))

(defn input-option-selected?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; @param (*) option
  ;
  ; @return (boolean)
  [input-id input-props option]
  (if (multiple-input-option-selectable? input-id input-props)
      (input-option-toggled?             input-id input-props option)
      (input-option-picked?              input-id input-props option)))

(defn get-input-option-color
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
