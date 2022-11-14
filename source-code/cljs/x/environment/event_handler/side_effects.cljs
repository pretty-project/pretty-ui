
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.event-handler.side-effects
    (:require [dom.api                             :as dom]
              [re-frame.api                        :as r]
              [x.environment.event-handler.helpers :as event-handler.helpers]))



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
  ;  (add-event-listener! "mousemove" my-handler-f)
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
  ;  (remove-event-listener! "mousemove" my-handler-f)
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
  ;  (add-event! "mousemove" [:my-event])
  [type event & [element-id]]
  (let [listener #(r/dispatch event)
        target    (event-handler.helpers/element-id->target element-id)]
       (dom/add-event-listener! type listener target)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  (defn my-handler-f [e] ...)
;  [:x.environment/add-event-listener! "mousemove" my-handler-f]
(r/reg-fx :x.environment/add-event-listener! add-event-listener!)

; @usage
;  (defn my-handler-f [e] ...
;  [:x.environment/remove-event-listener! "mousemove" my-handler-f]
(r/reg-fx :x.environment/remove-event-listener! remove-event-listener!)

; @usage
;  [:x.environment/add-event! "mousemove" [:my-event]]
(r/reg-fx :x.environment/add-event! add-event!)
