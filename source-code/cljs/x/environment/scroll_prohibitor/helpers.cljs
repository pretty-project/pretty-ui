
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.scroll-prohibitor.helpers
    (:require [dom.api :as dom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dom-scroll-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  []
  (let [data-scroll-disabled (dom/get-element-attribute (dom/get-document-element) "data-scroll-disabled")]
       (= data-scroll-disabled "true")))
