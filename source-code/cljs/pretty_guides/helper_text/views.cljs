
(ns pretty-guides.helper-text.views
    (:require [fruits.random.api                    :as random]
              [pretty-guides.helper-text.attributes :as helper-text.attributes]
              [pretty-guides.helper-text.prototypes :as helper-text.prototypes]
              [pretty-accessories.methods.api :as pretty-accessories.methods]
              [pretty-accessories.engine.api :as pretty-accessories.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- helper-text
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [id {:keys [content] :as props}]
  [:div (helper-text.attributes/outer-attributes id props)
        [:div (helper-text.attributes/inner-attributes id props)
              [:div (helper-text.attributes/content-attributes id props) content]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-accessories.engine/accessory-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-accessories.engine/accessory-will-unmount id props))
                         :reagent-render         (fn [_ props] [helper-text id props])}))

(defn view
  ; @description
  ; Helper text guide for inputs.
  ;
  ; @links Implemented models
  ; [Container model](pretty-core/cljs/pretty-models/api.html#container-model)
  ; [Content model](pretty-core/cljs/pretty-models/api.html#content-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-guides/helper-text.png)
  ; [helper-text {:content "My helper text"}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [helper-text "My helper text"]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-accessories.methods/apply-accessory-shorthand-key  id props :content)
             props (pretty-accessories.methods/apply-accessory-preset         id props)
             props (pretty-accessories.methods/import-accessory-dynamic-props id props)
             props (pretty-accessories.methods/import-accessory-state-events  id props)
             props (pretty-accessories.methods/import-accessory-state         id props)
             props (helper-text.prototypes/props-prototype                    id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
