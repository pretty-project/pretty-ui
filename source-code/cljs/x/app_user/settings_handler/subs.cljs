
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.settings-handler.subs)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-user-settings
  ; @usage
  ;  (r user/get-user-settings db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:user :settings-handler/data-items]))

(defn get-user-settings-item
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r user/get-user-settings-item db :my-settings-item)
  ;
  ; @return (*)
  [db [_ item-key]]
  (get-in db [:user :settings-handler/data-items item-key]))
