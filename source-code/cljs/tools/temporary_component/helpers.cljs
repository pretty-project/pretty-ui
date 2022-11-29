
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.temporary-component.helpers
    (:require [dom.api :as dom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-environment-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (DOM-element)
  []
  (dom/get-body-element))

(defn create-container!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (DOM-element)
  []
  (let [environment-element (get-environment-element)
        temporary-container (dom/create-element! "DIV")]
       (dom/set-element-id!    temporary-container "temporary-component")
       (dom/set-element-style! temporary-container {:display :none})
       (dom/append-element!    environment-element temporary-container)))

(defn remove-container!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [temporary-container (dom/get-element-by-id "temporary-component")]
          (let [environment-element (get-environment-element)]
               (dom/remove-child! environment-element temporary-container))))
