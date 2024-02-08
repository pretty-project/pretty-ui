
(ns pretty-inputs.text-field.views
    (:require [fruits.random.api                   :as random]
              [fruits.vector.api                   :as vector]
              [metamorphic-content.api             :as metamorphic-content]
              [pretty-elements.api                 :as pretty-elements]
              [pretty-inputs.engine.api            :as pretty-inputs.engine]
              [pretty-inputs.header.views          :as header.views]
              [pretty-inputs.text-field.attributes :as text-field.attributes]
              [pretty-inputs.text-field.env        :as text-field.env]
              [pretty-inputs.text-field.prototypes :as text-field.prototypes]
              [pretty-presets.engine.api           :as pretty-presets.engine]
              [reagent.api                         :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-end-adornments
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:end-adornments (maps in vector)(opt)}
  [field-id {:keys [end-adornments] :as field-props}]
  (let [end-adornments (text-field.prototypes/end-adornments-prototype field-id field-props)]
       (if (vector/not-empty? end-adornments)
           [:div (text-field.attributes/field-adornments-attributes field-id field-props)
                 [pretty-elements/adornment-group {:adornments end-adornments}]]
           [:div (text-field.attributes/field-adornments-placeholder-attributes field-id field-props)])))

(defn field-start-adornments
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:start-adornments (maps in vector)(opt)}
  [field-id {:keys [start-adornments] :as field-props}]
  (let [start-adornments (text-field.prototypes/start-adornments-prototype field-id field-props)]
       (if (vector/not-empty? start-adornments)
           [:div (text-field.attributes/field-adornments-attributes field-id field-props)
                 [pretty-elements/adornment-group {:adornments start-adornments}]]
           [:div (text-field.attributes/field-adornments-placeholder-attributes field-id field-props)])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  [field-id {:keys [multiline? placeholder surface] :as field-props}]
  ; - The placeholder element has an absolute position. Therefore, ...
  ;   ... it must be placed within the same ancestor element as the input element!
  ;   ... but it cannot be in the very same parent element as the input element
  ;       (otherwise, somehow it covers the input, regardless their order)!
  ; - The surface element is placed outside of the input container, otherwise
  ;   it would be misplaced in case the text-field has indent (applied on the input container).
  ; - The surface element has a relatively positioned wrapper element, otherwise
  ;   it wouldn't shrink (in terms of width) in case the text-field has outdent.
  [:div (text-field.attributes/field-attributes  field-id field-props)
        [pretty-inputs.header.views/view         field-id field-props]
        [pretty-inputs.engine/input-synchronizer field-id field-props]
        [:div (text-field.attributes/field-body-attributes field-id field-props)
              [field-start-adornments field-id field-props]
              [:div {:class :pi-text-field--input-structure}
                    (if placeholder (if-let [field-empty? (pretty-inputs.engine/input-empty? field-id field-props)]
                                            [:div (text-field.attributes/field-placeholder-attributes field-id field-props)
                                                  (metamorphic-content/compose placeholder)]))
                    [:div (text-field.attributes/input-emphasize-attributes field-id field-props)
                          [(if multiline? :textarea :input)
                           (text-field.attributes/field-input-attributes field-id field-props)]]]
              [field-end-adornments field-id field-props]]
        (if surface (if (text-field.env/field-surface-visible? field-id field-props)
                        [:div {:class :pi-text-field--surface-wrapper}
                              [:div (text-field.attributes/field-surface-attributes field-id field-props)
                                    [metamorphic-content/compose surface]]]))])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    field-id field-props))
                       :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount field-id field-props))
                       :reagent-render         (fn [_ field-props] [text-field field-id field-props])}))

(defn view
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ; {:autofill-name (keyword)(opt)
  ;  :autofocus? (boolean)(opt)
  ;  :border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :emptiable? (boolean)(opt)
  ;  :end-adornment-default (map)(opt)
  ;  :end-adornments (maps in vector)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;   Default: :s
  ;  :font-weight (keyword or integer)(opt)
  ;   Default: :normal
  ;  :form-id (keyword)(opt)
  ;  :get-value-f (function)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :helper (metamorphic-content)(opt)
  ;  :hover-color (keyword or string)(opt)      ??
  ;  :hover-pattern (keyword)(opt)              ??
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info (metamorphic-content)(opt)
  ;  :initial-value (string)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :line-height (keyword, px or string)(opt)
  ;   Default: :text-block
  ;  :marker (map)(opt)
  ;  :max-length (integer)(opt)
  ;  :modifier-f (function)(opt)
  ;  :on-blur-f (function)(opt)
  ;  :on-changed-f (function)(opt)
  ;  :on-empty-f (function)(opt)
  ;  :on-enter-f (function)(opt)
  ;  :on-focus-f (function)(opt)
  ;  :on-invalid-f (function)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-type-ended-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :on-valid-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :set-value-f (function)(opt)
  ;  :start-adornment-default (map)(opt)
  ;  :start-adornments (maps in vector)(opt)
  ;  :style (map)(opt)
  ;  :surface (map)(opt)
  ;   {:border-radius (map)(opt)
  ;     {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;    :content (metamorphic-content)(opt)
  ;    :indent (map)(opt)
  ;    :placeholder (metamorphic-content)(opt)}
  ;  :theme (keyword)(opt)
  ;  :type (keyword)(opt)
  ;   :email, :number, :password, :tel, :text
  ;   Default: :text
  ;  :validate-when-change? (boolean)(opt)
  ;  :validate-when-leave? (boolean)(opt)
  ;  :validators (maps in vector)(opt)
  ;   [{:f (function)
  ;     :invalid-message (metamorphic-content)(opt)}]
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [text-field {...}]
  ;
  ; @usage
  ; [text-field :my-text-field {...}]
  ([field-props]
   [view (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parameterizing)
   (fn [_ field-props]
       (let [field-props (pretty-presets.engine/apply-preset          field-id field-props)
             field-props (text-field.prototypes/field-props-prototype field-id field-props)]

             ;field-props (pretty-elements.engine/element-subitem-field<-subitem-default field-id field-props :start-adornments :start-adornment-default)
             ;field-props (pretty-elements.engine/element-subitem-field<-disabled-state  field-id field-props :end-adornments   :end-adornment-default)
             ;field-props (pretty-elements.engine/element-subitem-field<-subitem-default field-id field-props :start-adornments :start-adornment-default)
             ;field-props (pretty-elements.engine/element-subitem-field<-disabled-state  field-id field-props :end-adornments   :end-adornment-default)
            [view-lifecycles field-id field-props]))))
