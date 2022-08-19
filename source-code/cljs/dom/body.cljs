

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.body)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-body-element
  ; @usage
  ;  (dom/get-body-element)
  ;
  ; @return (DOM-element)
  []
  (-> js/document (.getElementsByTagName "body")
                  (aget 0)))

                  

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-body-style-value
  ; @param (string) style-name
  ;
  ; @usage
  ;  (dom/get-element-style "background-color")
  ;
  ; @return (string)
  [style-name]
  (-> js/window (.getComputedStyle (-> js/document (.getElementsByTagName "body")
                                                   (aget 0)))
                (aget style-name)))
