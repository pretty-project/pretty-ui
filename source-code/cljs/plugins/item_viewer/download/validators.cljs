
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.download.validators
    (:require [plugins.item-viewer.download.subs :as download.subs]
              [x.app-core.api                    :refer [r]]
              [x.app-db.api                      :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ viewer-id server-response]]
  (let [resolver-id (r download.subs/get-resolver-id db viewer-id :get-item)
        document    (get server-response resolver-id)]
       (db/document->document-namespaced? document)))
