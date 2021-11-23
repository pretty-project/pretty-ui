
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.engine
    (:require [x.server-core.api :as a :refer [r]]
              [mid-plugins.item-editor.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.engine
(def extension-namespace engine/extension-namespace)
(def item-id->new-item?  engine/item-id->new-item?)
(def item-id->form-label engine/item-id->form-label)
(def item-id->item-uri   engine/item-id->item-uri)
(def request-id          engine/request-id)
(def mutation-name       engine/mutation-name)
(def form-id             engine/form-id)
(def route-id            engine/route-id)
(def route-template      engine/route-template)
(def parent-uri          engine/parent-uri)
(def render-event-id     engine/render-event-id)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/initialize!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/initialize! :products :product]
  (fn [_ [_ extension-id item-namespace]
      ;[:router/add-route! :products/editor-route {...}]
       [:router/add-route! (route-id extension-id item-namespace)
                           ;:route-template "/products/:product-id"
                           {:route-template (route-template extension-id item-namespace)
                           ;:route-parent   "/products"
                            :route-parent   (parent-uri     extension-id item-namespace)
                           ;:client-event   [:item-editor/load! :products :product]
                            :client-event   [:item-editor/load! extension-id item-namespace]
                            :restricted?    true}]]))
