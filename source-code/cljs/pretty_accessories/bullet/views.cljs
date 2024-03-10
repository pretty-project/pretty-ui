
(ns pretty-accessories.bullet.views
    (:require [fruits.random.api                    :as random]
              [pretty-accessories.bullet.attributes :as bullet.attributes]
              [pretty-accessories.bullet.prototypes :as bullet.prototypes]
              [pretty-accessories.engine.api        :as pretty-accessories.engine]
              [pretty-accessories.methods.api       :as pretty-accessories.methods]
              [reagent.core                         :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bullet
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  [:div (bullet.attributes/outer-attributes id props)
        [:div (bullet.attributes/inner-attributes id props)]])

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
                         :reagent-render         (fn [_ props] [bullet id props])}))

(defn view
  ; @description
  ; Bullet accessory for elements.
  ;
  ; @links Implemented models
  ; [Plain container model](pretty-core/cljs/pretty-models/api.html#plain-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-accessories/bullet.png)
  ; [bullet {:border-radius {:all :xs}
  ;          :fill-color    :muted
  ;          :outer-height  :xs
  ;          :outer-width   :xs}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-accessories.methods/apply-accessory-preset         id props)
             props (pretty-accessories.methods/import-accessory-dynamic-props id props)
             props (pretty-accessories.methods/import-accessory-state-events  id props)
             props (pretty-accessories.methods/import-accessory-state         id props)
             props (bullet.prototypes/props-prototype                         id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
