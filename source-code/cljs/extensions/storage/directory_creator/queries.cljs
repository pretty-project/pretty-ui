
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.directory-creator.queries
    (:require [x.app-core.api :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-create-directory-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) creator-id
  ; @param (string) directory-name
  ;
  ; @return (vector)
  [db [_ creator-id directory-name]]
  (let [destination-id (get-in db [:storage :directory-creator/meta-items :destination-id])]
       [`(storage.directory-creator/create-directory! ~{:destination-id destination-id :alias directory-name})]))
