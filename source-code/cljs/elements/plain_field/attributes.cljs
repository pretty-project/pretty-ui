
(ns elements.plain-field.attributes
    (:require [elements.plain-field.env   :as plain-field.env]
              [elements.plain-field.utils :as plain-field.utils]
              [pretty-css.api             :as pretty-css]
              [hiccup.api                 :as hiccup]
              [re-frame.api               :as r]))

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
  ;  :data-autofill (keyword)
  ;  :data-caret-color (keyword)
  ;  :id (string)
  ;  :on-blur (function)
  ;  :on-change (function)
  ;  :on-focus (function)
  ;  :tab-index (integer)
  ;  :type (keyword)
  ;  :value (string)}
  [field-id {:keys [disabled?] :as field-props}]
  ; XXX#4460 (source-code/cljs/elements/button/views.cljs)
  ;
  ; BUG#8806
  ; If the {:disabled? true} state of the plain-field element would set the
  ; disabled="true" attribute on the input DOM element ...
  ; ... the input loses its focus.
  ; ... the on-blur event doesn't occur in some browsers, therefore
  ;     the keypress handler stays in type mode and the field stays marked
  ;     as focused.
  ; ... after the {:disabled? true} state ends, the field doesn't get back
  ;     its focused state.
  ; Therefore, the input DOM element doesn't get the disabled="true" attribute!
  ;
  ; BUG#8809
  ; If the {:disabled? true} state of the plain-field element removes the
  ; :on-change property of the input DOM element ...
  ; ... the React warns that the input stepped into uncontrolled state.
  ; Therefore, the input DOM element must keeps its :on-change property
  ; in {:disabled? true} state!
  (merge {:class         :e-plain-field--input
          :data-autofill :remove-style
          :type          :text
          :id            (hiccup/value field-id "input")
          :value         (plain-field.env/get-field-content field-id)}
         (if disabled? {:data-caret-color :hidden
                        :tab-index        -1
                        :on-change        (fn [])}
                       {:on-blur   #(r/dispatch [:elements.plain-field/field-blurred field-id field-props])
                        :on-focus  #(r/dispatch [:elements.plain-field/field-focused field-id field-props])
                        :on-change #(plain-field.utils/on-change-f field-id field-props %)})))

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
  ; XXX#4460 (source-code/cljs/elements/button/views.cljs)
  ; BUG#2105 (source-code/cljs/elements/text_field/attributes.cljs)
  (-> {:class                 :e-plain-field--surface
       :data-box-shadow-color :default
       :id                    (hiccup/value field-id "surface")
       :on-mouse-down         #(.preventDefault %)}
      (pretty-css/border-attributes surface)
      (pretty-css/indent-attributes surface)))

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
  (-> {:class :e-plain-field--body
       :style style}
      (pretty-css/indent-attributes field-props)))

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
  (-> {:class        :e-plain-field
       :data-covered disabled?}
      (pretty-css/default-attributes field-props)
      (pretty-css/outdent-attributes field-props)))
