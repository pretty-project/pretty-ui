
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.transfer.subs)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-transfer-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ selector-id item-key]]
  (get-in db [:plugins :view-selector/transfer-items selector-id item-key]))
