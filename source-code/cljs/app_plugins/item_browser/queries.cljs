
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.15
; Description:
; Version: v0.7.6
; Compatibility: x4.5.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.queries
    (:require [mid-fruits.candy  :refer [param return]]
              [x.app-core.api    :as a :refer [r]]
              [app-plugins.item-browser.engine :as engine]
              [app-plugins.item-browser.subs   :as subs]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [resolver-id     (engine/resolver-id extension-id item-namespace :get)
        current-item-id (r subs/get-current-item-id db extension-id)]
       [:debug `(~resolver-id ~{:item-id current-item-id})]))
