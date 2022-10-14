
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.routes.effects
    (:require [mid-fruits.uri                     :as uri]
              [plugins.item-lister.routes.helpers :as routes.helpers]
              [re-frame.api                       :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/add-base-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) lister-props
  ;  {:base-route (string)}
  (fn [_ [_ lister-id {:keys [base-route]}]]
      (let [base-route (uri/valid-path base-route)]
           [:router/add-route! (routes.helpers/route-id lister-id :base)
                               {:client-event   [:item-lister/handle-route! lister-id]
                                :restricted?    true
                                :route-template base-route}])))
