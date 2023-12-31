
(ns pretty-elements.plain-field.attributes
    (:require [fruits.hiccup.api                 :as hiccup]
              [pretty-build-kit.api                    :as pretty-build-kit]
              [pretty-elements.plain-field.env   :as plain-field.env]
              [pretty-elements.plain-field.utils :as plain-field.utils]
              [re-frame.api                      :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-input-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:disabled? (boolean)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-autofill-style (keyword)
  ;  :data-caret-color (keyword)
  ;  :id (string)
  ;  :on-blur (function)
  ;  :on-change (function)
  ;  :on-focus (function)
  ;  :tab-index (integer)
  ;  :type (keyword)
  ;  :value (string)}
  [field-id {:keys [disabled? on-change-f] :as field-props}]
  ; @note (pretty-elements.button.attributes#4460)
  ;
  ; BUG#8806
  ; If the {:disabled? true} state of the plain-field element would set the
  ; disabled="true" attribute on the input DOM element ...
  ; ... the input loses its focus.
  ; ... the on-blur event doesn't occur in some browsers.
  ;     Therefore, the keypress handler stays in type mode and the field stays
  ;     marked as focused.
  ; ... after the {:disabled? true} state ends, the field doesn't get back
  ;     its focused state.
  ; Therefore, the input DOM element doesn't get the disabled="true" attribute!
  ;
  ; BUG#8809
  ; The React would warn that the input stepped into uncontrolled state
  ; if it has no :on-change property. Therefore, the input DOM element must keep
  ; its :on-change property in {:disabled? true} state!
  ;
  ; BUG#8811
  ; In some cases the input element didn't fire the on-change function.
  ; Therefore it had been replaced by the on-input function.
  ; E.g.: When a text-field appeared on the UI with a content that was in the application
  ;       state before the field did mount and the first interaction with the field
  ;       was a full selection (cmd + A) and a clear action (backspace), the on-change
  ;       function somehow didn't fire.
  (merge {:class               :pe-plain-field--input
          :data-autofill-style :none
          :type                :text
          :id                  (hiccup/value field-id "input")
          :value               (plain-field.env/get-field-content field-id)}
         (if disabled? {:data-caret-color :hidden
                        :tab-index        -1
                        :on-change        (fn [_])}
                       {:on-blur          (fn [_] (r/dispatch [:pretty-elements.plain-field/field-blurred field-id field-props]))
                        :on-focus         (fn [_] (r/dispatch [:pretty-elements.plain-field/field-focused field-id field-props]))
                       ;:on-change        (fn [%] (plain-field.utils/on-change-f field-id field-props %))
                        :on-change        (fn [_])
                        :on-input         (fn [%] (plain-field.utils/on-change-f field-id field-props %))})))

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
  ;  :id (string)
  ;  :on-mouse-down (function)}
  [field-id {:keys [surface]}]
  ; @note (pretty-elements.button.attributes#4460)
  ; BUG#2105 (source-code/cljs/pretty_elements/text_field/attributes.cljs)
  (-> {:class                 :pe-plain-field--surface
       :data-box-shadow-color :default
       :id                    (hiccup/value field-id "surface")
       :on-mouse-down         #(.preventDefault %)}
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
  (-> {:class :pe-plain-field--body
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
  (-> {:class        :pe-plain-field
       :data-covered disabled?}
      (pretty-build-kit/class-attributes   field-props)
      (pretty-build-kit/outdent-attributes field-props)
      (pretty-build-kit/state-attributes   field-props)))
