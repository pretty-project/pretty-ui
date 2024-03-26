
(ns pretty-inputs.field.views
    (:require [fruits.random.api                     :as random]
              [pretty-elements.adornment-group.views :as adornment-group.views]
              [pretty-elements.expandable.views      :as expandable.views]
              [pretty-guides.placeholder-text.views  :as placeholder-text.views]
              [pretty-inputs.engine.api              :as pretty-inputs.engine]
              [pretty-inputs.field.attributes        :as field.attributes]
              [pretty-inputs.field.prototypes        :as field.prototypes]
              [pretty-inputs.methods.api             :as pretty-inputs.methods]
              [pretty-models.api                     :as pretty-models]
              [pretty-subitems.api                   :as pretty-subitems]
              [reagent.core                          :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:expandable            expandable.views/SHORTHAND-KEY
                    :placeholder-text      placeholder-text.views/SHORTHAND-KEY
                    :end-adornment-group   adornment-group.views/SHORTHAND-MAP
                    :start-adornment-group adornment-group.views/SHORTHAND-MAP})

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
        (when placeholder-text [placeholder-text.views/view (pretty-subitems/subitem-id id :placeholder-text) placeholder-text])
        (when :always          [(pretty-models/field-input-auto-tag props) (field.attributes/input-attributes id props)])])

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
              (when start-adornment-group [adornment-group.views/view (pretty-subitems/subitem-id id :start-adornment-group) start-adornment-group])
              (when :always               [field-structure            (pretty-subitems/subitem-id id :structure)             props])
              (when end-adornment-group   [adornment-group.views/view (pretty-subitems/subitem-id id :end-adornment-group)   end-adornment-group])
              (when expandable            [expandable.views/view      (pretty-subitems/subitem-id id :expandable)            expandable])]])

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
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ; [Field input model](pretty-core/cljs/pretty-models/api.html#field-input-model)
  ; [Multiline content model](pretty-core/cljs/pretty-models/api.html#multiline-content-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented controls.
  ; Check out the implemented elements.
  ; Check out the implemented guides.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-inputs/field.png)
  ; [field {:border-radius       {:all :s}
  ;         :fill-color          :highlight
  ;         :indent              {:all :xs}
  ;         :get-value-f         #(deref  MY-ATOM)
  ;         :set-value-f         #(reset! MY-ATOM %)
  ;         :placeholder-text    {:content "My placeholder text"}
  ;         ;; start-adornment-group {...}
  ;         :end-adornment-group {:adornment-default {:icon {:icon-size :m}}
  ;                               :adornments [{:icon {:icon-name :close}}]}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-inputs.methods/apply-input-shorthand-map    id props SHORTHAND-MAP)
             props (pretty-inputs.methods/apply-input-presets          id props)
             props (pretty-inputs.methods/import-input-dynamic-props   id props)
             props (pretty-inputs.methods/import-input-field-events    id props)
             props (pretty-inputs.methods/import-input-field-value     id props)
             props (pretty-inputs.methods/import-input-focus-reference id props)
             props (pretty-inputs.methods/import-input-state-events    id props)
             props (pretty-inputs.methods/import-input-state           id props)
             props (field.prototypes/props-prototype                   id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
