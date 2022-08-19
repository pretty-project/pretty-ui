

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.selection
    (:require [dom.config        :as config]
              [mid-fruits.candy  :refer [param return]]
              [mid-fruits.css    :as css]
              [mid-fruits.io     :as io]
              [mid-fruits.map    :as map]
              [mid-fruits.math   :as math]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selection-start
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-selection-start my-element)
  ;
  ; @return (integer)
  [element]
  (.-selectionStart element))

(defn get-selection-end
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/get-selection-end my-element)
  ;
  ; @return (integer)
  [element]
  (.-selectionStart element))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-selection-start!
  ; @param (DOM-element) element
  ; @param (integer) selection-start
  ;
  ; @usage
  ;  (dom/set-selection-start! my-element 2)
  ;
  ; @return (?)
  [element selection-start]
  (-> element .-selectionStart (set! selection-start)))

(defn set-selection-end!
  ; @param (DOM-element) element
  ; @param (integer) selection-end
  ;
  ; @usage
  ;  (dom/set-selection-end! my-element 2)
  ;
  ; @return (?)
  [element selection-end]
  (-> element .-selectionEnd (set! selection-end)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-selection-range!
  ; @param (DOM-element) element
  ; @param (integer) selection-start
  ; @param (integer) selection-end
  ;
  ; @usage
  ;  (dom/set-selection-range! my-element 2 10)
  ;
  ; @return (?)
  [element selection-start selection-end]
  (set-selection-start! element selection-start)
  (set-selection-end!   element selection-end))

(defn set-caret-position!
  ; @param (DOM-element) element
  ; @param (integer) caret-position
  ;
  ; @usage
  ;  (dom/set-caret-position! my-element 20)
  [element caret-position]
  (set-selection-start! element caret-position)
  (set-selection-end!   element caret-position))
