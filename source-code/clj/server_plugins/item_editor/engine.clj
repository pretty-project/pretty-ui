
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
(def item-id->new-item?      engine/item-id->new-item?)
(def item-id->form-label     engine/item-id->form-label)
(def item-id->item-uri       engine/item-id->item-uri)
(def item-id-key             engine/item-id-key)
(def request-id              engine/request-id)
(def mutation-name           engine/mutation-name)
(def form-id                 engine/form-id)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def parent-uri              engine/parent-uri)
(def render-event            engine/render-event)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/initialize!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) editor-props
  ;  {:multi-view? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [:item-editor/initialize! :my-extension :my-type]
  ;
  ; @usage
  ;  [:item-editor/initialize! :my-extension :my-type {...}]
  (fn [_ [_ extension-id item-namespace {:keys [multi-view?]}]]
      {:dispatch [:router/add-route! (route-id extension-id item-namespace)
                                     {:route-template (route-template     extension-id item-namespace)
                                      :route-parent   (parent-uri         extension-id item-namespace)
                                      :client-event   [:item-editor/load! extension-id item-namespace]
                                      :restricted?    true}]
       :dispatch-if [(boolean multi-view?)
                     [:router/add-route! (extended-route-id extension-id item-namespace)
                                         {:route-template (extended-route-template extension-id item-namespace)
                                          :route-parent   (parent-uri              extension-id item-namespace)
                                          :client-event   [:item-editor/load!      extension-id item-namespace]
                                          :restricted?    true}]]}))
