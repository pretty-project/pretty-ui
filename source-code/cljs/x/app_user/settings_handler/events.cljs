
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.settings-handler.events
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-user-settings-item!
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @usage
  ;  (r user/set-user-settings-item! db :my-settings-item "My value")
  ;
  ; @return (map)
  [db [_ item-key item-value]]
  (assoc-in db [:user :settings-handler/data-items item-key] item-value))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:user/set-user-settings-item! :my-settings-item "My value"]
(a/reg-event-db :user/set-user-settings-item! set-user-settings-item!)
