
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.routes.effects
    (:require [engines.item-lister.routes.helpers :as routes.helpers]
              [mid-fruits.uri                     :as uri]
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
                                :js-build       :app
                                :restricted?    true
                                :route-template base-route}])))
