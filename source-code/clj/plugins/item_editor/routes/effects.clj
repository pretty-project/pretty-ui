
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.routes.effects
    (:require [mid-fruits.uri                     :as uri]
              [plugins.item-editor.routes.helpers :as routes.helpers]
              [x.server-core.api                  :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:base-route (string)}
  (fn [_ [_ editor-id {:keys [base-route]}]]
      (let [base-route     (uri/valid-path base-route)
            extended-route (str            base-route "/:item-id")]
           [:router/add-route! (routes.helpers/route-id editor-id :extended)
                               {:client-event   [:item-editor/handle-route! editor-id]
                                :restricted?    true
                                :route-template extended-route}])))

(a/reg-event-fx
  :item-editor/add-sub-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:base-route (string)}
  (fn [_ [_ editor-id {:keys [base-route]}]]
      (let [base-route (uri/valid-path base-route)
            sub-route  (str            base-route "/:item-id/:view-id")]
           [:router/add-route! (routes.helpers/route-id editor-id :sub)
                               {:client-event   [:item-editor/handle-route! editor-id]
                                :restricted?    true
                                :route-parent   base-route
                                :route-template sub-route}])))
