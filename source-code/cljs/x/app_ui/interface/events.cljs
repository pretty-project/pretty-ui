
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.interface.events)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-interface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) interface
  ;
  ; @return (map)
  [db [_ interface]]
  (assoc-in db [:ui :interface/meta-items :interface] interface))
