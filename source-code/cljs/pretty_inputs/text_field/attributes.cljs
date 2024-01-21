
(ns pretty-inputs.text-field.attributes
    (:require [dom.api                              :as dom]
              [pretty-build-kit.api                 :as pretty-build-kit]
              [pretty-inputs.text-field.side-effects :as text-field.side-effects]
              [pretty-inputs.text-field.env         :as text-field.env]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {}
  [adornment-id adornment-props]
  (-> {:class :pi-text-field--adornment-icon}
      (pretty-build-kit/icon-attributes adornment-props)))

(defn adornment-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {}
  [adornment-id adornment-props]
  ; @bug (#2105)
  ; An 'on-mouse-down' event fired anywhere outside the input triggers the 'on-blur' event of the field
  ; that would cause the dissapearing of the surface unless the 'on-mouse-down' default event is prevented.
  (-> {:class         :pi-text-field--adornment
       :on-mouse-down dom/prevent-default}
      (pretty-build-kit/color-attributes        adornment-props)
      (pretty-build-kit/cursor-attributes       adornment-props)
      (pretty-build-kit/effect-attributes       adornment-props)
      (pretty-build-kit/font-attributes         adornment-props)
      (pretty-build-kit/mouse-event-attributes  adornment-props)
      (pretty-build-kit/state-attributes        adornment-props)
      (pretty-build-kit/tab-attributes          adornment-props)
      (pretty-build-kit/tooltip-attributes      adornment-props)
      (pretty-build-kit/unselectable-attributes adornment-props)))

(defn countdown-adornment-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  [adornment-id adornment-props]
  (adornment-attributes adornment-id (-> adornment-props (dissoc :click-effect :hover-effect :icon-family :icon-size :on-click-f)
                                                         (assoc  :text-color :highlight))))

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
  (-> {:class               :pi-text-field--placeholder
       :data-font-size      :xs
       :data-letter-spacing :auto
       :data-line-height    :text-block
       :data-selectable     false
       :data-text-overflow  :hidden}
      (pretty-build-kit/effect-attributes field-props)))

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

(defn input-container-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [style] :as field-props}]
  (-> {:class               :pi-text-field--input-container
       :data-letter-spacing :auto
       :style               style}
      (pretty-build-kit/border-attributes field-props)
      (pretty-build-kit/font-attributes   field-props)
      (pretty-build-kit/indent-attributes field-props)
      (pretty-build-kit/marker-attributes field-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  {:class         :pi-text-field--adornments-placeholder
   :on-mouse-down dom/prevent-default})

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
      (pretty-build-kit/border-attributes surface)
      (pretty-build-kit/indent-attributes surface)))

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
      (pretty-build-kit/class-attributes        field-props)
      (pretty-build-kit/outdent-attributes      field-props)
      (pretty-build-kit/state-attributes        field-props)
      (pretty-build-kit/wrapper-size-attributes field-props)))
