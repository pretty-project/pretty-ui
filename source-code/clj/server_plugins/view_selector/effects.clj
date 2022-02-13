
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.4.8
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.view-selector.effects
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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (fn [_ [_ extension-id selector-props]]
      (let [selector-props (selector-props-prototype extension-id selector-props)]
           {:dispatch-n [[:view-selector/reg-transfer-selector-props! extension-id selector-props]
                         [:view-selector/add-route!                   extension-id selector-props]
                         [:view-selector/add-extended-route!          extension-id selector-props]]})))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/reg-transfer-selector-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  (fn [_ [_ extension-id selector-props]]
      [:core/reg-transfer! (engine/transfer-id extension-id)
                           {:data-f      (fn [_] (return selector-props))
                            :target-path [extension-id :view-selector/meta-items]}]))

(a/reg-event-fx
  :view-selector/add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:routed? (boolean)}
  (fn [_ [_ extension-id {:keys [routed?]}]]
      (if routed? [:router/add-route! (engine/route-id extension-id)
                                      {:route-template (engine/route-template         extension-id)
                                       :client-event   [:view-selector/load-selector! extension-id]
                                       :restricted?    true}])))

(a/reg-event-fx
  :view-selector/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:routed? (boolean)}
  (fn [_ [_ extension-id {:keys [routed?]}]]
      (if routed? [:router/add-route! (engine/extended-route-id extension-id)
                                      {:route-template (engine/extended-route-template extension-id)
                                       :client-event   [:view-selector/load-selector!  extension-id]
                                       :restricted?    true}])))
