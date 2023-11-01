
(ns renderers.renderer.side-effects
    (:require [renderers.renderer.state :as renderer.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-renderer!
  ; @ignore
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  [renderer-id renderer-props]
  (swap! assoc renderer.state/RENDERERS renderer-id {:rendered-contents {}
                                                     :content-order     []
                                                     :meta-items        renderer-props}))

(defn destruct-renderer!
  ; @ignore
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  [_ _])
