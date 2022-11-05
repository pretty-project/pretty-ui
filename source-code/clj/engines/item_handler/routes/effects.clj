
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.routes.effects
    (:require [engines.item-handler.routes.helpers :as routes.helpers]
              [mid-fruits.uri                      :as uri]
              [re-frame.api                        :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) handler-props
  ;  {:base-route (string)}
  (fn [_ [_ handler-id {:keys [base-route]}]]
      (let [base-route     (uri/valid-path base-route)
            extended-route (str            base-route "/:item-id/edit")]
           [:router/add-route! (routes.helpers/route-id handler-id :extended)
                               {:client-event   [:item-handler/handle-route! handler-id]
                                :restricted?    true
                                ;:route-parent   base-route
                                :route-template extended-route}])))

(r/reg-event-fx :item-handler/add-creator-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) handler-props
  ;  {:base-route (string)}
  (fn [_ [_ handler-id {:keys [base-route]}]]
      (let [base-route    (uri/valid-path base-route)
            creator-route (str            base-route "/create")]
           [:router/add-route! (routes.helpers/route-id handler-id :creator)
                               {:client-event   [:item-handler/handle-route! handler-id]
                                :restricted?    true
                                :route-template creator-route}])))
