

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.focus)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-active-element
  ; @usage
  ;  (dom/get-active-element)
  ;
  ; @return (DOM-element)
  []
  (.-activeElement js/document))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-element!
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/focus-element!)
  [element]
  (.focus element))

(defn blur-element!
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/blur-element!)
  [element]
  (.blur element))
