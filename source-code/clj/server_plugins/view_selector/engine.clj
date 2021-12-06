
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.view-selector.engine
    (:require [x.server-core.api :as a :refer [r]]
              [mid-plugins.view-selector.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.view-selector.engine
(def request-id              engine/request-id)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def route                   engine/route)
(def extended-route          engine/extended-route)
(def render-event            engine/render-event)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/initialize!
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:allowed-view-ids (keywords in vector)(opt)
  ;    Ha a kiválasztott view-id értéke nem található meg az allowed-view-ids felsorolásban,
  ;    akkor behelyettesítésre kerül a default-view-id értékével.
  ;   :default-view-id (keyword)(opt)}
  ;
  ; @usage
  ;  [:view-selector/initialize! :my-extension]
  ;
  ; @usage
  ;  [:view-selector/initialize! :my-extension {:default-view-id :my-view}]
  ;
  ; @usage
  ;  [:view-selector/initialize! :my-extension {:default-view-id  :my-view
  ;                                             :allowed-view-ids [:my-view :your-view :our-view]}]
  (fn [_ [_ extension-id selector-props]]
      {:dispatch-n [[:router/add-route! (route-id extension-id)
                                        {:route-template (route-template extension-id)
                                         :client-event   [:view-selector/load! extension-id selector-props]
                                         :restricted?    true}]
                    [:router/add-route! (extended-route-id extension-id)
                                        {:route-template (extended-route-template extension-id)
                                         :client-event   [:view-selector/load! extension-id selector-props]
                                         :restricted?    true}]]}))
