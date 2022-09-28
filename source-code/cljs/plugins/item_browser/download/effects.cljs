
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.effects
    (:require [plugins.item-browser.body.subs           :as body.subs]
              [plugins.item-browser.core.subs           :as core.subs]
              [plugins.item-browser.download.events     :as download.events]
              [plugins.item-browser.download.queries    :as download.queries]
              [plugins.item-browser.download.validators :as download.validators]
              [x.app-core.api                           :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/reload-items!
  ; @param (keyword) browser-id
  ; @param (map)(opt) reload-props
  ;  {:on-reload (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [:item-browser/reload-items! :my-browser]
  ;
  ; @usage
  ;  [:item-browser/reload-items! :my-browser {...}]
  (fn [{:keys [db]} [_ browser-id reload-props]]
      {:dispatch-n [[:item-lister/reload-items!  browser-id reload-props]
                    [:item-browser/request-item! browser-id]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/request-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      [:item-lister/request-items! browser-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      (let [query        (r download.queries/get-request-item-query          db browser-id)
            validator-f #(r download.validators/request-item-response-valid? db browser-id %)]
           [:pathom/send-query! (r core.subs/get-request-id db browser-id)
                                {:on-failure [:item-browser/set-error-mode! browser-id]
                                 :on-success [:item-browser/receive-item!   browser-id]
                                 :query query :validator-f validator-f}])))

(a/reg-event-fx
  :item-browser/receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id server-response]]
      ; Ha az [:item-browser/receive-item! ...] esemény megtörténésekor a body komponens már
      ; nincs a React-fába csatolva, akkor az esemény nem végez műveletet.
      (if (r body.subs/body-did-mount? db browser-id)
          {:db       (r download.events/receive-item! db browser-id server-response)
           :dispatch [:item-browser/item-received browser-id]})))

(a/reg-event-fx
  :item-browser/item-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      (if-let [auto-title (r core.subs/get-auto-title db browser-id)]
              [:ui/set-window-title! auto-title])))
