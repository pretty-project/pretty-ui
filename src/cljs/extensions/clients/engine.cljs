
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

(defn get-downloaded-clients
  [db _]
  (get-in db [:clients :list-data]))

(defn get-downloaded-client-count
  [db _]
  (let [downloaded-clients (r get-downloaded-clients db)]
       (count downloaded-clients)))

(defn all-documents-downloaded?
  [db _]
  (let [client-count            (get-in db [:clients :list-meta :document-count])
        downloaded-client-count (r get-downloaded-client-count db)]))

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
      (let [client-props (get-in db [:clients :form-data])]
           [:x.app-sync/send-query!
            :clients/synchronize-client-form!
            {:on-success [:x.app-router/go-to! "/clients"]
             :on-failure [:x.app-ui/blow-bubble! ::failure-notification {:content :saving-error :color :warning}]
             :query      [`(clients/save-client! ~client-props)]}])))

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

(a/reg-event-fx
  :clients/receive-clients!
  (fn [{:keys [db]} [_ server-response]]
      (let [documents      (get-in server-response [:clients/get-clients :documents])
            document-count (get-in server-response [:clients/get-clients :document-count])]
           {:db       (-> db (update-in [:clients :list-data] vector/concat-items documents)
                             ; Szükséges frissíteni a keresési feltételeknek megfelelő
                             ; dokumentumok számát, mert változhat
                             (assoc-in  [:clients :list-meta :document-count] document-count))
            :dispatch-if [(r all-documents-downloaded? db)
                          [:x.app-components/reload-infinite-loader! :clients]]})))

(a/reg-event-fx
  :clients/request-clients!
  (fn [{:keys [db]} _]
      [:x.app-sync/send-query!
       :clients/synchronize-client-list!
        ; A letöltött dokumentumok on-success helyett on-stalled időpontban kerülnek tárolásra
        ; a Re-Frame adatbázisba, így elkerülhető, hogy a request idle-timeout ideje alatt
        ; az újonnan letöltött dokumentumok már kirenderelésre kerüljenek, amíg a letöltést jelző
        ; felirat még megjelenik a lista végén.
       {:on-stalled [:clients/receive-clients!]
        :query      [`(:clients/get-clients
                        {:skip      ~(r get-downloaded-client-count db)
                         :max-count 10
                         :search-pattern [[:client/full-name ""] [:client/email-address ""]]
                         :sort-pattern   [[:client/first-name 1] [:client/last-name 1]]})]}]))


;; ----------------------------------------------------------------------------
;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------
