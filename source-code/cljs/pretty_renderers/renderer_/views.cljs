
(ns renderers.renderer.views
    (:require [fruits.hiccup.api        :as hiccup]
              [fruits.vector.api        :as vector]
              [reagent.api              :as reagent]
              [renderers.renderer.utils :as renderer.utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn renderer-contents
  ; @ignore
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  [renderer-id renderer-props]
  (let [content-order (-> @renderer.state/RENDERERS renderer-id :content-order)]
       (letfn [(f0 [content-id] [content-component renderer-id content-id])]
              (if (vector/nonempty? content-order)
                  [:div {:id (-> renderer-id renderer.utils/renderer-id->dom-id hiccup/value)}
                        (hiccup/put-with [:<>] content-order f0)]))))

(defn renderer
  ; @ignore
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  ; {:content-component (Reagent component symbol)
  ;  :max-elements-rendered (integer)
  ;  :queue-behavior (keyword)
  ;   :ignore, :push, :wait
  ;  :rerender-same? (boolean)(opt)}
  ;
  ; @usage
  ; (defn my-content-component [renderer-id content-id] ...)
  ; [renderer :my-renderer {:content-component #'my-content-component}]
  [renderer-id renderer-props]
  (reagent/lifecycles (renderer.utils/renderer-id->dom-id renderer-id)
                      {:reagent-render         (fn [] [renderer-contents                        renderer-id renderer-props])
                       :component-will-unmount (fn [] (renderer.side-effects/destruct-renderer! renderer-id renderer-props))
                       :component-did-mount    (fn [] (renderer.side-effects/init-renderer!     renderer-id renderer-props))}))
