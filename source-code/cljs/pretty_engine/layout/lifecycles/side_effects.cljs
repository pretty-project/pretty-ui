
(ns pretty-engine.layout.lifecycles.side-effects
    (:require [pretty-engine.layout.keypress.side-effects :as layout.keypress.side-effects]
              [pretty-engine.layout.state.side-effects :as layout.state.side-effects]
              [keypress-handler.api :as keypress-handler]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-did-mount
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ; {}
  [layout-id {:keys [on-mount-f] :as layout-props}]
  (layout.keypress.side-effects/reg-layout-keypress-events! layout-id layout-props)
  (if on-mount-f (on-mount-f)))

(defn layout-will-unmount
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ; {}
  [layout-id {:keys [on-unmount-f] :as layout-props}]
  (layout.keypress.side-effects/dereg-layout-keypress-events! layout-id layout-props)
  (layout.state.side-effects/clear-layout-state!              layout-id)
  (if on-unmount-f (on-unmount-f)))
