
(ns pretty-engine.element.surface.side-effects
    (:require [pretty-engine.element.state.side-effects :as element.state.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hide-element-surface!
  ; @ignore
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  [element-id _]
  (element.state.side-effects/update-element-state! element-id dissoc :popup-rendered?))

(defn show-element-surface!
  ; @ignore
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  [element-id element-props]
  (element.state.side-effects/update-all-element-state! dissoc :surface-rendered?)
  (element.state.side-effects/update-element-state! element-id assoc :surface-rendered? true))
