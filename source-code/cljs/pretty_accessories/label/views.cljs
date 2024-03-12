
(ns pretty-accessories.label.views
    (:require [fruits.random.api                   :as random]
              [pretty-accessories.engine.api       :as pretty-accessories.engine]
              [pretty-accessories.label.attributes :as label.attributes]
              [pretty-accessories.label.prototypes :as label.prototypes]
              [pretty-accessories.methods.api      :as pretty-accessories.methods]
              [reagent.core                        :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-KEY :content)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [id {:keys [content] :as props}]
  [:div (label.attributes/outer-attributes id props)
        [:div (label.attributes/inner-attributes id props)
              [:div (label.attributes/body-attributes id props) content]]])

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
                         :reagent-render         (fn [_ props] [label id props])}))

(defn view
  ; @description
  ; Label accessory for elements.
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ; [Plain content model](pretty-core/cljs/pretty-models/api.html#plain-content-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-accessories/label.png)
  ; [label {:border-color     :primary
  ;         :content          "My label"
  ;         :border-position  :bottom
  ;         :border-width     :xs
  ;         :gap              :auto
  ;         :horizontal-align :left
  ;         :outer-width      :xxl}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [label "My label"]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-accessories.methods/apply-accessory-shorthand-key  id props SHORTHAND-KEY)
             props (pretty-accessories.methods/apply-accessory-preset         id props)
             props (pretty-accessories.methods/import-accessory-dynamic-props id props)
             props (pretty-accessories.methods/import-accessory-state-events  id props)
             props (pretty-accessories.methods/import-accessory-state         id props)
             props (label.prototypes/props-prototype                          id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
