
(ns pretty-guides.info-text.views
    (:require [fruits.random.api                  :as random]
              [pretty-accessories.engine.api      :as pretty-accessories.engine]
              [pretty-accessories.methods.api     :as pretty-accessories.methods]
              [pretty-guides.info-text.attributes :as info-text.attributes]
              [pretty-guides.info-text.prototypes :as info-text.prototypes]
              [reagent.core                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-KEY :content)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- info-text
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [id {:keys [content] :as props}]
  [:div (info-text.attributes/outer-attributes id props)
        [:div (info-text.attributes/inner-attributes id props)
              [:div (info-text.attributes/body-attributes id props) content]]])

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
                         :reagent-render         (fn [_ props] [info-text id props])}))

(defn view
  ; @description
  ; Info text guide for inputs.
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ; [Plain content model](pretty-core/cljs/pretty-models/api.html#plain-content-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-guides/info-text.png)
  ; [info-text {:content "My info text"}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [info-text "My info text"]
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
             props (info-text.prototypes/props-prototype                      id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
