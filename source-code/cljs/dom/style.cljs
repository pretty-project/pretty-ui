
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.style
    (:require [mid-fruits.css :as css]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-style
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-element-style my-element)
  [element])
  ; ...

(defn set-element-style!
  ; @param (DOM-element) element
  ; @param (map) style
  ;
  ; @usage
  ;  (dom/set-element-style! my-element {:position "fixed" :top "0"})
  [element style]
  (let [parsed-style (css/unparse style)]
       (.setAttribute element "style" parsed-style)))

(defn remove-element-style!
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/remove-element-style! my-element)
  [element]
  (.removeAttribute element "style"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-style-value
  ; @param (DOM-element) element
  ; @param (string) style-name
  ;
  ; @usage
  ;  (dom/get-element-style my-element "position")
  ;
  ; @return (string)
  [element style-name]
  (-> js/window (.getComputedStyle element)
                (aget style-name)))

(defn set-element-style-value!
  ; @param (DOM-element) element
  ; @param (string) style-name
  ; @param (*) style-value
  ;
  ; @usage
  ;  (dom/set-element-style-value! my-element "position" "fixed")
  [element style-name style-value]
  (-> element .-style (aset style-name style-value)))

(defn remove-element-style-value!
  ; @param (DOM-element) element
  ; @param (string) style-name

  ; @usage
  ;  (dom/remove-element-style-value! my-element "position")
  [element style-name]
  (-> element .-style (aset style-name nil)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-computed-style
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-element-computed-style my-element)
  ;
  ; @return (CSSStyleDeclarationObject)
  ;  The returned object updates automatically when the element's styles are changed
  [element]
  (.getComputedStyle js/window element))
