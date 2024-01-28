
(ns pretty-inputs.text-field.attributes
    (:require [dom.api                               :as dom]
              [pretty-css.api :as pretty-css]
              [pretty-engine.api                     :as pretty-engine]
              [pretty-inputs.text-field.env          :as text-field.env]
              [pretty-inputs.text-field.side-effects :as text-field.side-effects]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.accessories.api :as pretty-css.accessories]
              [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.content.api :as pretty-css.content]
              [pretty-css.control.api :as pretty-css.control]
              [pretty-css.layout.api :as pretty-css.layout]
              [pretty-css.live.api :as pretty-css.live]
              [pretty-css.input.api :as pretty-css.input]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-placeholder-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {}
  [_ field-props]
  (-> {:class                :pi-text-field--placeholder
       :data-font-size       :xs
       :data-letter-spacing  :auto
       :data-line-height     :text-block
       :data-text-overflow   :hidden
       :data-text-selectable false}
      (pretty-css.live/effect-attributes field-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-emphasize-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)
  ;   {:height (string)}}
  [field-id field-props]
  {:class :pi-text-field--input-emphasize
   :style {:height (text-field.env/get-field-auto-height field-id field-props)}})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-adornments-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {}
  [field-id field-props]
  ; @bug (#2105)
  ; An 'on-mouse-down' event fired anywhere outside the input triggers the 'on-blur' event of the field
  ; that would cause the dissapearing of the surface, unless the default 'on-mouse-down' event is prevented.
  {:class         :pi-text-field--adornments
   :on-mouse-down dom/prevent-default})

(defn field-adornments-placeholder-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {}
  [field-id field-props]
  ; @bug (#2105)
  (let [on-mouse-up-f #(pretty-engine/focus-input! field-id field-props)]
       {:class         :pi-text-field--adornments-placeholder
        :on-mouse-down dom/prevent-default
        :on-mouse-up   on-mouse-up-f}))

(defn field-surface-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [field-id {:keys [surface] :as field-props}]
  ; @bug (#2105)
  (-> {:class                 :pi-text-field--surface
       :data-box-shadow-color :default
       :on-mouse-down         dom/prevent-default}
      (pretty-css.appearance/border-attributes surface)
      (pretty-css.layout/indent-attributes surface)
      ; The field surface inherits the font settings of the field as its default.
      (pretty-css.content/font-attributes field-props)))

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
  (let [field-content (text-field.env/get-field-content field-id field-props)
        on-blur-f   (fn [_] (text-field.side-effects/field-left    field-id field-props))
        on-focus-f  (fn [_] (text-field.side-effects/field-focused field-id field-props))
        on-input-f  (fn [%] (text-field.side-effects/value-changed field-id field-props %))
        on-change-f (fn [%] (text-field.side-effects/value-changed field-id field-props %))
        on-change-f (fn [_])]
       (-> (if disabled? {:class               :pi-text-field--input
                          :data-autofill-style :none
                          :data-text-caret     :hidden
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
           (pretty-css.input/autofill-attributes     field-props)
           (pretty-css.input/field-attributes        field-props)
           (pretty-css.live/effect-attributes       field-props)
           (pretty-css.layout/element-size-attributes field-props)
           (pretty-css.control/focus-attributes        field-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-body-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {}
  [_ field-props]
  (-> {:class               :pi-text-field--body
       :data-letter-spacing :auto}
      (pretty-css.appearance/background-attributes  field-props)
      (pretty-css.appearance/border-attributes field-props)
      (pretty-css.content/font-attributes   field-props)
      (pretty-css.layout/indent-attributes field-props)
      (pretty-css.accessories/marker-attributes field-props)
      (pretty-css.basic/style-attributes  field-props)))

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
  ; {:class (keyword or keywords in vector)}
  [_ {:keys [disabled?] :as field-props}]
  (-> {:class        :pi-text-field
       :data-covered disabled?}
      (pretty-css.basic/class-attributes        field-props)
      (pretty-css.layout/outdent-attributes      field-props)
      (pretty-css.basic/state-attributes        field-props)
      (pretty-css.appearance/theme-attributes        field-props)
      (pretty-css.layout/wrapper-size-attributes field-props)))
