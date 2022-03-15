
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.event-handler.side-effects
    (:require [app-fruits.dom                          :as dom]
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
  ;  (defn handler-f [e] (do-something!))
  ;  (environment/add-event-listener! "mousemove" handler-f)
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
  ;  (defn handler-f [e] (do-something!))
  ;  (environment/remove-event-listener! "mousemove" handler-f)
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
  ;  (environment/add-event! "mousemove" [:do-something!])
  [type event & [element-id]]
  (let [listener #(a/dispatch event)
        target    (event-handler.helpers/element-id->target element-id)]
       (dom/add-event-listener! type listener target)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  (defn handler-f [e] (do-something!))
;  [:environment/add-event-listener! "mousemove" handler-f]
(a/reg-fx :environment/add-event-listener! add-event-listener!)

; @usage
;  (defn handler-f [e] (do-something!))
;  [:environment/remove-event-listener! "mousemove" handler-f]
(a/reg-fx :environment/remove-event-listener! remove-event-listener!)

; @usage
;  [:environment/add-event! "mousemove" [:do-something!]]
(a/reg-fx :environment/add-event! add-event!)
