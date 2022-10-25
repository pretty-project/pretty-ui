
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.routes.effects
    (:require [engines.item-viewer.routes.helpers :as routes.helpers]
              [mid-fruits.uri                     :as uri]
              [re-frame.api                       :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-viewer/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) viewer-props
  ;  {:base-route (string)}
  (fn [_ [_ viewer-id {:keys [base-route]}]]
      (let [base-route     (uri/valid-path base-route)
            extended-route (str            base-route "/:item-id")]
           [:router/add-route! (routes.helpers/route-id viewer-id :extended)
                               {:client-event   [:item-viewer/handle-route! viewer-id]
                                :restricted?    true
                                :route-template extended-route}])))