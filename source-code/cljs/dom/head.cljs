
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.head)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-head-element
  ; @usage
  ;  (dom/get-head-element)
  ;
  ; @return (DOM-element)
  []
  (-> js/document (.getElementsByTagName "head")
                  (aget 0)))
