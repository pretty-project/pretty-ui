
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
  ; @param (function) f
  ;
  ; @usage
  ;  (dom/stop-propagation! % (fn [] ...))
  ;
  ; @return (*)
  [event f]
  (.stopPropagation event)
  (f))

(defn prevent-default!
  ; @param (DOM event) event
  ; @param (function) f
  ;
  ; @usage
  ;  (dom/prevent-default! % (fn [] ...))
  ;
  ; @return (*)
  [event f]
  (.preventDefault event)
  (f))


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
