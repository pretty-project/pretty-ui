
(ns pretty-guides.placeholder-text.views
    (:require [fruits.random.api                         :as random]
              [pretty-accessories.engine.api             :as pretty-accessories.engine]
              [pretty-accessories.methods.api            :as pretty-accessories.methods]
              [pretty-guides.placeholder-text.attributes :as placeholder-text.attributes]
              [pretty-guides.placeholder-text.prototypes :as placeholder-text.prototypes]
              [reagent.core                              :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-KEY :content)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- placeholder-text
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [id {:keys [content] :as props}]
  [:div (placeholder-text.attributes/outer-attributes id props)
        [:div (placeholder-text.attributes/inner-attributes id props)
              [:div (placeholder-text.attributes/body-attributes id props) content]]])

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
                         :reagent-render         (fn [_ props] [placeholder-text id props])}))

(defn view
  ; @description
  ; Placeholder text guide for inputs.
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ; [Plain content model](pretty-core/cljs/pretty-models/api.html#plain-content-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-guides/placeholder-text.png)
  ; [placeholder-text {:content "My placeholder text"}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [placeholder-text "My placeholder text"]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-accessories.methods/apply-accessory-shorthand-key  id props SHORTHAND-KEY)
             props (pretty-accessories.methods/apply-accessory-presets        id props)
             props (pretty-accessories.methods/import-accessory-dynamic-props id props)
             props (pretty-accessories.methods/import-accessory-state-events  id props)
             props (pretty-accessories.methods/import-accessory-state         id props)
             props (placeholder-text.prototypes/props-prototype               id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
