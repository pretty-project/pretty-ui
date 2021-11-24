
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-browser.engine
    (:require [x.server-core.api :as a :refer [r]]
              [mid-plugins.item-browser.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-browser.engine
(def request-id              engine/request-id)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def go-up-event             engine/go-up-event)
(def go-home-event           engine/go-home-event)
(def render-event            engine/render-event)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/initialize!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;
  ; @usage
  ;  [:item-browser/initialize! :my-extension :my-type {...}]
  (fn [_ [_ extension-name item-name browser-props]]
      {:dispatch-n [[:router/add-route! (route-id extension-id item-namespace)
                                        {:route-event    [:item-browser/load! extension-name item-name browser-props]
                                         :route-template (route-template extension-id item-namespace)
                                         :restricted?    true}]
                    [:router/add-route! (extended-route-id extension-id item-namespace)
                                        {:route-event    [:item-browser/load! extension-name item-name browser-props]
                                         :route-template (extended-route-template extension-id item-namespace)
                                         :restricted?    true}]]}))
