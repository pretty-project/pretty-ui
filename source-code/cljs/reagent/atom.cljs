

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns reagent.atom
    (:require [reagent.core :as reagent.core]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; reagent.core
(def ratom reagent.core/atom)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn not!
  ; @param (atom) atom
  ;
  ; @usage
  ;  (reagent/not! my-atom)
  ;
  ; @return (atom)
  [atom]
  (swap! atom not))
