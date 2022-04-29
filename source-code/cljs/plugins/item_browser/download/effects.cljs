
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
  :item-browser/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      (let [query        (r download.queries/get-request-item-query          db browser-id)
            validator-f #(r download.validators/request-item-response-valid? db browser-id %)]
           [:sync/send-query! (r core.subs/get-request-id db browser-id)
                              {:on-failure [:item-browser/set-error-mode! browser-id]
                               :on-success [:item-browser/receive-item!   browser-id]
                               :query query :validator-f validator-f}])))

(a/reg-event-fx
  :item-browser/receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id server-response]]))
      ; - A download.events/receive-item! függvény eltárolja az aktuálisan letöltött elem adatait, ezért ...
      ;   ... a core.subs/get-current-item-label függvény lefutása előtt szükséges meghívni.
      ;
      ; - Ha az [:item-browser/receive-item! ...] esemény megtörténésekor a body komponens már nincs
      ;   a React-fába csatolva, akkor az esemény nem végez műveletet.
      ;(if (r body.subs/body-did-mount? db browser-id)
      ;    (let [db (r download.events/receive-item! db browser-id server-response)]
      ;         (if-let [auto-title? (r body.subs/get-body-prop db browser-id :auto-title?)])]))
                       ;{:db db :dispatch-n [(if-let [item-label (r core.subs/get-current-item-label db browser-id)]
                        ;                            [:ui/set-window-title! item-label])]
                       ;{:db db})))))
