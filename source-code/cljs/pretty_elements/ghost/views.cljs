
(ns pretty-elements.ghost.views
    (:require [fruits.random.api                :as random]
              [pretty-elements.engine.api       :as pretty-elements.engine]
              [pretty-elements.ghost.attributes :as ghost.attributes]
              [pretty-elements.ghost.prototypes :as ghost.prototypes]
              [pretty-elements.methods.api      :as pretty-elements.methods]
              [reagent.core                     :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  [:div (ghost.attributes/outer-attributes id props)
        [:div (ghost.attributes/inner-attributes id props)]])

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
                         :reagent-render         (fn [_ props] [ghost id props])}))

(defn view
  ; @description
  ; Placeholder element for loading contents (with optional loading animation).
  ;
  ; @links Implemented models
  ; [Plain container model](pretty-core/cljs/pretty-models/api.html#plain-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/ghost.png)
  ; [ghost {:border-radius {:all :m}
  ;         :fill-color    :muted
  ;         :outer-height  :xs
  ;         :outer-width   :5xl}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (ghost.prototypes/props-prototype                     id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
