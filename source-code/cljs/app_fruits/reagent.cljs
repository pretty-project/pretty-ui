
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.14
; Description:
; Version: v0.3.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-fruits.reagent
    (:require [reagent.core :as core]
              [reagent.dom  :as dom]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; reagent.core
(def adapt-react-class core/adapt-react-class)
(def as-element        core/as-element)
(def ratom             core/atom)

; reagent.dom
(def render dom/render)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component?
  ; @param (*)
  ;
  ; @example
  ;  (reagent/component? [:div "..."])
  ;  =>
  ;  false
  ;
  ; @example
  ;  (reagent/component? [my-component "..."])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (and (-> n vector?)
       (-> n first fn?)))

(defn ndis!
  ; @param (atom) atom
  ;
  ; @return (atom)
  [atom]
  (swap! atom not))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn lifecycles
  ; @param (map) lifecyles
  ;  {...}
  ;
  ; @return (map)
  [lifecyles]
  (core/create-class lifecyles))
