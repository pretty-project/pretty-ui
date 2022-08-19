

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.dimensions
    (:require [mid-fruits.candy :refer [return]]
              [mid-fruits.math  :as math]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-width
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-element-width my-element)
  ;
  ; @return (px)
  [element]
  (.-offsetWidth element))

(defn get-element-height
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-element-height my-element)
  ;
  ; @return (px)
  [element]
  (.-offsetHeight element))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-relative-left
  ; Relative position: relative to viewport position
  ;
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-element-relative-left my-element)
  ;
  ; @return (px)
  [element]
  (-> element .getBoundingClientRect .-left math/round))

(defn get-element-relative-top
  ; Relative position: relative to viewport position
  ;
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-element-relative-top my-element)
  ;
  ; @return (px)
  [element]
  (-> element .getBoundingClientRect .-top math/round))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-absolute-left
  ; Absolute position: relative to document position
  ;
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-element-absolute-left my-element)
  ;
  ; @return (px)
  [element]
  (math/round (+ (-> element     .getBoundingClientRect .-left)
                 (-> js/document .-documentElement .-scrollLeft))))

(defn get-element-absolute-top
  ; Absolute position: relative to document position
  ;
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-element-absolute-top my-element)
  ;
  ; @return (px)
  [element]
  (math/round (+ (-> element     .getBoundingClientRect .-top)
                 (-> js/document .-documentElement .-scrollTop))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-offset-left
  ; Offset position: relative to parent position
  ;
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-element-offset-left my-element)
  ;
  ; @return (px)
  [element]
  (-> element .-offsetLeft math/round))

(defn get-element-offset-top
  ; Offset position: relative to parent position
  ;
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-element-offset-top my-element)
  ;
  ; @return (px)
  [element]
  (-> element .-offsetTop math/round))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-masspoint-x
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-element-masspoint-x my-element)
  ;
  ; @return (px)
  [element]
  (math/round (+ (-> element     .-offsetWidth (/ 2))
                 (-> element     .getBoundingClientRect .-left)
                 (-> js/document .-documentElement      .-scrollLeft))))

(defn get-element-masspoint-y
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-element-masspoint-y my-element)
  ;
  ; @return (px)
  [element]
  (math/round (+ (-> element     .-offsetHeight (/ 2))
                 (-> element     .getBoundingClientRect .-left)
                 (-> js/document .-documentElement      .-scrollTop))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-on-viewport-left?
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/element-on-viewport-left? my-element)
  ;
  ; @return (boolean)
  [element]
  (<= (+ (-> element     .-offsetWidth (/ 2))
         (-> element     .getBoundingClientRect .-left)
         (-> js/document .-documentElement      .-scrollLeft))
      (-> js/window .-innerWidth (/ 2))))

(defn element-on-viewport-right?
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/element-on-viewport-right? my-element)
  ;
  ; @return (boolean)
  [element]
  (> (+ (-> element     .-offsetWidth (/ 2))
        (-> element     .getBoundingClientRect .-left)
        (-> js/document .-documentElement      .-scrollLeft))
     (-> js/window .-innerWidth (/ 2))))

(defn element-on-viewport-top?
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/element-on-viewport-top? my-element)
  ;
  ; @return (boolean)
  [element]
  (<= (+ (-> element     .-offsetHeight (/ 2))
         (-> element     .getBoundingClientRect .-left)
         (-> js/document .-documentElement      .-scrollTop))
      (-> js/window .-innerWidth (/ 2))))

(defn element-on-viewport-bottom?
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/element-on-viewport-bottom? my-element)
  ;
  ; @return (boolean)
  [element]
  (> (+ (-> element     .-offsetHeight (/ 2))
        (-> element     .getBoundingClientRect .-left)
        (-> js/document .-documentElement      .-scrollTop))
     (-> js/window .-innerWidth (/ 2))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-masspoint-orientation
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-element-masspoint-orientation my-element)
  ;
  ; @return (keyword)
  ;  :bl, :br, :tl, :tr
  [element]
  (if (element-on-viewport-bottom? element)
      (if (element-on-viewport-left? element)
          (return :bl)
          (return :br))
      (if (element-on-viewport-left? element)
          (return :tl)
          (return :tr))))
