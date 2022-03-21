
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.helpers
    (:require [mid.plugins.item-editor.core.helpers :as core.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.plugins.item-editor.core.helpers
(def collection-name    core.helpers/collection-name)
(def value-path         core.helpers/value-path)
(def add-item-label     core.helpers/add-item-label)
(def edit-item-label    core.helpers/edit-item-label)
(def new-item-label     core.helpers/new-item-label)
(def unnamed-item-label core.helpers/unnamed-item-label)
(def component-id       core.helpers/component-id)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (core.helpers/default-item-path :my-extension :my-type)
  ;  =>
  ;  [:plugins :item-editor/edited-items :my-extension]
  ;
  ; @return (vector)
  [extension-id _]
  [:plugins :item-editor/edited-items extension-id])

(defn default-suggestions-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (core.helpers/default-suggestions-path :my-extension :my-type)
  ;  =>
  ;  [:plugins :item-editor/suggestions :my-extension]
  ;
  ;
  ; @return (vector)
  [extension-id _]
  [:plugins :item-editor/suggestions extension-id])
