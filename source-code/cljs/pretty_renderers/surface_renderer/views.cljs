
(ns renderers.surface-renderer.views
    (:require [dom.api                               :as dom]
              [metamorphic-content.api               :as metamorphic-content]
              [random.api                            :as random]
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
  ; @description
  ; Rendering/removing content on/from the 'surface-renderer' renderer could be
  ; done by its Re-Frame effects.
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  ; {:autoreset-scroll-y? (boolean)(opt)
  ;   Resets the document scroll Y position when a new content rendered.
  ;   Default: true
  ;  :max-elements-rendered (integer)(opt)
  ;   Default: 1
  ;  :queue-behavior (keyword)
  ;   If the rendered element count exceeds the :max-elements-rendered property value,
  ;   a new content rendering request could ...
  ;   ... be ignored.
  ;   ... pushes the oldest rendered content out of the queue in order to render the new content.
  ;   ... being put to the end of the queue and wait until another rendered content is being removed.
  ;   :ignore, :push, :wait
  ;   Default: :push
  ;  :rerender-same? (boolean)
  ;   If a content rendering request has the same ID as an already rendered content
  ;   this property determines whether the new content replaces the old one.
  ;   Default: false}
  ;
  ; @usage
  ; [surface-renderer {...}]
  ;
  ; @usage
  ; [surface-renderer :my-surface-renderer {...}]
  ;
  ; @usage
  ; [surface-renderer {:queue-behavior :ignore
  ;                    :rerender-same? true}]
  ; (r/dispatch [:renderers.surface-renderer/render-surface! :my-surface {...}])
  ([renderer-props]
   [renderer (random/generate-keyword) renderer-props])

  ([renderer-id renderer-props]
   (let [renderer-props (surface-renderer.prototypes/renderer-props-prototype renderer-props)]
        [renderer.views/renderer renderer-id (assoc renderer-props :content-component #'surface-component)])))
