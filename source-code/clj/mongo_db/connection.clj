
(ns mongo-db.connection
    (:import [com.mongodb MongoOptions ServerAddress]
             org.bson.types.BSONTimestamp)
    (:require [mid-fruits.candy  :refer [param return]]
              [monger.core       :as mcr]
              [x.server-core.api :as a]))



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (?)
(def DB (atom nil))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn connected?
  ; @usage
  ;  (mongo-db/connected?)
  ;
  ; @return (boolean)
  []
  (some? @DB))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- build-connection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) database-name
  ; @param (string) database-host
  ; @param (integer) database-port
  [[database-name database-host database-port]]
  (let [^MongoOptions  mongo-options  (mcr/mongo-options {:threads-allowed-to-block-for-connection-multiplier 300})
        ^ServerAddress server-address (mcr/server-address database-host  database-port)
                       connection     (mcr/connect        server-address mongo-options)
                       database       (mcr/get-db         connection     database-name)]
       (reset! DB database)))

(a/reg-fx :mongo-db/build-connection! build-connection!)
