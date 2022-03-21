
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.helpers
    (:require [mid.plugins.item-browser.core.helpers :as core.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.plugins.item-browser.core.helpers
(def collection-name core.helpers/collection-name)
(def component-id    core.helpers/component-id)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-items-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (core.helpers/default-items-path :my-extension :my-type)
  ;  =>
  ;  [:plugins :item-browser/downloaded-items :my-extension]
  ;
  ; @return (vector)
  [extension-id _]
  [:plugins :item-browser/downloaded-items extension-id])

(defn default-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (core.helpers/default-item-path :my-extension :my-type)
  ;  =>
  ;  [:plugins :item-browser/browsed-items :my-extension]
  ;
  ; @return (vector)
  [extension-id _]
  [:plugins :item-browser/browsed-items extension-id])
