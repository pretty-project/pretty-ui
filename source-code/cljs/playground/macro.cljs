
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.playground.macro
    (:require [re-frame.core :as re-frame.core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-user-data
  [db [_ user-id]]
  (get-in db [:users user-id]))

(re-frame.core/reg-sub :get-user-data get-user-data)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defr user-data
  [:get-user-id {:keys [user-name email-address] :as user-data}]
  [user-id]
  [:<> [:div               "#" user-id]
       [:div     "User-name: " user-name]
       [:div "Email-address: " email-address]])

(defn view
  []
  [user-data "my-user"])
