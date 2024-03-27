
(ns pretty-renderers.content-renderer.views
    (:require [fruits.random.api                            :as random]
              [pretty-renderers.content-renderer.attributes :as content-renderer.attributes]
              [pretty-renderers.content-renderer.prototypes :as content-renderer.prototypes]
              [pretty-renderers.engine.api                  :as pretty-renderers.engine]
              [pretty-renderers.methods.api                 :as pretty-renderers.methods]
              [reagent.core                                 :as reagent]
              [transition-controller.api                    :as transition-controller]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-renderer
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [id {:keys [content] :as props}]
  [:div (content-renderer.attributes/outer-attributes id props)
        [:div (content-renderer.attributes/inner-attributes id props)
              [:div (content-renderer.attributes/body-attributes id props) content]]])

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
       (let [props (pretty-renderers.methods/apply-renderer-presets id props)
             props (content-renderer.prototypes/props-prototype     id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
