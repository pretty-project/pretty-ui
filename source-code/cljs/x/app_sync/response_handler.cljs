
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.23
; Description:
; Version: v0.8.8
; Compatibility: x4.3.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.response-handler
    (:require [mid-fruits.candy :refer [param return]]
              [mid-fruits.map   :as map]
              [mid-fruits.mixed :as mixed]
              [x.app-core.api   :as a :refer [r]]
              [x.app-db.api     :as db]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-response-target-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (item-path vector)
  [db [_ request-id]]
  (get-in db (db/path :sync/requests request-id :target-path)))

(defn- get-response-target-paths
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (map)
  [db [_ request-id]]
  (get-in db (db/path :sync/requests request-id :target-paths)))

(defn get-request-response
  ; @param (keyword) request-id
  ;
  ; @return (*)
  [db [_ request-id]]
  (get-in db (db/path :sync/responses request-id)))

(defn- store-response-to-target?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [target-path (r get-response-target-path db request-id)]
       (some? target-path)))

(defn- distribute-response-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [target-paths (r get-response-target-paths db request-id)]
       (map/nonempty? target-paths)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-request-response!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (*) server-response
  ;
  ; @return (map)
  [db [event-id request-id server-response]]
  (let [target-path  (r get-response-target-path  db request-id)
        target-paths (r get-response-target-paths db request-id)]
       (cond-> db :store-original-response
                  (db/set-item! [event-id (db/path :sync/responses request-id) server-response])
                  ; Distribute server-response items ...
                  (and (r distribute-response-items? db request-id)
                       (map/nonempty? server-response))
                  (db/distribute-items! [event-id server-response target-paths])
                  ; Store server-response
                  (r store-response-to-target? db request-id)
                  (db/set-item! [event-id target-path server-response])
                  :update-response-history! ; DEBUG
                  (db/update-data-history! [event-id :sync/responses request-id]))))



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
            data-url (mixed/to-data-url server-response-body)]
          ;[:tools/save-file! ...]
           [:ui/blow-bubble! {:color :warning :content :service-not-available}])))

(a/reg-event-fx
  :sync/handle-request-response!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; WARNING!
  ; A tesztek során ha az [:sync/store-request-response! ...] esemény különálló
  ; Re-Frame adatbázis-eseményként lett regisztrálva és az [:sync/handle-request-response! ...]
  ; esemény megtörténése után történik meg, akkor véletlenszerűen előfordultak olyan esetek, amelyekben
  ; az :on-success esemény hamarabb megtörtént, mint a [:sync/store-request-response! ...] esemény!
  ;
  ; @param (keyword) request-id
  ; @param (string) server-response-body
  ;  "{...}"
  (fn [{:keys [db]} [_ request-id server-response-body]]
      (let [response-action (get-in db (db/path :sync/requests request-id :response-action))]
           (case response-action
                 :save [:sync/save-request-response! request-id server-response-body]
                 ; Az :on-failure, :on-success és :on-responsed események megtörténése előtt
                 ; szükséges eltárolni a szerver válaszát!
                 :store {:db (r store-request-response! db request-id server-response-body)}))))
