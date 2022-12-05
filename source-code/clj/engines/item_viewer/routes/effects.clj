
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.routes.effects
    (:require [engines.item-viewer.routes.helpers :as routes.helpers]
              [re-frame.api                       :as r]
              [uri.api                            :as uri]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-viewer/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) viewer-props
  ; {:base-route (string)}
  (fn [_ [_ viewer-id {:keys [base-route]}]]
      (let [base-route     (uri/valid-path base-route)
            extended-route (str            base-route "/:item-id")]
           [:x.router/add-route! (routes.helpers/route-id viewer-id :extended)
                                 {:client-event   [:item-viewer/handle-route! viewer-id]
                                  :js-build       :app
                                  :restricted?    true
                                  :route-template extended-route}])))
