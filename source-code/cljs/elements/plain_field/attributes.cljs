
(ns elements.plain-field.attributes
    (:require [elements.plain-field.helpers :as plain-field.helpers]
              [pretty-css.api               :as pretty-css]
              [hiccup.api                   :as hiccup]
              [re-frame.api                 :as r]))

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
          :value         (plain-field.helpers/get-field-content field-id)}
         (if disabled? {:data-caret-color :hidden
                        :tab-index        -1
                        :on-change        (fn [])}
                       {:on-blur   #(r/dispatch [:elements.plain-field/field-blurred field-id field-props])
                        :on-focus  #(r/dispatch [:elements.plain-field/field-focused field-id field-props])
                        :on-change #(plain-field.helpers/on-change-f field-id field-props %)})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-surface-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:id (string)
  ;  :on-mouse-down (function)}
  [field-id _]
  ; XXX#4460 (source-code/cljs/elements/button/views.cljs)
  ; BUG#2105
  {:class :e-plain-field--surface
   :id (hiccup/value field-id "surface")
   :on-mouse-down #(.preventDefault %)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-accessory-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:on-mouse-down (function)}
  [field-id _]
  ; BUG#2105
  ; An on-mouse-down event fired on anywhere out of the input triggers the
  ; on-blur event of the field, therefore the surface would dissapears unless
  ; if the on-mouse-down event prevented.
  ;
  ; If the user clicks on a field accessory (adornment, surface, placeholder, etc.)
  ; the field has been focused!
  {:on-mouse-down (fn [e] (.preventDefault e)
                          (r/dispatch-fx [:elements.plain-field/focus-field! field-id]))})

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
  ;
  ; @return (map)
  ; {}
  [_ field-props]
  (-> {:class :e-plain-field}
      (pretty-css/default-attributes field-props)
      (pretty-css/outdent-attributes field-props)))
