
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.transfer.subs
    (:require [plugins.item-lister.transfer.subs :as plugins.item-lister.transfer.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.transfer.subs
(def get-transfer-item plugins.item-lister.transfer.subs/get-transfer-item)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-transfer-item__
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ extension-id _ item-key]]
  (get-in db [:plugins :item-lister/transfer-items extension-id item-key]))
