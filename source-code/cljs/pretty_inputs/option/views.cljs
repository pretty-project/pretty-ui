
(ns pretty-inputs.option.views
    (:require [fruits.random.api               :as random]
              [pretty-accessories.icon.views   :as icon.views]
              [pretty-accessories.label.views  :as label.views]
              [pretty-guides.helper-text.views :as helper-text.views]
              [pretty-inputs.engine.api        :as pretty-inputs.engine]
              [pretty-inputs.methods.api       :as pretty-inputs.methods]
              [pretty-inputs.option.attributes :as option.attributes]
              [pretty-inputs.option.prototypes :as option.prototypes]
              [pretty-models.api               :as pretty-models]
              [pretty-subitems.api             :as pretty-subitems]
              [reagent.core                    :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:helper-text helper-text.views/SHORTHAND-KEY
                    :icon        icon.views/SHORTHAND-KEY
                    :label       label.views/SHORTHAND-KEY})

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
        [(pretty-models/click-control-auto-tag props) (option.attributes/inner-attributes id props)
         (if icon  [:div {:class :pi-option--checkmark} [icon.views/view        (pretty-subitems/subitem-id id :icon)        icon]])
         (if label [:div {:class :pi-option--content}   [label.views/view       (pretty-subitems/subitem-id id :label)       label]])
         (if helper-text                                [helper-text.views/view (pretty-subitems/subitem-id id :helper-text) helper-text])]])

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
  ; Customizable option element for selectable inputs.
  ;
  ; @links Implemented accessories
  ; [Icon](pretty-core/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-core/cljs/pretty-accessories/api.html#label)
  ;
  ; @links Implemented guides
  ; [Helper-text](pretty-core/cljs/pretty-guides/api.html#helper-text)
  ;
  ; @links Implemented models
  ; [Click control model](pretty-core/cljs/pretty-models/api.html#click-control-model)
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented guides.
  ; Check out the implemented models.
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
       (let [props (pretty-inputs.methods/apply-input-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-inputs.methods/apply-input-presets        id props)
             props (pretty-inputs.methods/import-input-dynamic-props id props)
             props (pretty-inputs.methods/import-input-state-events  id props)
             props (pretty-inputs.methods/import-input-state         id props)
             props (option.prototypes/props-prototype                id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
