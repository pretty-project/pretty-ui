
(ns pretty-elements.chip.views
    (:require [fruits.random.api               :as random]
              [pretty-accessories.api          :as pretty-accessories]
              [pretty-elements.chip.attributes :as chip.attributes]
              [pretty-elements.chip.prototypes :as chip.prototypes]
              [pretty-elements.engine.api      :as pretty-elements.engine]
              [pretty-elements.methods.api     :as pretty-elements.methods]
              [pretty-models.api               :as pretty-models]
              [pretty-subitems.api             :as pretty-subitems]
              [reagent.core                    :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:label (map)(opt)
  ;  :tooltip (map)(opt)
  ;  ...}
  [id {:keys [label tooltip] :as props}]
  [:div (chip.attributes/outer-attributes id props)
        [(pretty-models/clickable-model-auto-tag props) (chip.attributes/inner-attributes id props)
         (if label   [pretty-accessories/label   (pretty-subitems/subitem-id id :label)   label])
         (if tooltip [pretty-accessories/tooltip (pretty-subitems/subitem-id id :tooltip) tooltip])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount id props))
                         :reagent-render         (fn [_ props] [chip id props])}))

(defn view
  ; @description
  ; Clickable chip style element.
  ;
  ; @links Implemented accessories
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented models
  ; [Container model](pretty-core/cljs/pretty-models/api.html#container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/chip.png)
  ; [chip {:border-radius {:all :s}
  ;        :fill-color    :primary
  ;        :indent        {:horizontal :s}
  ;        :label         {:content "My chip #1" :font-weight :semi-bold}
  ;        :outer-width   :l}]
  ;
  ; [chip {:border-color  :highlight
  ;        :border-radius {:all :s}
  ;        :fill-color    :highlight
  ;        :indent        {:horizontal :s}
  ;        :label         {:content "My chip #2" :font-weight :semi-bold}
  ;        :outer-width   :l}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-map  id props {:label :content})
             props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (chip.prototypes/props-prototype                      id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
