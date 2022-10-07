
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.mouse
    (:require [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-mouse-x
  ; @param (DOM-event) mouse-event
  ;
  ; @usage
  ;  (dom/get-mouse-x %)
  ;
  ; @return (px)
  [mouse-event]
  (.-clientX mouse-event))

(defn get-mouse-y
  ; @param (DOM-event) mouse-event
  ;
  ; @usage
  ;  (dom/get-mouse-x %)
  ;
  ; @return (px)
  [mouse-event]
  (.-clientY mouse-event))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-mouse-viewport-quarter
  ; @param (DOM-event) mouse-event
  ;
  ; @usage
  ;  (dom/get-mouse-viewport-quarter %)
  ;
  ; @return (keyword)
  ;  :tl, :tr, :bl, :br
  [mouse-event]
  (let [half-viewport-height (-> js/window .-innerHeight (/ 2))
        half-viewport-width  (-> js/window .-innerWidth  (/ 2))
        mouse-x              (.-clientX mouse-event)
        mouse-y              (.-clientY mouse-event)]
       (cond (and (< mouse-x half-viewport-width)
                  (< mouse-y half-viewport-height))
             :tl
             (and (>= mouse-x half-viewport-width)
                  (<  mouse-y half-viewport-height))
             :tr
             (< mouse-x half-viewport-width)
             :bl :return :br)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-preventer
  ; @param (DOM-event) mouse-event
  ;
  ; @usage
  ;  (dom/select-preventer %)
  [mouse-event]
  (let [node-name (-> mouse-event .-srcElement .-nodeName string/lowercase)]
       ; Az input es textarea elemek hasznalatahoz szukseg van mouse-down eventre!
       (when-not (or (= node-name "input")
                     (= node-name "textarea"))
                 (do (-> mouse-event .preventDefault)
                     (-> js/document .-activeElement .blur)))))
