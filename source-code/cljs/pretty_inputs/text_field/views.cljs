
(ns pretty-inputs.text-field.views
    (:require [fruits.random.api                   :as random]
              [fruits.vector.api                   :as vector]
              [multitype-content.api               :as multitype-content]
              [pretty-elements.api                 :as pretty-elements]
              [pretty-inputs.engine.api            :as pretty-inputs.engine]
              [pretty-inputs.field.views :as field.views]
              [pretty-inputs.header.views          :as header.views]
              [pretty-inputs.text-field.attributes :as text-field.attributes]
              [pretty-inputs.text-field.env        :as text-field.env]
              [pretty-inputs.text-field.prototypes :as text-field.prototypes]
              [pretty-presets.engine.api           :as pretty-presets.engine]
              [reagent.core                        :as reagent]
              [pretty-subitems.api :as pretty-subitems]))

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
                 [pretty-elements/adornment-group field-id {:adornments end-adornments}]]
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
                 [pretty-elements/adornment-group field-id {:adornments start-adornments}]]
           [:div (text-field.attributes/field-adornments-placeholder-attributes field-id field-props)])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field_
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
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
  [:div (text-field.attributes/outer-attributes  field-id field-props)
        ;[pretty-inputs.header.views/view         field-id field-props] <- turn back on / temp
        [pretty-inputs.engine/input-synchronizer field-id field-props]
        [:div (text-field.attributes/inner-attributes field-id field-props)
              [field-start-adornments field-id field-props]
              [:div {:class :pi-text-field--input-structure}
                    (if placeholder (if-let [field-empty? (pretty-inputs.engine/input-empty? field-id field-props)]
                                            [:div (text-field.attributes/field-placeholder-attributes field-id field-props)
                                                  (multitype-content/compose placeholder)]))
                    [:div (text-field.attributes/input-emphasize-attributes field-id field-props)
                          [(if multiline? :textarea :input)
                           (text-field.attributes/field-input-attributes field-id field-props)]]]
              [field-end-adornments field-id field-props]]
        (if surface (if (text-field.env/field-surface-visible? field-id field-props)
                        [:div {:class :pi-text-field--surface-wrapper}
                              [:div (text-field.attributes/field-surface-attributes field-id field-props)
                                    [multitype-content/compose surface]]]))])

(defn- text-field-input
  [])
  ;[(pretty-models/input-field-auto-tag     id props)
   ;(text-field.attributes/value-attributes id props)])

(defn- text-field-structure
  [])
  ;[:div (text-field.attributes/structure-attributes id props)
  ;      (when start-adornment-group [pretty-elements/adornment-group (pretty-subitems/subitem-id id :start-adornment-group) start-adornment-group])
  ;      (cond value                 [text-field-input                (pretty-subitems/subitem-id id :input)                 props]
  ;            placeholder-text      [pretty-guides/placeholder-text  (pretty-subitems/subitem-id id :placeholder-text)      placeholder-text]
  ;      (when end-adornment-group   [pretty-elements/adornment-group (pretty-subitems/subitem-id id :end-adornment-group)   end-adornment-group])])

(defn- text-field
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {field (map)(opt)
  ;  :header (map)(opt)
  ;  ...}
  [id {:keys [field header] :as props}]
  [:div (text-field.attributes/outer-attributes id props)
        [:div (text-field.attributes/inner-attributes id props)
              (if header [header.views/view (pretty-subitems/subitem-id id :header) header])
              (if field  [field.views/view  (pretty-subitems/subitem-id id :field)  field])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/pseudo-input-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/pseudo-input-will-unmount id props))
                         :reagent-render         (fn [_ props] [text-field id props])}))

(defn view
  ; @description
  ; Text field input.
  ;
  ; @links Implemented elements
  ; [Adornment-group](pretty-core/cljs/pretty-elements/api.html#adornment-group)
  ;
  ; @links Implemented guides
  ; [Placeholder-text](pretty-core/cljs/pretty-guides/api.html#placeholder-text)
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Input state properties](pretty-core/cljs/pretty-properties/api.html#input-state-properties)
  ; [Input validation properties](pretty-core/cljs/pretty-properties/api.html#input-validation-properties)
  ; [Input value properties](pretty-core/cljs/pretty-properties/api.html#input-value-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented elements.
  ; Check out the implemented guides.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-inputs/field.png)
  ; [text-field {:header      {}
  ;              :field {:placeholder-text {}}
  ;              }]


  ; @param (keyword)(opt) id
  ; @param (map) props
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
  ;  :helper (multitype-content)(opt)
  ;  :hover-color (keyword or string)(opt)      ??
  ;  :hover-pattern (keyword)(opt)              ??
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info (multitype-content)(opt)
  ;  :initial-value (string)(opt)
  ;  :label (multitype-content)(opt)
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
  ;  :placeholder (multitype-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :set-value-f (function)(opt)
  ;  :start-adornment-default (map)(opt)
  ;  :start-adornments (maps in vector)(opt)
  ;  :style (map)(opt)
  ;  :surface (map)(opt)
  ;   {:border-radius (map)(opt)
  ;     {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;    :content (multitype-content)(opt)
  ;    :indent (map)(opt)
  ;    :placeholder (multitype-content)(opt)}
  ;  :theme (keyword)(opt)
  ;  :type (keyword)(opt)
  ;   :email, :number, :password, :tel, :text
  ;   Default: :text
  ;  :validate-when-change? (boolean)(opt)
  ;  :validate-when-leave? (boolean)(opt)
  ;  :validators (maps in vector)(opt)
  ;   [{:f (function)
  ;     :invalid-message (multitype-content)(opt)}]
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [text-field {...}]
  ;
  ; @usage
  ; [text-field :my-text-field {...}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset    id props)
             props (text-field.prototypes/props-prototype id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
