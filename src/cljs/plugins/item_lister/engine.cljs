(ns plugins.item-lister.engine
  (:require
    [x.app-core.api :as a :refer [r]]))

(defn get-sort-by [db [_ lister-id]]
  (get-in db
    (db/path ::item-listers lister-id :sort-by)))