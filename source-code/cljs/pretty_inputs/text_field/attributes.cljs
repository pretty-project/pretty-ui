
(ns pretty-inputs.text-field.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------
;(let [on-blur-f  (fn [_] (pretty-inputs.engine/input-left    checkbox-id checkbox-props))
;      on-focus-f (fn [_] (pretty-inputs.engine/input-focused checkbox-id checkbox-props))
;     (-> {:class    :pi-checkbox--inner
;          :on-blur  on-blur-f
;          :on-focus on-focus-f
;         (pretty-attributes/inner-space-attributes checkbox-props)
;         (pretty-attributes/flex-attributes checkbox-props)
;         (pretty-attributes/style-attributes       checkbox-props))))

(defn field-input-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [field-id {:keys [disabled?] :as field-props}]
  ; @bug (#8806)
  ; If the '{:disabled? true}' state of the 'text-field' input would set the 'disabled="true"' attribute on the input DOM element ...
  ; ... the input would lose its focus.
  ; ... the 'on-blur' event wouldn't occur in some browsers. Therefore, ...
  ;     ... the keypress handler would stay in type mode.
  ;     ... the field would stay marked as focused.
  ; ... after the '{:disabled? true}' state would end, the field wouldn't get back its focused state.
  ; Therefore, the input DOM element shouldn't get the 'disabled="true"' attribute!
  ;
  ; @bug (#8809)
  ; If the input has no ':on-change' property, the React would warn that the input stepped into an uncontrolled state.
  ; Therefore, the input DOM element must keep its ':on-change' property in '{:disabled? true}' state as well!
  (let [];field-content (text-field.env/get-field-content field-id field-props)]
        ;on-blur-f   (fn [_] (text-field.side-effects/field-left    field-id field-props)
        ;                    (pretty-inputs.engine/input-left    field-id field-props))
        ;on-focus-f  (fn [_] (text-field.side-effects/field-focused field-id field-props))
        ;on-input-f  (fn [%] (text-field.side-effects/value-changed field-id field-props %))
        ;on-change-f (fn [%] (text-field.side-effects/value-changed field-id field-props %))
        ;on-change-f (fn [_])]
       (-> (if disabled? {:class               :pi-text-field--input
                          :data-autofill-style :none
                          :data-text-caret     :hidden
                          :tab-index           -1}
                          ;:value               field-content}
                          ;:on-change           on-change-f}
                         {:class               :pi-text-field--input

                          :data-autofill-style :none})

                          ;:value               field-content})
                          ;:on-blur             on-blur-f
                          ;:on-focus            on-focus-f
                          ;:on-change           on-change-f
                          ;:on-input            on-input-f})
           (pretty-attributes/input-field-attributes        field-props)
           (pretty-attributes/effect-attributes       field-props)
           (pretty-attributes/react-attributes        field-props)
           (pretty-attributes/inner-size-attributes            field-props)))) ; <- ?? body inkább?

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inner-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pi-text-field--inner}
      (pretty-attributes/flex-attributes        props)
      (pretty-attributes/inner-size-attributes  props)
      (pretty-attributes/inner-space-attributes props)
      (pretty-attributes/mouse-event-attributes props)
      (pretty-attributes/style-attributes       props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn outer-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pi-text-field--outer}
      (pretty-attributes/class-attributes          props)
      (pretty-attributes/inner-position-attributes props)
      (pretty-attributes/outer-position-attributes props)
      (pretty-attributes/outer-size-attributes     props)
      (pretty-attributes/outer-space-attributes    props)
      (pretty-attributes/state-attributes          props)
      (pretty-attributes/theme-attributes          props)))
