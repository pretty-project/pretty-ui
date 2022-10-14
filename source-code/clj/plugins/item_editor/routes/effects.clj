
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.routes.effects
    (:require [mid-fruits.uri                     :as uri]
              [plugins.item-editor.routes.helpers :as routes.helpers]
              [re-frame.api                       :as r]))



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
           [:router/add-route! (routes.helpers/route-id editor-id :extended)
                               {:client-event   [:item-editor/handle-route! editor-id]
                                :restricted?    true
                                ;:route-parent   base-route
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
           [:router/add-route! (routes.helpers/route-id editor-id :creator)
                               {:client-event   [:item-editor/handle-route! editor-id]
                                :restricted?    true
                                :route-template creator-route}])))
