
(ns pretty-engine.element.lifecycles.side-effects
    (:require [pretty-engine.element.state.side-effects :as element.state.side-effects]
              [pretty-engine.element.keypress.side-effects :as element.keypress.side-effects]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-did-mount
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {}
  [element-id {:keys [on-mount-f] :as element-props}]
  (element.keypress.side-effects/reg-element-keypress-events! element-id element-props)
  (if on-mount-f (on-mount-f)))

(defn element-did-update
  ; @note
  ; Use the 'element-did-update' lifecycle for elements that take the ':keypress' property.
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; @param (?) %
  [element-id _ %]
  ; Reregisters the keypress event of the element in case the element properties gets updated,
  ; to update the possibly changed 'on-click-f' function.
  (let [[_ element-props] (reagent/arguments %)]
       (element.keypress.side-effects/dereg-element-keypress-events! element-id element-props)
       (element.keypress.side-effects/reg-element-keypress-events!   element-id element-props)))

(defn element-will-unmount
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {}
  [element-id {:keys [on-unmount-f] :as element-props}]
  (element.keypress.side-effects/dereg-element-keypress-events! element-id element-props)
  (element.state.side-effects/clear-element-state!              element-id)
  (if on-unmount-f (on-unmount-f)))
