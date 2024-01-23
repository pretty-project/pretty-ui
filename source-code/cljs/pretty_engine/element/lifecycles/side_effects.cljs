
(ns pretty-engine.element.lifecycles.side-effects
    (:require [pretty-engine.element.state.side-effects :as element.state.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-did-mount
  ; @ignore
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {}
  [_ {:keys [on-mount-f]}]
  (if on-mount-f (on-mount-f)))

(defn element-will-unmount
  ; @ignore
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {}
  [element-id {:keys [on-unmount-f]}]
  (element.state.side-effects/clear-element-state! element-id)
  (if on-unmount-f (on-unmount-f)))
