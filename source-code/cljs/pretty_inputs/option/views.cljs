
(ns pretty-inputs.option.views
    (:require [fruits.random.api                 :as random]
              [pretty-inputs.option.attributes :as option.attributes]
              [pretty-inputs.option.prototypes :as option.prototypes]
              [pretty-inputs.engine.api          :as pretty-inputs.engine]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-guides.api :as pretty-guides]
              [pretty-accessories.api :as pretty-accessories]
              [pretty-models.api :as pretty-models]
              [pretty-subitems.api :as pretty-subitems]))

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
        [(pretty-models/clickable-auto-tag   id props)
         (option.attributes/inner-attributes id props)
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
  ; [Background action properties](pretty-core/cljs/pretty-properties/api.html#background-action-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Clickable state properties](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Effect properties](pretty-core/cljs/pretty-properties/api.html#effect-properties)
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
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
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
       (let [props (pretty-presets.engine/apply-preset id props)
             props (option.prototypes/props-prototype  id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
