
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.routes.effects
    (:require [engines.item-editor.routes.helpers :as routes.helpers]
              [re-frame.api                       :as r]
              [uri.api                            :as uri]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-editor/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:base-route (string)}
  (fn [_ [_ editor-id {:keys [base-route]}]]
      (let [base-route     (uri/valid-path base-route)
            extended-route (str            base-route "/:item-id/edit")]
           [:x.router/add-route! (routes.helpers/route-id editor-id :extended)
                                 {:client-event   [:item-editor/handle-route! editor-id]
                                  :js-build       :app
                                  :restricted?    true
                                  ;:parent-route   base-route
                                  :route-template extended-route}])))

(r/reg-event-fx :item-editor/add-creator-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:base-route (string)}
  (fn [_ [_ editor-id {:keys [base-route]}]]
      (let [base-route    (uri/valid-path base-route)
            creator-route (str            base-route "/create")]
           [:x.router/add-route! (routes.helpers/route-id editor-id :creator)
                                 {:client-event   [:item-editor/handle-route! editor-id]
                                  :js-build       :app
                                  :restricted?    true
                                  :route-template creator-route}])))
