
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.transfer.subs
    (:require [plugins.item-editor.transfer.helpers :as transfer.helpers]
              [x.app-core.api                       :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-transfer-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ extension-id item-namespace item-key]]
  (let [transfer-id (transfer.helpers/transfer-id extension-id item-namespace)]
       (r a/get-transfer-item db transfer-id item-key)))
