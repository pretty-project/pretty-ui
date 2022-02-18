
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.23
; Description:
; Version: v1.1.8
; Compatibility: x4.6.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.response-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.mixed  :as mixed]
              [mid-fruits.reader :as reader]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-response
  ; @param (keyword) request-id
  ;
  ; @return (*)
  [db [_ request-id]]
  (get-in db (db/path :sync/responses request-id)))

(defn store-request-response?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [response-action (get-in db (db/path :sync/requests request-id :response-action))]
       (= response-action :store)))

(defn save-request-response?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [response-action (get-in db (db/path :sync/requests request-id :response-action))]
       (= response-action :save)))

(defn request-response-invalid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (string) server-response-body
  ;  "{...}"
  ;
  ; @return (boolean)
  [db [_ request-id server-response-body]]
  ; A sikeres HTTP státusz-kódtól függetlenül ha a szerver válasza a validator-f függvény szerint
  ; nem megfelelő, akkor az on-success esemény helyett az on-failure esemény fog megtörténni ...
  (boolean (if-let [validator-f (get-in db (db/path :sync/requests request-id :validator-f))]
                   (-> server-response-body reader/string->mixed validator-f not))))

(defn get-invalid-server-response
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (string) server-response-body
  ;  "{...}"
  ;
  ; @return (map)
  ;  {:failure (keyword)
  ;   :response (string)
  ;   :validator-f (function)}
  [db [_ request-id server-response-body]]
  (let [validator-f (get-in db (db/path :sync/requests request-id :validator-f))]
       {:failure     :invalid
        :response    server-response-body
        :validator-f validator-f}))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- target-request-response!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (*) server-response
  ;
  ; @return (map)
  [db [_ request-id server-response]]
  (if-let [target-path (get-in db (db/path :sync/requests request-id :target-path))]
          (r db/set-item! db target-path server-response)
          (return         db)))

(defn- store-request-response!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (*) server-response
  ;
  ; @return (map)
  [db [_ request-id server-response]]
  (as-> db % (r db/set-item!             % (db/path :sync/responses request-id) server-response)
             (r db/update-data-history!  %          :sync/responses request-id) ; DEBUG
             (r target-request-response! % request-id server-response)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :sync/save-request-response!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (string) server-response-body
  ;  "{...}"
  (fn [{:keys [db]} [_ request-id server-response-body]]
      (let [filename (get-in db (db/path :sync/requests request-id :filename))
            data-url (mixed/to-data-url server-response-body)])))
          ;[:tools/save-file! ...]
