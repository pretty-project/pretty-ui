
(ns pretty-accessories.overlay.views
    (:require [fruits.random.api                     :as random]
              [pretty-accessories.engine.api         :as pretty-accessories.engine]
              [pretty-accessories.methods.api        :as pretty-accessories.methods]
              [pretty-accessories.overlay.attributes :as overlay.attributes]
              [pretty-accessories.overlay.prototypes :as overlay.prototypes]
              [reagent.core                          :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- overlay
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  [:div (overlay.attributes/outer-attributes id props)
        [:div (overlay.attributes/inner-attributes id props)]])

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
                         :reagent-render         (fn [_ props] [overlay id props])}))

(defn view
  ; @description
  ; Overlay accessory for elements.
  ;
  ; @links Implemented models
  ; [Plain container model](pretty-core/cljs/pretty-models/api.html#plain-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-accessories/overlay.png)
  ; [overlay {:fill-color :invert}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-accessories.methods/apply-accessory-preset         id props)
             props (pretty-accessories.methods/import-accessory-dynamic-props id props)
             props (pretty-accessories.methods/import-accessory-state-events  id props)
             props (pretty-accessories.methods/import-accessory-state         id props)
             props (overlay.prototypes/props-prototype                        id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
