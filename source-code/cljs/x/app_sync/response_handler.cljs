
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.23
; Description:
; Version: v1.1.4
; Compatibility: x4.5.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.response-handler
    (:require [mid-fruits.candy :refer [param return]]
              [mid-fruits.mixed :as mixed]
              [x.app-core.api   :as a :refer [r]]
              [x.app-db.api     :as db]))



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
  (as-> db % (r db/update-data-history!  %          :sync/responses request-id) ; DEBUG
             (r db/set-item!             % (db/path :sync/responses request-id) server-response)
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
