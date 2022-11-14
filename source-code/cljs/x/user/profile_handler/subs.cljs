
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.profile-handler.subs
    (:require [re-frame.api                  :as r :refer [r]]
              [x.user.profile-handler.config :as profile-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-user-profile
  ; @usage
  ;  (r get-user-profile db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:x.user :profile-handler/user-profile]))

(defn get-user-profile-item
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r get-user-profile-item db :email-address)
  ;
  ; @return (map)
  [db [_ item-key]]
  (get-in db [:x.user :profile-handler/user-profile item-key]))

(defn get-user-email-address
  ; @usage
  ;  (r get-user-email-address db)
  ;
  ; @return (string)
  [db _]
  (r get-user-profile-item db :email-address))

(defn get-user-first-name
  ; @usage
  ;  (r get-user-first-name db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:x.user :profile-handler/user-profile :first-name]))

(defn get-user-last-name
  ; @usage
  ;  (r get-user-last-name db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:x.user :profile-handler/user-profile :last-name]))

(defn get-user-locale
  ; @usage
  ;  (r get-user-locale db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:x.user :profile-handler/user-profile :locale]))

(defn get-user-phone-number
  ; @usage
  ;  (r get-user-phone-number db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:x.user :profile-handler/user-profile :phone-number]))

(defn get-user-profile-picture
  ; @usage
  ;  (r get-user-profile-picture db)
  ;
  ; @return (string)
  [db _]
  (or nil profile-handler.config/DEFAULT-PROFILE-PICTURE-URL))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.user/get-user-profile]
(r/reg-sub :x.user/get-user-profile get-user-profile)

; @usage
;  [:x.user/get-user-profile-item :email-address]
(r/reg-sub :x.user/get-user-profile-item get-user-profile-item)

; @usage
;  [:x.user/get-user-first-name]
(r/reg-sub :x.user/get-user-first-name get-user-first-name)

; @usage
;  [:x.user/get-user-last-name]
(r/reg-sub :x.user/get-user-last-name get-user-last-name)

; @usage
;  [:x.user/get-user-locale]
(r/reg-sub :x.user/get-user-locale get-user-locale)

; @usage
;  [:x.user/get-user-phone-number]
(r/reg-sub :x.user/get-user-phone-number get-user-phone-number)

; @usage
;  [:x.user/get-user-profile-picture]
(r/reg-sub :x.user/get-user-profile-picture get-user-profile-picture)
