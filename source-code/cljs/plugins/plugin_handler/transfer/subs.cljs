
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.transfer.subs)



;; -- Transfer item subscriptions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-transfer-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ plugin-id item-key]]
  (get-in db [:plugins :plugin-handler/transfer-items plugin-id item-key]))
