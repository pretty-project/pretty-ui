
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.transfer.subs
    (:require [plugins.view-selector.transfer.helpers :as transfer.helpers]
              [x.app-core.api                         :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-transfer-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ extension-id item-key]]
  (let [transfer-id (transfer.helpers/transfer-id extension-id)]
       (r a/get-transfer-item db transfer-id item-key)))
