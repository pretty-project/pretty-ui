
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.transfer.subs)



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
  [db [_ extension-id _ item-key]]
  (get-in db [:plugins :item-lister/transfer-items extension-id item-key]))
