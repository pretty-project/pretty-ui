
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.item-browser.core.helpers
    (:require [mid.plugins.plugin-handler.core.helpers :as core.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.plugins.plugin-handler.core.helpers
(def component-id      core.helpers/component-id)
(def default-data-path core.helpers/default-data-path)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-items-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @example
  ;  (core.helpers/default-items-path :my-browser)
  ;  =>
  ;  [:plugins :plugin-handler/downloaded-items :my-browser]
  ;
  ; @return (vector)
  [browser-id]
  (default-data-path browser-id :downloaded-items))

(defn default-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @example
  ;  (core.helpers/default-item-path :my-browser)
  ;  =>
  ;  [:plugins :plugin-handler/browsed-item :my-browser]
  ;
  ; @return (vector)
  [browser-id]
  (default-data-path browser-id :browsed-item))
