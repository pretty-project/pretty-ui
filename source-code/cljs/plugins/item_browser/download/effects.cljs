
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.effects
    (:require [plugins.item-browser.core.subs           :as core.subs]
              [plugins.item-browser.download.queries    :as download.queries]
              [plugins.item-browser.download.validators :as download.validators]
              [x.app-core.api                           :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/reload-items!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-browser/reload-items! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:dispatch-n [[:item-lister/reload-items!  extension-id item-namespace]
                    [:item-browser/request-item! extension-id item-namespace]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [request-id   (r core.subs/get-request-id                         db extension-id item-namespace)
            query        (r download.queries/get-request-item-query          db extension-id item-namespace)
            validator-f #(r download.validators/request-item-response-valid? db extension-id item-namespace %)]
           [:sync/send-query! request-id
                              {:on-failure [:item-browser/set-error-mode! extension-id item-namespace]
                               :on-success [:item-browser/receive-item!   extension-id item-namespace]
                               :query query :validator-f validator-f}])))

(a/reg-event-fx
  :item-browser/receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]))
;      (let [db (r events/receive-item! db extension-id item-namespace server-response)]
;           (if-let [current-item-label (r subs/get-current-item-label db extension-id item-namespace)]
;                   {:db db :dispatch-if [(r subs/set-title? db extension-id item-namespace)
;                                         [:ui/set-title! current-item-label]]
;                   {:db db}]))
