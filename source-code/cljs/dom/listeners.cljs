

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.listeners)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-event-listener!
  ; @param (string) type
  ; @param (function) listener-f
  ; @param (DOM-element)(opt) target
  ;  Default: js/window
  ;
  ; @usage
  ;  (dom/add-event-listener! "mousemove" (fn [] ...))
  ([type listener-f]        (.addEventListener js/window type listener-f false))
  ([type listener-f target] (.addEventListener target    type listener-f false)))

(defn remove-event-listener!
  ; @param (string) type
  ; @param (function) listener-f
  ; @param (DOM-element)(opt) target
  ;  Default: js/window
  ;
  ; @usage
  ;  (def my-listener-f (fn []))
  ;  (dom/remove-event-listener! "mousemove" my-listener-f)
  ([type listener-f]        (.removeEventListener js/window type listener-f false))
  ([type listener-f target] (.removeEventListener target    type listener-f false)))
