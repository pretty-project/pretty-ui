
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.4
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.engine
    (:require [x.server-core.api :as a :refer [r]]
              [mid-plugins.item-lister.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.engine
(def request-id         engine/request-id)
(def resolver-id        engine/resolver-id)
(def new-item-uri       engine/new-item-uri)
(def add-new-item-event engine/add-new-item-event)
(def route-id           engine/route-id)
(def render-event       engine/render-event)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/initialize!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-lister/initialize! :products :product]
  (fn [_ [_ extension-id item-namespace]
      ;[:router/add-route! :products/route {...}]
       [:router/add-route! (route-id extension-id item-namespace)
                           ;:route-template "/products"
                           {:route-template (route-template extension-id item-namespace)
                           ;:client-event   [:item-lister/load! :products :product]
                            :client-event   [:item-lister/load! extension-id item-namespace]
                            :restricted?    true}]]))
