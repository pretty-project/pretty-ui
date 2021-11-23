
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
(def request-id      engine/request-id)
(def render-event-id engine/render-event-id)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/initialize!
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:allowed-views (keywords in vector)(opt)
  ;    Ha a selected-view értéke nem található meg az allowed-views felsorolásban,
  ;    akkor behelyettesítésre kerül a default-view értékével.
  ;   :default-view (keyword)(opt)}
  ;
  ; @usage
  ;  [:view-selector/initialize! :settings]
  ;
  ; @usage
  ;  [:view-selector/initialize! :settings {:default-view :privacy}]
  ;
  ; @usage
  ;  [:view-selector/initialize! :settings {:default-view  :privacy
  ;                                         :allowed-views [:privacy :personal :appearance]}]
  (fn [_ [_ extension-id selector-props]]
                   ;[:router/add-route! :settings/route {...}]
      {:dispatch-n [[:router/add-route! (keyword extension-id :route)
                                        ;:route-template "/settings"
                                        {:route-template (route-template extension-id)
                                        ;:client-event   [:view-selector/load! :settings {...}]
                                         :client-event   [:view-selector/load! extension-id selector-props]
                                         :restricted?    true}]
                   ;[:router/add-route! :settings/extended-route {...}]
                    [:router/add-route! (keyword extension-id :extended-route)
                                        ;:route-template "/settings/:selected-view"
                                        {:route-template (extended-route-template extension-id)
                                        ;:client-event   [:view-selector/load! :settings {...}]
                                         :client-event   [:view-selector/load! extension-id selector-props]
                                         :restricted?    true}]]}))
