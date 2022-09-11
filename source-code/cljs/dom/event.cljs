
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.event)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-mouse-left
  ; @param (DOM event) mouse-event
  ; @param (function) f
  ;
  ; @usage
  ;  (dom/on-mouse-left % (fn [] ...))
  ;
  ; @return (*)
  [mouse-event f]
  (if (= (.-button mouse-event) 0)
      (f)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stop-propagation!
  ; @param (DOM event) event
  ;
  ; @usage
  ;  (dom/stop-propagation! my-event)
  ;
  ; @return (*)
  [event]
  (.stopPropagation event))

(defn prevent-default!
  ; @param (DOM event) event
  ;
  ; @usage
  ;  (dom/prevent-default! my-event)
  ;
  ; @return (*)
  [event]
  (.preventDefault event))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event->node-name
  ; @param (DOM event) event
  ;
  ; @usage
  ;  (dom/event->node-name %)
  ;
  ; @return (string)
  [event]
  (-> event .-srcElement .-nodeName))

(defn event->value
  ; @param (dom-event) n
  ;
  ; @usage
  ;  (dom/event->value %)
  ;
  ; @return (*)
  [n]
  (-> n .-target .-value))
