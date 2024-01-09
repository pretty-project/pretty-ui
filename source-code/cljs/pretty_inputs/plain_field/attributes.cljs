
(ns pretty-inputs.plain-field.attributes
    (:require [fruits.hiccup.api                 :as hiccup]
              [pretty-build-kit.api                    :as pretty-build-kit]
              [pretty-inputs.plain-field.env   :as plain-field.env]
              [pretty-inputs.plain-field.utils :as plain-field.utils]
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
  ; @bug (#8806)
  ; If the {:disabled? true} state of the 'plain-field' element set the disabled="true" attribute on the input DOM element ...
  ; ... the input would lose its focus.
  ; ... the 'on-blur' event wouldn't occur in some browsers. Therefore, ...
  ;     ... the keypress handler would stay in type mode.
  ;     ... the field would stay marked as focused.
  ; ... after the {:disabled? true} state would end, the field wouldn't get back its focused state.
  ; Therefore, the input DOM element shouldn't get the disabled="true" attribute!
  ;
  ; @bug (#8809)
  ; If the input has no ':on-change' property, the React would warn that the input stepped into an uncontrolled state.
  ; Therefore, the input DOM element must keep its ':on-change' property in {:disabled? true} state as well!
  ;


  ; @bug (#8811)
  ; In some cases the input element somehow didn't fire the 'on-change' function.
  ; Therefore, it had been replaced with the 'on-input' function.
  ; E.g., When a 'text-field' input appeared on the UI with a content that was in the application
  ;       state before the field did mount and the first interaction with the field
  ;       was a full selection (cmd + A) and a clear action (backspace), the on-change
  ;       function somehow didn't fire.


  (merge {:class               :pi-plain-field--input
          :data-autofill-style :none
          :type                :text
          :id                  (hiccup/value field-id "input")
          :value               (plain-field.env/get-field-content field-id)}
         (if disabled? {:data-caret-color :hidden
                        :tab-index        -1
                        :on-change        (fn [_])}
                       {:on-blur          (fn [_] (r/dispatch [:pretty-inputs.plain-field/field-blurred field-id field-props]))
                        :on-focus         (fn [_] (r/dispatch [:pretty-inputs.plain-field/field-focused field-id field-props]))
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
  ;  :on-mouse-down (function)}
  [field-id {:keys [surface]}]
  ; BUG#2105 (source-code/cljs/pretty_inputs/text_field/attributes.cljs)
  (-> {:class                 :pi-plain-field--surface
       :data-box-shadow-color :default
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
