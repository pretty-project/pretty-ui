
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.routes.effects
    (:require [engines.item-browser.routes.helpers :as routes.helpers]
              [mid-fruits.uri                      :as uri]
              [re-frame.api                        :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/add-base-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) browser-props
  ;  {:base-route (string)}
  (fn [_ [_ browser-id {:keys [base-route]}]]
      (let [base-route   (uri/valid-path      base-route)
            parent-route (uri/uri->parent-uri base-route)]
           [:router/add-route! (routes.helpers/route-id browser-id :base)
                               {:client-event   [:item-browser/handle-route! browser-id]
                                :route-parent   parent-route
                                :restricted?    true
                                :route-template base-route}])))

(r/reg-event-fx :item-browser/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) browser-props
  ;  {:base-route (string)}
  (fn [_ [_ browser-id {:keys [base-route]}]]
      (let [base-route     (uri/valid-path      base-route)
            extended-route (str                 base-route "/:item-id")
            parent-route   (uri/uri->parent-uri base-route)]
           [:router/add-route! (routes.helpers/route-id browser-id :extended)
                               {:client-event   [:item-browser/handle-route! browser-id]
                                :route-parent   parent-route
                                :restricted?    true
                                :route-template extended-route}])))
