
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.account-handler.events
    (:require [mid-fruits.map  :refer [dissoc-in]]
              [mid-fruits.time :as time]
              [x.app-core.api  :as a :refer [r]]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- reg-login-attempt!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (assoc-in db [:user :account-handler/meta-items :login-attempted-at]
               (time/elapsed)))

(defn- clear-login-attempt!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (dissoc-in db [:user :account-handler/meta-items :login-attempted-at]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :user/reg-login-attempt! reg-login-attempt!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :user/clear-login-attempt! clear-login-attempt!)
