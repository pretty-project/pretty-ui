
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.directory-creator.validators
    (:require [x.app-db.api :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-directory-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ creator-id server-response]]
  (let [document (get server-response (symbol "storage.directory-creator/create-directory!"))]
       (db/document->document-namespaced? document)))
