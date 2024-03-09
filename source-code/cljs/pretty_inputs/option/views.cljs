
(ns pretty-inputs.option.views
    (:require [fruits.random.api               :as random]
              [pretty-accessories.api          :as pretty-accessories]
              [pretty-guides.api               :as pretty-guides]
              [pretty-inputs.engine.api        :as pretty-inputs.engine]
              [pretty-inputs.methods.api       :as pretty-inputs.methods]
              [pretty-inputs.option.attributes :as option.attributes]
              [pretty-inputs.option.prototypes :as option.prototypes]
              [pretty-models.api               :as pretty-models]
              [pretty-subitems.api             :as pretty-subitems]
              [reagent.core                    :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- option
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:helper-text (map)(opt)
  ;  :icon (map)(opt)
  ;  :label (map)(opt)
  ;  ...}
  [id {:keys [helper-text icon label] :as props}]
  [:div (option.attributes/outer-attributes id props)
        [(pretty-models/clickable-model-auto-tag props) (option.attributes/inner-attributes id props)
         (if icon  [:div {:class :pi-option--checkmark} [pretty-accessories/icon   (pretty-subitems/subitem-id id :icon)        icon]])
         (if label [:div {:class :pi-option--content}   [pretty-accessories/label  (pretty-subitems/subitem-id id :label)       label]])
         (if helper-text                                [pretty-guides/helper-text (pretty-subitems/subitem-id id :helper-text) helper-text])]])

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
                         :reagent-render         (fn [_ props] [option id props])}))

(defn view
  ; @description
  ; Customizable option element for optionable inputs.
  ;
  ; @links Implemented accessories
  ; [Icon](pretty-core/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-core/cljs/pretty-accessories/api.html#label)
  ;
  ; @links Implemented guides
  ; [Helper-text](pretty-core/cljs/pretty-guides/api.html#helper-text)
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented guides.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-inputs/option.png)
  ; [option {:helper-text {:content "My option helper"}
  ;          :icon        {:border-color  :muted
  ;                        :border-radius {:all :s}
  ;                        :border-width  :xs
  ;                        :icon-name     :done
  ;          :gap         :xs
  ;          :label       {:content "My option"}}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-inputs.methods/apply-input-shorthand-map  id props {:icon :icon-name :label :content})
             props (pretty-inputs.methods/apply-input-preset         id props)
             props (pretty-inputs.methods/import-input-dynamic-props id props)
             props (pretty-inputs.methods/import-input-state-events  id props)
             props (pretty-inputs.methods/import-input-state         id props)
             props (option.prototypes/props-prototype                id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
