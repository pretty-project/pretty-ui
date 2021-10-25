
(ns plugins.item-lister.engine
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-db.api   :as db]
              [x.app-sync.api :as sync]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn lister-props-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (item-path vector)
  [lister-id]
  (db/path ::item-listers lister-id))

(defn lister-prop-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (item-path vector)
  [lister-id prop-id]
  (db/path ::item-listers lister-id prop-id))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-lister-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (keyword) prop-id
  ;
  ; @return (*)
  [db [_ lister-id prop-id]]
  (get-in db (db/path ::item-listers lister-id prop-id)))

(defn get-value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (item-path vector)
  [db [_ lister-id]]
  (r get-lister-prop db lister-id :value-path))

(defn get-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (vector)
  [db [_ lister-id]]
  (if-let [value-path (r get-value-path db lister-id)]
          (get-in db value-path)))

(defn listening-to-request?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (let [request-id (r get-lister-prop db lister-id :request-id)]
       (r sync/listening-to-request? db request-id)))
