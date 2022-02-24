
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.profile-handler.events
    (:require [x.app-core.api :as a]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-user-profile-item!
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @usage
  ;  (r set-user-profile-item! db :last-name "Roger")
  ;
  ; @return (map)
  [db [_ item-key item-value]]
  (assoc-in db [:user :profile-handler/data-items item-key] item-value))
