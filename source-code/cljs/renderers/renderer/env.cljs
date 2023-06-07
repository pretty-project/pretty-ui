
(ns renderers.renderer.env
    (:require [renderers.renderer.state :as renderer.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-content-prop
  ; @ignore
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) content-id
  ; @param (keyword) prop-key
  ;
  ; @usage
  ; (get-content-prop :my-renderer :my-content :on-mount)
  ;
  ; @return (*)
  [renderer-id content-id prop-key]
  (-> renderer.state/RENDERERS renderer-id :rendered-contents content-id prop-key))
