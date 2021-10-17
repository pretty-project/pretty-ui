
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.1.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.event-handler
    (:require [app-fruits.dom   :as dom]
              [mid-fruits.candy :refer [return]]
              [x.app-core.api   :as a]))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-id->target
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) element-id
  ;
  ; @return (DOM-element)
  [element-id]
  (if (some? element-id)
      (dom/get-element-by-id element-id)
      (return js/window)))



;; -- Side effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-handled-fx
  ::add-event-listener!
  ; @param (string) type
  ; @param (function) listener
  ; @param (string)(opt) element-id
  ;  Default: js/window
  ;
  ; @usage
  ;  (defn handler-f [e] (do-something!))
  ;  [::add-event-listener! "mousemove" handler-f]}
  (fn [[type listener element-id]]
      (let [target (element-id->target element-id)]
           (dom/add-event-listener! type listener target))))

(a/reg-handled-fx
  ::remove-event-listener!
  ; @param (string) type
  ; @param (function) listener
  ; @param (string)(opt) element-id
  ;  Default: js/window
  ;
  ; @usage
  ;  (defn handler-f [e] (do-something!))
  ;  [::remove-event-listener! "mousemove" handler-f]}
  (fn [[type listener element-id]]
      (let [target (element-id->target element-id)]
           (dom/remove-event-listener! type listener target))))

(a/reg-handled-fx
  ::add-event!
  ; @param (string) type
  ; @param (metamorphic-event) event
  ; @param (string)(opt) element-id
  ;  Default: js/window
  ;
  ; @usage
  ;  [::add-event! "mousemove" [:do-something!]]}
  (fn [[type event element-id]]
      (let [listener #(a/dispatch event)
            target    (element-id->target element-id)]
           (dom/add-event-listener! type listener target))))
