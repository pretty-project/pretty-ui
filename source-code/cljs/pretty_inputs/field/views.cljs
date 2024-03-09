
(ns pretty-inputs.field.views
    (:require [fruits.random.api               :as random]
              [pretty-elements.api :as pretty-elements]
              [pretty-guides.api               :as pretty-guides]
              [pretty-inputs.engine.api        :as pretty-inputs.engine]
              [pretty-inputs.methods.api :as pretty-inputs.methods]
              [pretty-inputs.field.attributes :as field.attributes]
              [pretty-inputs.field.prototypes :as field.prototypes]
              [pretty-models.api               :as pretty-models]
              [pretty-subitems.api             :as pretty-subitems]
              [reagent.core                    :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-structure
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:placeholder-text (map)(opt)
  ;  ...}
  [id {:keys [placeholder-text] :as props}]
  [:div (field.attributes/structure-attributes id props)
        (when placeholder-text [pretty-guides/placeholder-text (pretty-subitems/subitem-id id :placeholder-text) placeholder-text])
        (when :always          [(pretty-models/field-model-auto-tag props) (field.attributes/input-attributes id props)])])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:end-adornment-group (map)(opt)
  ;  :expandable (map)(opt)
  ;  :start-adornment-group (map)(opt)
  ;  ...}
  [id {:keys [end-adornment-group expandable start-adornment-group] :as props}]
  [:div (field.attributes/outer-attributes id props)
        [pretty-inputs.engine/input-synchronizer id props]
        [:div (field.attributes/inner-attributes id props)
              (when start-adornment-group [pretty-elements/adornment-group (pretty-subitems/subitem-id id :start-adornment-group) start-adornment-group])
              (when :always               [field-structure                 (pretty-subitems/subitem-id id :structure)             props])
              (when end-adornment-group   [pretty-elements/adornment-group (pretty-subitems/subitem-id id :end-adornment-group)   end-adornment-group])
              (when expandable            [pretty-elements/expandable      (pretty-subitems/subitem-id id :expandable)            expandable])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount id props))
                         :reagent-render         (fn [_ props] [field id props])}))

(defn view
  ; @description
  ; Customizable input field for field type inputs.
  ;
  ; @links Implemented controls
  ; + add empty-field control! TODO
  ;
  ; @links Implemented elements
  ; [Adornment-group](pretty-core/cljs/pretty-elements/api.html#adornment-group)
  ; [Expandable](pretty-core/cljs/pretty-elements/api.html#expandable)
  ;
  ; @links Implemented guides
  ; [Placeholder-text](pretty-core/cljs/pretty-guides/api.html#placeholder-text)
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Focus properties](pretty-core/cljs/pretty-properties/api.html#focus-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Input field properties](pretty-core/cljs/pretty-properties/api.html#input-field-properties)
  ; [Input validation properties](pretty-core/cljs/pretty-properties/api.html#input-validation-properties)
  ; [Input value properties](pretty-core/cljs/pretty-properties/api.html#input-value-properties)
  ; [Keypress control properties](pretty-core/cljs/pretty-properties/api.html#keypress-control-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented controls.
  ; Check out the implemented elements.
  ; Check out the implemented guides.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-inputs/field.png)
  ; [field {:border-radius       {:all :s}
  ;         :fill-color          :highlight
  ;         :indent              {:all :xs}
  ;         :get-value-f         #(deref  MY-ATOM)
  ;         :set-value-f         #(reset! MY-ATOM %)
  ;         :placeholder-text    {:content "My placeholder text"}
  ;         :end-adornment-group {:adornment-default {:icon {:icon-size :m}}
  ;                               :adornments [{:icon {:icon-name :close}}]}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-inputs.methods/apply-input-shorthand-map id props {:expandable :content :placeholder-text :content})
             props (pretty-inputs.methods/apply-input-preset        id props)
             props (pretty-inputs.methods/import-input-field-events id props)
             props (pretty-inputs.methods/import-input-field-value  id props)
             props (field.prototypes/props-prototype                id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
