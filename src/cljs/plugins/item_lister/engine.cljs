
(ns plugins.item-lister.engine
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-db.api   :as db]
              [x.app-sync.api :as sync]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn lister-props-path
  [lister-id]
  (db/path ::item-listers lister-id))

(defn lister-prop-path
  [lister-id prop-id]
  (db/path ::item-listers lister-id prop-id))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-lister-prop
  [db [_ lister-id prop-id]]
  (get-in db (db/path ::item-listers lister-id prop-id)))

(defn get-value-path
  [db [_ lister-id]]
  (r get-lister-prop db lister-id :value-path))

(defn get-items
  [db [_ lister-id]]
  (if-let [value-path (r get-value-path db lister-id)]
          (get-in db value-path)))

(defn get-sort-by
  [db [_ lister-id]]
  (r get-lister-prop db lister-id :sort-by))

(defn listening-to-request?
  [db [_ lister-id]]
  (let [request-id (r get-lister-prop db lister-id :request-id)]
       (r sync/listening-to-request? db request-id)))
