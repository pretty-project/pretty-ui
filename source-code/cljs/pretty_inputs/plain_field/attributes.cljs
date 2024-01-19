
(ns pretty-inputs.plain-field.attributes
    (:require [dom.api                                :as dom]
              [pretty-build-kit.api                   :as pretty-build-kit]
              [pretty-inputs.core.env                 :as core.env]
              [pretty-inputs.plain-field.side-effects :as plain-field.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  ; If the '{:disabled? true}' state of the 'plain-field' element would set the 'disabled="true"' attribute on the input DOM element ...
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
  (let [field-content (core.env/get-input-internal-value field-id field-props)
        on-blur-f   (fn [_] (plain-field.side-effects/field-left    field-id field-props))
        on-focus-f  (fn [_] (plain-field.side-effects/field-focused field-id field-props))
        on-input-f  (fn [%] (plain-field.side-effects/value-changed field-id field-props %))
        on-change-f (fn [%] (plain-field.side-effects/value-changed field-id field-props %))
        on-change-f (fn [_])]
       (-> (if disabled? {:class               :pi-text-field--input
                          :data-autofill-style :none
                          :data-caret          :hidden
                          :tab-index           -1
                          :value               field-content
                          :on-change           on-change-f}
                         {:class               :pi-text-field--input
                          :data-autofill-style :none
                          :value               field-content
                          :on-blur             on-blur-f
                          :on-focus            on-focus-f
                          :on-change           on-change-f
                          :on-input            on-input-f})
           (pretty-build-kit/autofill-attributes     field-props)
           (pretty-build-kit/field-attributes        field-props)
           (pretty-build-kit/effect-attributes       field-props)
           (pretty-build-kit/focus-attributes        field-props)
           (pretty-build-kit/element-size-attributes field-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-surface-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:surface (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-box-shadow-color (keyword)
  ;  :on-mouse-down (function)}
  [field-id {:keys [surface]}]
  ; @bug (pretty-inputs.text-field.attributes#2105)
  (-> {:class                 :pi-plain-field--surface
       :data-box-shadow-color :default
       :on-mouse-down         dom/prevent-default}
      (pretty-build-kit/border-attributes surface)
      (pretty-build-kit/indent-attributes surface)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-body-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as field-props}]
  (-> {:class :pi-plain-field--body
       :style style}
      (pretty-build-kit/indent-attributes field-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:disabled? (boolean)(opt)}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [disabled?] :as field-props}]
  (-> {:class        :pi-plain-field
       :data-covered disabled?}
      (pretty-build-kit/class-attributes   field-props)
      (pretty-build-kit/outdent-attributes field-props)
      (pretty-build-kit/state-attributes   field-props)))
