
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.temporary-component.sample
    (:require [x.app-tools.api :as tools]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-component [])

(defn append-my-component!
  []
  (tools/append-temporary-component! [my-component]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-button
  []
  [:a#my-button {:href "/my-link"}])
  
(defn click-my-button!
  []
  (.click (.getElementById js/document "my-button")))

(defn append-my-button!
  []
  (tools/append-temporary-component! [my-button] click-my-button!))

(defn remove-my-button!
  []
  (tools/remove-temporary-component!))
