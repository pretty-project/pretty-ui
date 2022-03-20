
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.event-handler.side-effects
    (:require [dom.api                                 :as dom]
              [x.app-core.api                          :as a]
              [x.app-environment.event-handler.helpers :as event-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-event-listener!
  ; @param (string) type
  ; @param (function) listener
  ; @param (string)(opt) element-id
  ;  Default: js/window
  ;
  ; @usage
  ;  (defn my-handler-f [e] ...)
  ;  (environment/add-event-listener! "mousemove" my-handler-f)
  [type listener & [element-id]]
  (let [target (event-handler.helpers/element-id->target element-id)]
       (dom/add-event-listener! type listener target)))

(defn remove-event-listener!
  ; @param (string) type
  ; @param (function) listener
  ; @param (string)(opt) element-id
  ;  Default: js/window
  ;
  ; @usage
  ;  (defn my-handler-f [e] ...)
  ;  (environment/remove-event-listener! "mousemove" my-handler-f)
  [type listener & [element-id]]
  (let [target (event-handler.helpers/element-id->target element-id)]
       (dom/remove-event-listener! type listener target)))

(defn add-event!
  ; @param (string) type
  ; @param (metamorphic-event) event
  ; @param (string)(opt) element-id
  ;  Default: js/window
  ;
  ; @usage
  ;  (environment/add-event! "mousemove" [:my-event])
  [type event & [element-id]]
  (let [listener #(a/dispatch event)
        target    (event-handler.helpers/element-id->target element-id)]
       (dom/add-event-listener! type listener target)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  (defn my-handler-f [e] ...)
;  [:environment/add-event-listener! "mousemove" my-handler-f]
(a/reg-fx :environment/add-event-listener! add-event-listener!)

; @usage
;  (defn my-handler-f [e] ...
;  [:environment/remove-event-listener! "mousemove" my-handler-f]
(a/reg-fx :environment/remove-event-listener! remove-event-listener!)

; @usage
;  [:environment/add-event! "mousemove" [:my-event]]
(a/reg-fx :environment/add-event! add-event!)
