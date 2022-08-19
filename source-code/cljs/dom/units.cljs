
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.units
    (:require [mid-fruits.math :as math]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn px->vh
  ; @param (px) n
  ;
  ; @usage
  ;  (dom/px->vh 420)
  ;
  ; @return (vh)
  [n]
  (-> n (/ (-> js/window .-innerHeight (/ 100))) math/floor))
  
(defn px->vw
  ; @param (px) n
  ;
  ; @usage
  ;  (dom/px->vw 420)
  ;
  ; @return (vw)
  [n]
  (-> n (/ (-> js/window .-innerWidth (/ 100))) math/floor))

(defn vh->px
  ; @param (vh) n
  ;
  ; @usage
  ;  (dom/vh->px 42)
  ;
  ; @return (px)
  [n]
  (-> n (* (-> js/window .-innerHeight (/ 100))) math/floor))

(defn vw->px
  ; @param (vw) n
  ;
  ; @usage
  ;  (dom/vw->px 42)
  ;
  ; @return (px)
  [n]
  (-> n (* (-> js/window .-innerWidth (/ 100))) math/floor))
