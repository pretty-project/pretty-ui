
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.download.events)



;; -- Data-receiving events ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items plugin-id :data-received?] true))
