
(ns pretty-elements.horizontal-spacer.views
    (:require [fruits.random.api                            :as random]
              [pretty-elements.engine.api                   :as pretty-elements.engine]
              [pretty-elements.horizontal-spacer.attributes :as horizontal-spacer.attributes]
              [pretty-elements.horizontal-spacer.prototypes :as horizontal-spacer.prototypes]
              [pretty-elements.methods.api :as pretty-elements.methods]
              [reagent.core                                 :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-spacer
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  [:div (horizontal-spacer.attributes/outer-attributes id props)
        [:div (horizontal-spacer.attributes/inner-attributes id props)]])

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
                         :reagent-render         (fn [_ props] [horizontal-spacer id props])}))

(defn view
  ; @description
  ; Empty horizontal spacer element.
  ;
  ; @links Implemented models
  ; [Plain container model](pretty-core/cljs/pretty-models/api.html#plain-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/horizontal-spacer.png)
  ; [horizontal-spacer {:outer-height :s}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (horizontal-spacer.prototypes/props-prototype         id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
