
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.transfer.subs)



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
  (get-in db [:plugins :view-selector/transfer-items extension-id item-key]))
