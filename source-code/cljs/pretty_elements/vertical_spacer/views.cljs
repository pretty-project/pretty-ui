
(ns pretty-elements.vertical-spacer.views
    (:require [fruits.random.api                          :as random]
              [pretty-elements.engine.api                 :as pretty-elements.engine]
              [pretty-elements.methods.api                :as pretty-elements.methods]
              [pretty-elements.vertical-spacer.attributes :as vertical-spacer.attributes]
              [pretty-elements.vertical-spacer.prototypes :as vertical-spacer.prototypes]
              [reagent.core                               :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vertical-spacer
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  [:div (vertical-spacer.attributes/outer-attributes id props)
        [:div (vertical-spacer.attributes/inner-attributes id props)]])

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
                         :reagent-render         (fn [_ props] [vertical-spacer id props])}))

(defn view
  ; @description
  ; Empty vertical spacer element.
  ;
  ; @links Implemented models
  ; [Plain container model](pretty-core/cljs/pretty-models/api.html#plain-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/vertical-spacer.png)
  ; [vertical-spacer {:outer-width :s}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (vertical-spacer.prototypes/props-prototype           id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
