
(ns templates.module-frame.core.helpers
    (:require [templates.module-frame.core.state :as core.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-layout
  ; @param (keyword) module-id
  ;
  ; @usage
  ; (get-layout :my-module)
  ;
  ; @example
  ; (get-layout :my-module)
  ; =>
  ; :my-layout
  ;
  ; @return (keyword)
  [module-id]
  (get-in @core.state/LAYOUT [module-id :layout]))

(defn layout-selected?
  ; @param (keyword) module-id
  ; @param (keyword) layout
  ;
  ; @usage
  ; (layout-selected? :my-module :my-layout)
  ;
  ; @return (boolean)
  [module-id layout]
  (= layout (get-in @core.state/LAYOUT [module-id :layout])))

(defn set-layout!
  ; @param (keyword) module-id
  ; @param (keyword) layout
  ;
  ; @usage
  ; (set-layout! :my-module :my-layout)
  [module-id layout]
  (swap! core.state/LAYOUT assoc-in [module-id :layout] layout))
