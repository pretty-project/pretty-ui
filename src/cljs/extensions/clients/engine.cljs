
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
;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (item-path vector)
(def CLIENTS-DATA-PATH [:clients :documents])

; @constant (item-path vector)
(def CLIENT-DATA-PATH  [:clients :form])

; @constant (item-path vector)
(def CLIENT-META-PATH  [:clients :form])

;; ----------------------------------------------------------------------------
;; -- Configuration -----------------------------------------------------------
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

(a/reg-event-fx
  :clients/receive-clients!
  (fn [{:keys [db]} _]
      [:x.app-db/apply! CLIENTS-DATA-PATH vector/concat-items (sample-clients)]))

(a/reg-event-fx
  :clients/request-clients!
  (fn [{:keys [db]} _]
      [:x.app-sync/send-query!
       {:on-success [:clients/receive-clients!]
        :query [`(:clients/get-clients {:skip 0
                                        :max-count 20
                                        :search-pattern [[:client/full-name :client/email-address]]
                                        :sort-pattern   [[:client/first-name 1] [:client/first-name 1]]})]}]))


;(a/reg-event-fx
;  :clients/download-clients-data!
;  (fn [{:keys [db]} _]
;
;      {:dispatch-later [; Request emulálása a UI számára
;                        {:ms    0 :dispatch [:x.app-core/set-process-activity! :clients/download-clients-data! :active]}
;                        {:ms  750 :dispatch [:x.app-core/set-process-activity! :clients/download-clients-data! :idle]}
;                        {:ms 1000 :dispatch [:x.app-core/set-process-activity! :clients/download-clients-data! :stalled]}
;                        ; Minta adatok hozzáadasa
;                        {:ms 1000 :dispatch [:x.app-db/apply! CLIENTS-DATA-PATH vector/concat-items (sample-clients)]}
;                        ; Infinite loader újratöltése
;                        {:ms 1000 :dispatch [:x.app-components/reload-infinite-loader! :clients]}]}))



;; ----------------------------------------------------------------------------
;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------
