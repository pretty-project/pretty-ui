
(ns pretty-renderers.content-renderer.views
    (:require [fruits.random.api                  :as random]
              [pretty-renderers.engine.api :as pretty-renderers.engine]
              [pretty-renderers.methods.api :as pretty-renderers.methods]
              [pretty-renderers.content-renderer.attributes :as content-renderer.attributes]
              [pretty-renderers.content-renderer.config     :as content-renderer.config]
              [pretty-renderers.content-renderer.prototypes :as content-renderer.prototypes]
              [reagent.core :as reagent]
              [transition-controller.api :as transition-controller]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-renderer
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:initial-content (multitype-content)(opt)
  ;  ...}
  [id {:keys [initial-content] :as props}]
  ; The 'content-renderer' component doesn't react on any change of the ':content' property, unless it gets re-mounted.
  [:div (content-renderer.attributes/outer-attributes id props)
        [:div (content-renderer.attributes/inner-attributes id props)
              [transition-controller/view id {:initial-content     (if initial-content [:div {:class :pr-content-renderer--body} initial-content])
                                              :transition-duration (-> content-renderer.config/TRANSITION-DURATION)}]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-renderers.engine/renderer-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-renderers.engine/renderer-will-unmount id props))
                         :reagent-render         (fn [_ props] [content-renderer id props])}))

(defn view
  ; @description
  ; ...
  ;
  ; @links Implemented models
  ; ...
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-renderers/content-renderer.png)
  ; ...
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-renderers.methods/apply-renderer-presets        id props)
             props (pretty-renderers.methods/import-renderer-dynamic-props id props)
             props (content-renderer.prototypes/props-prototype            id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
