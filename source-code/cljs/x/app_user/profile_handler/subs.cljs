
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.profile-handler.subs
    (:require [mid-fruits.string                 :as string]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-user.profile-handler.config :as profile-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-user-profile
  ; @usage
  ;  (r user/get-user-profile db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:user :profile-handler/data-items]))

(defn get-user-profile-item
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r user/get-user-profile-item db :email-address)
  ;
  ; @return (map)
  [db [_ item-key]]
  (get-in db [:user :profile-handler/data-items item-key]))

(defn get-user-email-address
  ; @usage
  ;  (r user/get-user-email-address db)
  ;
  ; @return (string)
  [db _]
  (r get-user-profile-item db :email-address))

(defn get-user-first-name
  ; @usage
  ;  (r user/get-user-first-name db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:user :profile-handler/data-items :first-name]))

(defn get-user-last-name
  ; @usage
  ;  (r user/get-user-last-name db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:user :profile-handler/data-items :last-name]))

(defn get-user-name
  ; @usage
  ;  (r user/get-user-name db)
  ;
  ; @return (string)
  [db _]
  (string/trim (str (r get-user-first-name db) " "
                    (r get-user-last-name  db))))

(defn get-user-phone-number
  ; @usage
  ;  (r user/get-user-phone-number db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:user :profile-handler/data-items :phone-number]))

(defn get-user-profile-picture
  ; @usage
  ;  (r user/get-user-profile-picture db)
  ;
  ; @return (string)
  [db _]
  (or nil profile-handler.config/DEFAULT-PROFILE-PICTURE-URL))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:user/get-user-profile]
(a/reg-sub :user/get-user-profile get-user-profile)

; @usage
;  [:user/get-user-profile-item :email-address]
(a/reg-sub :user/get-user-profile-item get-user-profile-item)

; @usage
;  [:user/get-user-first-name]
(a/reg-sub :user/get-user-first-name get-user-first-name)

; @usage
;  [:user/get-user-last-name]
(a/reg-sub :user/get-user-last-name get-user-last-name)

; @usage
;  [:user/get-user-name]
(a/reg-sub :user/get-user-name get-user-name)

; @usage
;  [:user/get-user-phone-number]
(a/reg-sub :user/get-user-phone-number get-user-phone-number)

; @usage
;  [:user/get-user-profile-picture]
(a/reg-sub :user/get-user-profile-picture get-user-profile-picture)
