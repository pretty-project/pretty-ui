
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.directory-creator.queries
    (:require [x.app-core.api :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-create-directory-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ creator-id directory-name]]
  (let [destination-id (get-in db [:storage :directory-creator/meta-items :destination-id])]
       [:debug `(storage.directory-creator/create-directory! ~{:destination-id destination-id :alias directory-name})]))
