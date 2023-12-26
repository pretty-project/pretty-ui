
(ns renderers.surface-renderer.views
    (:require [dom.api                               :as dom]
              [fruits.random.api                     :as random]
              [metamorphic-content.api               :as metamorphic-content]
              [renderers.renderer.env                :as renderer.env]
              [renderers.surface-renderer.attributes :as surface-renderer.attributes]
              [renderers.surface-renderer.prototypes :as surface-renderer.prototypes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-component
  ; @ignore
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) surface-id
  [renderer-id surface-id]
  (reagent/lifecycles surface-id
                      {:reagent-render         (fn [] (if-let [content (renderer.env/get-content-prop renderer-id surface-id :content)]
                                                              [:div (surface-renderer.attributes/surface-content-attributes renderer-id surface-id)
                                                                    [metamorphic-content/compose content]]))
                       :component-did-mount    (fn [] (if autoreset-scroll-y? (dom/set-scroll-y! 0))
                                                      (if-let [on-mount (renderer.env/get-content-prop renderer-id surface-id :on-mount)]
                                                              (r/dispatch on-mount)))
                       :component-will-unmount (fn [] (if-let [on-unmount (renderer.env/get-content-prop renderer-id surface-id :on-unmount)]
                                                              (r/dispatch on-unmount)))}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn renderer
  ; @param (keyword)(opt) renderer-id
  ; @param (map) renderer-props
  ; {:max-elements-rendered (integer)(opt)
  ;   Default: 1}
  ;
  ; @usage
  ; [surface-renderer {...}]
  ;
  ; @usage
  ; [surface-renderer :my-surface-renderer {...}]
  ;
  ; @usage
  ; [surface-renderer {}]
  ; (r/dispatch [:pretty-renderers.surface-renderer/render-surface! :my-surface {...}])
  ([renderer-props]
   [renderer (random/generate-keyword) renderer-props])

  ([renderer-id renderer-props]
   (let [renderer-props (surface-renderer.prototypes/renderer-props-prototype renderer-props)]
        [renderer.views/renderer renderer-id (assoc renderer-props :content-component #'surface-component)])))
