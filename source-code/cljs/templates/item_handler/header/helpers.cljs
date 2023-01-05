
(ns templates.item-handler.header.helpers
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-did-mount-f
  ; @param (keyword) handler-id
  ; @param (map) bar-props
  ; {:menu-items (maps in vector)(opt)}
  [handler-id {:keys [menu-items]}]
  (letfn [(f [{:keys [as-default? label]}] (if as-default? label))]
         (if-let [default-item-label (some f menu-items)]
                 (r/dispatch [:x.gestures/init-view-handler! handler-id {:default-view-id default-item-label}]))))
