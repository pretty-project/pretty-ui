
(ns mongo-db.aggregation
    (:require [mid-fruits.candy    :refer [param return]]
              [monger.core         :as mcr]
              [mongo-db.adaptation :as adaptation]
              [mongo-db.engine     :as engine]
              [x.server-core.api   :as a]))



;; -- Error handling ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- command
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) options
  ;
  ; @return (DBObject)
  [options]
  (let [database (a/subscribed [:mongo-db/get-connection])]
       (try (mcr/command database options)
            (catch Exception e (println (str e "\n" {:options options}))))))



;; -- Aggregation functions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn process
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (maps in vector) pipeline
  ; @param (map)(opt) options
  ;  {:locale (string)(opt)
  ;    Default: mongo-db.engine/DEFAULT-LOCALE}
  ;
  ; @return (maps in vector)
  ([collection-name pipeline]
   (process collection-name pipeline {:locale engine/DEFAULT-LOCALE}))

  ([collection-name pipeline {:keys [locale]}]
   (if-let [db-object (command {:aggregate collection-name
                                :pipeline  pipeline
                                :collation {:locale locale :numericOrdering true}
                                :cursor    {}})]
           (adaptation/aggregation-output db-object))))
