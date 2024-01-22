
(ns pretty-inputs.core.env
    (:require [pretty-elements.core.env]
              [fruits.mixed.api  :as mixed]
              [fruits.vector.api :as vector]))

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

(defn option-picked?
  ; @ignore
  ;
  ; @note
  ; Output of option picking is always a single option.
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
  ; @note
  ; Output of option toggling is a vector of currently toggled options.
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
  ; @note
  ; Output of option selecting depends on the number of available options.
  ; If only one option is available for the input, the selecting will pick/unpick that single option.
  ; If multiple options are available for the input, the selecting will toggle/untoggle options in the output vector.
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ; @param (*) option
  ;
  ; @return (boolean)
  [input-id {:keys [options] :as input-props} option]
  (if (vector/count-min? options 2)
      (option-toggled? input-id input-props option)
      (option-picked?  input-id input-props option)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-rendered?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [input-id _]
  (get-input-state input-id :popup-rendered?))
