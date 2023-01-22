
(ns elements.text-field.helpers
    (:require [elements.plain-field.helpers :as plain-field.helpers]
              [noop.api                     :refer [return]]
              [string.api                   :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-field-adornment-props
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {disabled? (boolean)
  ;  :icon (keyword)
  ;  :on-click (metamorphic-event)
  ;  :tooltip (metamorphic-content)}
  [field-id field-props]
  {:disabled?       (plain-field.helpers/field-empty? field-id)
   :icon            :close
   :on-click        [:elements.text-field/empty-field! field-id field-props]
   :tooltip-content :empty-field!})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-line-count
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:multiline? (boolean)(opt)}
  ;
  ; @return (integer)
  [field-id {:keys [multiline?]}]
  (let [field-content (plain-field.helpers/get-field-content field-id)]
       (if multiline? (let [line-count (-> field-content string/line-count inc)]
                           ; BUG#1481
                           ; Google Chrome Version 89.0.4389.114
                           ; The height of a textarea element has to be min. 2 rows!
                           ; Otherwise the browsers doesn't wraps the content in
                           ; every case.
                           (inc line-count))

                      ; If the field is NOT multiline ...
                      (return 1))))

(defn field-auto-height
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:font-size (keyword)
  ;  :line-height (keyword)}
  ;
  ; @example
  ; (field-auto-height :my-field {:font-size :s :line-height :native})
  ; =>
  ; "calc(var( --line-height-s ) * 1)"
  ;
  ; @example
  ; (field-auto-height :my-field {:font-size :s :line-height :text-block})
  ; =>
  ; "calc(var( --text-block-height-s ) * 1)"
  ;
  ; @example
  ; (field-auto-height :my-field {:font-size :s :line-height :xxl})
  ; =>
  ; "calc(var( --line-height-xxl ) * 1)"
  ;
  ; @example
  ; (field-auto-height :my-field {:font-size :s :line-height 48})
  ; =>
  ; "calc(48px * 1)"
  ;
  ; @return (string)
  [field-id {:keys [font-size line-height] :as field-props}]
  ; XXX#0886 (bithandshake/pretty-css)
  (let [line-count (field-line-count field-id field-props)]
       (case line-height :text-block (str "calc(var( --text-block-height-" (name font-size)   " ) * "line-count" + 12px)")
                         :native     (str "calc(var( --line-height-"       (name font-size)   " ) * "line-count" + 12px)")
                                     (str "calc(var( --line-height-"       (name line-height) " ) * "line-count" + 12px)"))))
