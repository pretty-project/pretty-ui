
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.4.0
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.event-handler
    (:require [app-fruits.dom   :as dom]
              [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-id->target
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) element-id
  ;
  ; @return (DOM-element)
  [element-id]
  (if element-id (dom/get-element-by-id element-id)
                 (return                js/window)))



;; -- Side effect events ------------------------------------------------------
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
  (let [target (element-id->target element-id)]
       (dom/add-event-listener! type listener target)))

; @usage
;  (defn handler-f [e] (do-something!))
;  {:environment/add-event-listener! ["mousemove" handler-f]}
(a/reg-fx :environment/add-event-listener! add-event-listener!)

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
  (let [target (element-id->target element-id)]
       (dom/remove-event-listener! type listener target)))

; @usage
;  (defn handler-f [e] (do-something!))
;  {:environment/remove-event-listener! ["mousemove" handler-f]}
(a/reg-fx :environment/remove-event-listener! remove-event-listener!)

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
        target    (element-id->target element-id)]
       (dom/add-event-listener! type listener target)))

; @usage
;  {:environment/add-event! ["mousemove" [:do-something!]]}
(a/reg-fx :environment/add-event! add-event!)
