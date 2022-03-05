
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.focus-handler.side-effects
    (:require [x.app-core.api                       :as a]
              [x.app-environment.api                :as environment]
              [x.app-elements.target-handler.engine :as target-handler.engine]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  [element-id]
  (-> element-id target-handler.engine/element-id->target-id environment/focus-element!))

(defn blur-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  [element-id]
  (-> element-id target-handler.engine/element-id->target-id environment/blur-element!))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :elements/focus-element! focus-element!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :elements/blur-element! blur-element!)
