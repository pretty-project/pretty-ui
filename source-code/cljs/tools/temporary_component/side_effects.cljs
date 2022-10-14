
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.temporary-component.side-effects
    (:require [dom.api                           :as dom]
              [reagent.api                       :as reagent]
              [tools.temporary-component.helpers :as helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn append-component!
  ; @param (component) component
  ; @param (function)(opt) render-callback
  ;
  ; @usage W/O callback
  ;  (defn my-component [])
  ;  (temporary-component/append-component! [my-component])
  ;
  ; @usage W/ callback
  ;  (defn my-button [] [:a {:href "foo/bar"}])
  ;  (defn click-my-button! [] ...)
  ;  (temporary-component/append-component! [my-button] click-my-button!)
  ([component]
   (helpers/remove-container!)
   (helpers/create-container!)
   (let [temporary-container (dom/get-element-by-id "temporary-component")]
        (reagent/render component temporary-container)))

  ([component render-callback]
   (helpers/remove-container!)
   (helpers/create-container!)
   (let [temporary-container (dom/get-element-by-id "temporary-component")]
        (reagent/render component temporary-container render-callback))))

(defn remove-component!
  ; @usage
  ;  (temporary-component/remove-component!)
  []
  (helpers/remove-container!))
