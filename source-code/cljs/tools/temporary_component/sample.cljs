
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.temporary-component.sample
    (:require [tools.temporary-component.api :as temporary-component]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-component [])

(defn append-my-component!
  []
  (temporary-component/append-component! [my-component]))



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
  (temporary-component/append-component! [my-button] click-my-button!))

(defn remove-my-button!
  []
  (temporary-component/remove-component!))
