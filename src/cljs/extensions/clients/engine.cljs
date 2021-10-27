
(ns extensions.clients.engine
    (:require [mid-fruits.keyword :as keyword]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              ; TEMP
              [mid-fruits.random  :as random]))



;; ----------------------------------------------------------------------------
;; -- Sample data -------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sample-clients
      []
      [{:client/id (random/generate-string)
        :client/client-no "051"
        :client/first-name "Debil"
        :client/last-name "Duck"
        :client/email-address "debil-duck@gmail.com"
        :client/added-at "2020-04-10T16:20:00.123Z"}
       {:client/id (random/generate-string)
        :client/client-no "112"
        :client/first-name "Addicted"
        :client/last-name "Alligator"
        :client/email-address "addicted-alligator@gmail.com"
        :client/added-at "2020-03-21T10:01:00.123Z"}
       {:client/id (random/generate-string)
        :client/client-no "941"
        :client/first-name "Monoton"
        :client/last-name "Monkey"
        :client/email-address "debil-duck@gmail.com"
        :client/added-at "2020-04-14T09:41:00.123Z"}
       {:client/id (random/generate-string)
        :client/client-no "415"
        :client/first-name "Baffling"
        :client/last-name "Badger"
        :client/email-address "baffling-badger@gmail.com"
        :client/added-at "2021-08-01T08:20:00.123Z"}
       {:client/id (random/generate-string)
        :client/client-no "515"
        :client/first-name "Paranormal"
        :client/last-name "Penguin"
        :client/email-address "paranormal-penguin@gmail.com"
        :client/added-at "2021-05-01T14:32:00.123Z"}
       {:client/id (random/generate-string)
        :client/client-no "612"
        :client/first-name "Promising"
        :client/last-name "Panda"
        :client/email-address "promising-panda@gmail.com"
        :client/added-at "2019-02-20T06:53:00.123Z"}])

;; ----------------------------------------------------------------------------
;; -- Sample data -------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-id->client-uri
  ; @param (keyword) client-id
  [client-id]
  (let [client-id (keyword/to-string client-id)]
       (str "/clients/" client-id)))

;; ----------------------------------------------------------------------------
;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  :clients/receive-save-client!
  {:dispatch-n [[:x.app-router/go-to! "/clients"]
                [:x.app-ui/blow-bubble! ::add-notification {:content "Client added."
                                                            :color :muted}]]})

(a/reg-event-fx
  :clients/request-save-client!
  (fn [{:keys [db]} [event-id client]]
    [:x.app-sync/send-query!
     :clients/request-save-client!
     {:on-success [:clients/receive-save-client!]
      :query [`(clients/add-client! ~client)]}]))

; Add client

(a/reg-event-fx
  :clients/receive-add-client!
  {:dispatch-n [[:x.app-router/go-to! "/clients"]
                [:x.app-ui/blow-bubble! ::add-notification {:content "Client added."
                                                            :color :muted}]]})

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
  (fn [{:keys [db]} [_ response-value]]
      (let [result    (:clients/get-client response-value)
            {:keys [documents count]} result]
           [:x.app-db/set-item! [:clients :form]
            {:client/id            "9b2f16b0-bb4c-46fe-95c0-a6879e4cb8de"
             :client/client-no     "051301"
             :client/first-name    "Debil"
             :client/last-name     "Duck"
             :client/email-address "debil-duck@gmail.com"
             :client/added-at      "2020-04-10T16:20:00.123Z"}])))

(a/reg-event-fx
  :clients/request-client!
  (fn [{:keys [db]} [event-id client-id]]
      [:x.app-sync/send-query!
       :clients/request-client!
       {:on-success [:clients/receive-clients!]
        :query [`(:clients/get-client {:client-id ~client-id})]}]))


; Download clients for the client-list

(a/reg-event-fx
  :clients/receive-clients!
  (fn [{:keys [db]} [_ response-value]]
      (let [result    (:clients/get-clients response-value)
            {:keys [documents count]} result]
           [:x.app-db/apply! [:clients :documents] vector/concat-items documents])))

(a/reg-event-fx
  :clients/request-clients!
  (fn [{:keys [db]} _]
      [:x.app-sync/send-query!
       :clients/request-clients!
       {:on-success [:clients/receive-clients!]
        :query [`(:clients/get-clients
                   {:skip 1
                    :max-count 3
                    :search-pattern [[:client/full-name ""] [:client/email-address ""]]
                    :sort-pattern   [[:client/first-name 1] [:client/last-name 1]]})]}]))

;; ----------------------------------------------------------------------------
;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------
