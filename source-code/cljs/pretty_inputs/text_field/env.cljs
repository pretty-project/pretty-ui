
(ns pretty-inputs.text-field.env
    (:require [fruits.string.api      :as string]
              [pretty-build-kit.api   :as pretty-build-kit]
              [pretty-engine.api :as pretty-engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-surface-visible?
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (boolean)
  [field-id _]
  (pretty-engine/get-input-state field-id :surface-visible?))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-field-content
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (string)
  [field-id field-props]
  ; @bug (#5160)
  ; In case the field external value gets updated to NIL, the input DOM element wouldn't react to the change,
  ; unless its ':value' property gets an empty string instead of NIL.
  (let [input-displayed-value (pretty-engine/get-input-displayed-value field-id field-props)]
       (str input-displayed-value)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-adornment-timeout-left
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ;
  ; @return (ms)
  [adornment-id]
  (pretty-engine/get-input-state adornment-id :timeout-left))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-field-line-count
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:multiline? (boolean)(opt)}
  ;
  ; @return (integer)
  [field-id {:keys [multiline?] :as field-props}]
  (let [field-content (get-field-content field-id field-props)]
       (if multiline? (let [line-count (-> field-content string/line-count inc)]
                           ; BUG#1481
                           ; Google Chrome Version 89.0.4389.114
                           ; The height of a textarea element has to be at least 2 rows!
                           ; Otherwise, the browsers might not wrap the content.
                           (inc line-count))

                      ; If the field is NOT multiline ...
                      (-> 1))))

(defn get-field-auto-height
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:font-size (keyword, px or string)
  ;  :line-height (keyword, px or string)}
  ;
  ; @usage
  ; (get-field-auto-height :my-field {:font-size :s :line-height :auto})
  ; =>
  ; "calc(var(--line-height-s) * 1)"
  ;
  ; @usage
  ; (get-field-auto-height :my-field {:font-size :s :line-height :text-block})
  ; =>
  ; "calc(var(--text-block-height-s) * 1)"
  ;
  ; @usage
  ; (get-field-auto-height :my-field {:font-size :s :line-height :xxl})
  ; =>
  ; "calc(var(--line-height-xxl) * 1)"
  ;
  ; @usage
  ; (get-field-auto-height :my-field {:font-size :s :line-height 48})
  ; =>
  ; "calc(48px * 1)"
  ;
  ; @return (string)
  [field-id {:keys [font-size line-height] :as field-props}]
  (let [field-line-count (get-field-line-count field-id field-props)
        horizontal-indent 12]
       (pretty-build-kit/adaptive-text-height font-size line-height field-line-count horizontal-indent)))
