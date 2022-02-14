
(ns mongo-db.connection
    (:import [com.mongodb MongoOptions ServerAddress])
             ;org.bson.types.BSONTimestamp
    (:require [monger.core       :as mcr]
              [x.server-core.api :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-connection
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (com.mongodb.DB object)
  [db _]
  (get-in db [:mongo-db :connection]))

(a/reg-sub :mongo-db/get-connection get-connection)

(defn- connected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (some? (r get-connection db)))

(a/reg-sub :mongo-db/connected? connected?)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-connection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (com.mongodb.DB object) reference
  ;
  ; @return (map)
  [db [_ reference]]
  (assoc-in db [:mongo-db :connection] reference))

(a/reg-event-db :mongo-db/store-connection! store-connection!)



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- build-connection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) database-name
  ; @param (string) database-host
  ; @param (integer) database-port
  [database-name database-host database-port]
  (let [^MongoOptions  mongo-options  (mcr/mongo-options {:threads-allowed-to-block-for-connection-multiplier 300})
        ^ServerAddress server-address (mcr/server-address database-host  database-port)
                       connection     (mcr/connect        server-address mongo-options)
                       database       (mcr/get-db         connection     database-name)]
       (a/dispatch [:mongo-db/store-connection! database])))

(a/reg-fx_ :mongo-db/build-connection! build-connection!)
