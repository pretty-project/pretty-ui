
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.4.2
; Compatibility: x4.5.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.view-selector.events
    (:require [mid-fruits.candy  :refer [param return]]
              [x.server-core.api :as a :refer [r]]
              [server-plugins.view-selector.engine :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;
  ; @return (map)
  ;  {:label (metamorphic-content)
  ;   :routed? (boolean)}
  [extension-id selector-props]
  (merge {:label   extension-id
          :routed? true}
         (param selector-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn transfer-selector-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  [_ [_ extension-id selector-props]]
  [:core/reg-transfer! {:data-f      (fn [_] (return selector-props))
                        :target-path [extension-id :view-selector/meta-items]}])

(defn add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:routed? (boolean)}
  [_ [_ extension-id {:keys [routed?]}]]
  (if routed? [:router/add-route! (engine/route-id extension-id)
                                  {:route-template (engine/route-template         extension-id)
                                   :client-event   [:view-selector/load-selector! extension-id]
                                   :restricted?    true}]))

(defn add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:routed? (boolean)}
  [_ [_ extension-id {:keys [routed?]}]]
  (if routed? [:router/add-route! (engine/extended-route-id extension-id)
                                  {:route-template (engine/extended-route-template extension-id)
                                   :client-event   [:view-selector/load-selector!  extension-id]
                                   :restricted?    true}]))

(a/reg-event-fx
  :view-selector/initialize-selector!
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:allowed-view-ids (keywords in vector)(opt)
  ;   :default-view-id (keyword)(opt)
  ;    Default: DEFAULT-VIEW-ID
  ;   :label (metamorphic-content)(opt)
  ;   :routed? (boolean)(opt)
  ;    Default: true}
  ;
  ; @usage
  ;  [:view-selector/initialize-selector! :my-extension]
  ;
  ; @usage
  ;  [:view-selector/initialize-selector! :my-extension {:default-view-id :my-view}]
  ;
  ; @usage
  ;  [:view-selector/initialize-selector! :my-extension {:default-view-id  :my-view
  ;                                                      :allowed-view-ids [:my-view :your-view :our-view]}]
  (fn [cofx [_ extension-id selector-props]]
      (let [selector-props (selector-props-prototype extension-id selector-props)]
           {:dispatch-n [(r transfer-selector-props! cofx extension-id selector-props)
                         (r add-route!               cofx extension-id selector-props)
                         (r add-extended-route!      cofx extension-id selector-props)]})))
