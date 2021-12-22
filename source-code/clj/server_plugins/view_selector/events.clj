
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.3.2
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.view-selector.events
    (:require [x.server-core.api :as a :refer [r]]
              [server-plugins.view-selector.engine :as engine]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  [_ [_ extension-id selector-props]]
  [:router/add-route! (engine/route-id extension-id)
                      {:route-template (engine/route-template extension-id)
                       :client-event   [:view-selector/load-selector! extension-id selector-props]
                       :restricted?    true}])

(defn- add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  [_ [_ extension-id selector-props]]
  [:router/add-route! (engine/extended-route-id extension-id)
                      {:route-template (engine/extended-route-template extension-id)
                       :client-event   [:view-selector/load-selector! extension-id selector-props]
                       :restricted?    true}])

(a/reg-event-fx
  :view-selector/initialize!
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:allowed-view-ids (keywords in vector)(opt)
  ;    Ha a kiválasztott view-id értéke nem található meg az allowed-view-ids felsorolásban,
  ;    akkor behelyettesítésre kerül a default-view-id értékével.
  ;   :default-view-id (keyword)(opt)
  ;    Default: DEFAULT-VIEW-ID}
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
  (fn [cofx [_ extension-id selector-props]]
      {:dispatch-n [(r add-route!          cofx extension-id selector-props)
                    (r add-extended-route! cofx extension-id selector-props)]}))
