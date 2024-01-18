
(ns pretty-inputs.text-field.env
    (:require [fruits.string.api             :as string]
              [pretty-build-kit.api          :as pretty-build-kit]
              [pretty-inputs.core.env :as core.env]))

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
  ;  :on-click (function or Re-Frame metamorphic-event)
  ;  :tooltip (metamorphic-content)}
  [field-id field-props]
  ; XXX#5100 (source-code/cljs/pretty_inputs/text_field/prototypes.cljs)
  {:disabled?       (core.env/input-empty? field-id field-props)
   :icon            :close
   :on-click        [:pretty-inputs.text-field/empty-field! field-id field-props]
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
  [field-id {:keys [multiline?] :as field-props}]
  (let [field-content (core.env/get-input-displayed-value field-id field-props)]
       (if multiline? (let [line-count (-> field-content string/line-count inc)]
                           ; BUG#1481
                           ; Google Chrome Version 89.0.4389.114
                           ; The height of a textarea element has to be at least 2 rows!
                           ; Otherwise, the browsers might not wrap the content.
                           (inc line-count))

                      ; If the field is NOT multiline ...
                      (-> 1))))

(defn field-auto-height
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:font-size (keyword, px or string)
  ;  :line-height (keyword, px or string)}
  ;
  ; @usage
  ; (field-auto-height :my-field {:font-size :s :line-height :auto})
  ; =>
  ; "calc(var(--line-height-s) * 1)"
  ;
  ; @usage
  ; (field-auto-height :my-field {:font-size :s :line-height :text-block})
  ; =>
  ; "calc(var(--text-block-height-s) * 1)"
  ;
  ; @usage
  ; (field-auto-height :my-field {:font-size :s :line-height :xxl})
  ; =>
  ; "calc(var(--line-height-xxl) * 1)"
  ;
  ; @usage
  ; (field-auto-height :my-field {:font-size :s :line-height 48})
  ; =>
  ; "calc(48px * 1)"
  ;
  ; @return (string)
  [field-id {:keys [font-size line-height] :as field-props}]
  (let [line-count (field-line-count field-id field-props)
        horizontal-indent 12]
       (pretty-build-kit/adaptive-text-height font-size line-height line-count horizontal-indent)))
