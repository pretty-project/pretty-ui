
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.login-box.subs
    (:require [mid-fruits.string :as string]
              [x.app-core.api    :as a :refer [r]]
              [x.app-sync.api    :as sync]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn login-fields-unfilled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (not (and (string/nonempty? (get-in db [:views :login-box/data-items :email-address]))
            (string/nonempty? (get-in db [:views :login-box/data-items :password])))))

(defn login-button-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (or (r login-fields-unfilled?     db)
      (r sync/listening-to-request? db :user/authenticate!)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :views.login-box/login-button-disabled? login-button-disabled?)
