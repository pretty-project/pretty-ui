
(ns pretty-accessories.icon.views
    (:require [fruits.random.api                  :as random]
              [pretty-accessories.engine.api      :as pretty-accessories.engine]
              [pretty-accessories.icon.attributes :as icon.attributes]
              [pretty-accessories.icon.prototypes :as icon.prototypes]
              [pretty-accessories.methods.api     :as pretty-accessories.methods]
              [reagent.core                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-KEY :icon-name)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:icon-name (keyword)(opt)
  ;  ...}
  [id {:keys [icon-name] :as props}]
  [:div (icon.attributes/outer-attributes id props)
        [:div (icon.attributes/inner-attributes id props)
              [:i (icon.attributes/body-attributes id props) icon-name]]])

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
                         :reagent-render         (fn [_ props] [icon id props])}))

(defn view
  ; @description
  ; Icon accessory for elements.
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ; [Icon canvas model](pretty-core/cljs/pretty-models/api.html#icon-canvas-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-accessories/icon.png)
  ; [icon {:icon-name :settings}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':icon-name' property.
  ; [icon :settings]
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
             props (icon.prototypes/props-prototype                           id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
