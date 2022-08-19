
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.settings-handler.effects
    (:require [x.app-core.api                     :as a :refer [r]]
              [x.app-user.settings-handler.events :as settings-handler.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :user/upload-user-settings-item!
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @usage
  ;  [:user/upload-user-settings-item! :my-settings-item "My value"]
  (fn [{:keys [db]} [_ item-key item-value]]
      {:db       (r settings-handler.events/set-user-settings-item! db item-key item-value)
       :dispatch [:sync/send-request! :user/upload-user-settings-item!
                                      {:method :post
                                       :params {:item-key item-key :item-value item-value}
                                       :uri    "/user/upload-user-settings-item"}]}))
