
(ns extensions.clients.engine
    (:require [mid-fruits.keyword :as keyword]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              ; TEMP
              [mid-fruits.random  :as random]))



;; ----------------------------------------------------------------------------
;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------


; Save client

(a/reg-event-fx
  :clients/request-save-client!
  (fn [{:keys [db]} [event-id]]
      (let [client-props (get-in db [:clients :editor-data])]
           [:x.app-sync/send-query!
            :clients/synchronize-client-form!
            {:on-stalled [:x.app-router/go-to! "/clients"]
             :on-failure [:x.app-ui/blow-bubble! ::failure-notification {:content :saving-error :color :warning}]
             :query      [`(~(symbol "clients/save-client!") ~client-props)]}])))

; Add client

(a/reg-event-fx
  :clients/receive-add-client!
  {:dispatch-n []})
              ;[:x.app-router/go-to! "/clients"]
                ;[:x.app-ui/blow-bubble! ::add-notification {:content "Client added."
                ;                                            :color :muted]})

(a/reg-event-fx
  :clients/request-add-client!
  (fn [{:keys [db]} [event-id client]]
      [:x.app-sync/send-query!
       :clients/request-add-client!
       {:on-success [:clients/receive-add-client!]
        :query [`(clients/add-client! ~client)]}]))

; Delete client

(a/reg-event-fx
  :clients/receive-delete-client!
  {:dispatch-n [[:x.app-router/go-to! "/clients"]
                [:x.app-ui/blow-bubble! ::delete-notification {:content "Client deleted.";#'undo-delete-button
                                                               :color :muted}]]})

(a/reg-event-fx
  :clients/request-delete-client!
  (fn [{:keys [db]} [event-id client-id]]
      [:x.app-sync/send-query!
       :clients/request-delete-client!
       {:on-success [:clients/receive-delete-client!]
        :query [`(clients/delete-client! {:client-id ~client-id})]}]))


; Duplicate client

(a/reg-event-fx
  :clients/receive-duplicate-client!
  [:x.app-ui/blow-bubble! ::duplicate-notification {:content "Client duplicated." ;#'edit-copy-button
                                                    :color :muted}])

(a/reg-event-fx
  :clients/request-duplicate-client!
  (fn [{:keys [db]} [event-id client-id]]
      [:x.app-sync/send-query!
       :clients/request-duplicate-client!
       {:on-success [:clients/receive-duplicate-client!]
        :query [`(clients/duplicate-client! {:client-id ~client-id})]}]))


; Download client for the form

(a/reg-event-fx
  :clients/receive-client!
  (fn [{:keys [db]} [_ entity server-response]]
      (let [client    (get server-response entity)]
           [:x.app-db/set-item! [:clients :form-data] client])))


(a/reg-event-fx
  :clients/request-client!
  (fn [{:keys [db]} [event-id client-id]]
      [:x.app-sync/send-query!
       :clients/synchronize-client-form!
       {:on-stalled [:clients/receive-client! [:client/id client-id]]
        :query      [{[:client/id client-id] [:client/last-name '*]}]}]))

; Download clients for the client-list

;; ----------------------------------------------------------------------------
;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------
