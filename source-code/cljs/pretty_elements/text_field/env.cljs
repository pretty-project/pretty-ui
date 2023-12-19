
(ns pretty-elements.text-field.env
    (:require [fruits.string.api               :as string]
              [pretty-elements.plain-field.env :as plain-field.env]))

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
  ;  :on-click (Re-Frame metamorphic-event)
  ;  :tooltip (metamorphic-content)}
  [field-id field-props]
  ; XXX#5100 (source-code/cljs/pretty_elements/text_field/prototypes.cljs)
  {:disabled?       (plain-field.env/field-empty? field-id)
   :icon            :close
   :on-click        [:pretty-elements.text-field/empty-field! field-id field-props]
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
  (let [field-content (plain-field.env/get-field-content field-id)]
       (if multiline? (let [line-count (-> field-content string/line-count inc)]
                           ; BUG#1481
                           ; Google Chrome Version 89.0.4389.114
                           ; The height of a textarea element has to be min. 2 rows!
                           ; Otherwise the browsers doesn't wraps the content in
                           ; every case.
                           (inc line-count))

                      ; If the field is NOT multiline ...
                      (-> 1))))

(defn field-auto-height
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:font-size (keyword)
  ;  :line-height (keyword)}
  ;
  ; @usage
  ; (field-auto-height :my-field {:font-size :s :line-height :auto})
  ; =>
  ; "calc(var( --line-height-s ) * 1)"
  ;
  ; @usage
  ; (field-auto-height :my-field {:font-size :s :line-height :text-block})
  ; =>
  ; "calc(var( --text-block-height-s ) * 1)"
  ;
  ; @usage
  ; (field-auto-height :my-field {:font-size :s :line-height :xxl})
  ; =>
  ; "calc(var( --line-height-xxl ) * 1)"
  ;
  ; @usage
  ; (field-auto-height :my-field {:font-size :s :line-height 48})
  ; =>
  ; "calc(48px * 1)"
  ;
  ; @return (string)
  [field-id {:keys [font-size line-height] :as field-props}]
  ; XXX#0886 (pretty-project/pretty-css)
  (let [line-count (field-line-count field-id field-props)]
       (case line-height :text-block (str "calc(var( --text-block-height-" (name font-size)   " ) * "line-count" + 12px)")
                         :auto       (str "calc(var( --line-height-"       (name font-size)   " ) * "line-count" + 12px)")
                                     (str "calc(var( --line-height-"       (name line-height) " ) * "line-count" + 12px)"))))
