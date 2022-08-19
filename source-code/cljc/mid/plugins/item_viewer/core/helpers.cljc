
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.item-viewer.core.helpers
    (:require [mid.plugins.plugin-handler.core.helpers :as core.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.plugins.plugin-handler.core.helpers
(def component-id      core.helpers/component-id)
(def default-data-path core.helpers/default-data-path)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @example
  ;  (core.helpers/default-item-path :my-viewer)
  ;  =>
  ;  [:plugins :plugin-handler/viewed-items :my-viewer]
  ;
  ; @return (vector)
  [viewer-id]
  (default-data-path viewer-id :viewed-items))
