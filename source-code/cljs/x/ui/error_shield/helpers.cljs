
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.error-shield.helpers
    (:require [dom.api :as dom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-shield-hidden?
  ; @usage
  ;  (error-shield-hidden?)
  ;
  ; @return (boolean)
  []
  (let [shield-element (dom/get-element-by-id       "x-error-shield")
        display-value  (dom/get-element-style-value shield-element "display")]
       (= "none" display-value)))
