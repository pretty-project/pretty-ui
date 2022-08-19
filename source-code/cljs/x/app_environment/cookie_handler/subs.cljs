

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.cookie-handler.subs
    (:require [mid-fruits.map                           :as map]
              [x.app-core.api                           :as a :refer [r]]
              [x.app-db.api                             :as db]
              [x.app-environment.cookie-handler.helpers :as cookie-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-stored-cookies
  ; @usage
  ;  (r environment/get-stored-cookies db)
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path :environment/cookie-handler)))

(defn any-cookies-stored?
  ; @usage
  ;  (r environment/any-cookies-stored? db)
  ;
  ; @return (boolean)
  [db _]
  (let [stored-cookies (r get-stored-cookies db)]
       (map/nonempty? stored-cookies)))

(defn get-cookie-value
  ; @param (keyword) cookie-id
  ;
  ; @usage
  ;  (r environment/get-cookie-value db :my-cookie)
  ;
  ; @return (*)
  [db [_ cookie-id]]
  (get-in db (db/path :environment/cookie-handler cookie-id)))

(defn cookies-enabled-by-browser?
  ; @usage
  ;  (r environment/cookies-enabled-by-browser? db)
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db (db/meta-item-path :environment/cookie-handler :enabled-by-browser?))))

(defn analytics-cookies-enabled?
  ; @usage
  ;  (r environment/analytics-cookies-enabled? db)
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db [:environment :cookie-handler/meta-items :analytics-cookies-enabled?])))

(defn necessary-cookies-enabled?
  ; @usage
  ;  (r environment/necessary-cookies-enabled? db)
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db [:environment :cookie-handler/meta-items :necessary-cookies-enabled?])))

(defn user-experience-cookies-enabled?
  ; @usage
  ;  (r environment/user-experience-cookies-enabled? db)
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db [:environment :cookie-handler/meta-items :user-experience-cookies-enabled?])))

(defn get-cookie-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:analytics-cookies-enabled? (boolean)
  ;   :necessary-cookies-enabled? (boolean)
  ;   :user-experience-cookies-enabled? (boolean)}
  [db _]
  {:analytics-cookies-enabled?       (r analytics-cookies-enabled?       db)
   :necessary-cookies-enabled?       (r necessary-cookies-enabled?       db)
   :user-experience-cookies-enabled? (r user-experience-cookies-enabled? db)})

(defn set-cookie?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  ;
  ; @return (boolean)
  [db [_ cookie-id {:keys [cookie-type]}]]
  (or (and (= cookie-type :analytics)       (r analytics-cookies-enabled?       db))
      (and (= cookie-type :necessary)       (r necessary-cookies-enabled?       db))
      (and (= cookie-type :user-experience) (r user-experience-cookies-enabled? db))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:environment/any-cookies-stored?]
(a/reg-sub :environment/any-cookies-stored? any-cookies-stored?)
