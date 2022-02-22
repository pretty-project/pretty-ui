
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.21
; Description:
; Version: 2.0.8
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.cookie-handler.events
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :refer [dissoc-in]]
              [mid-fruits.reader :as reader]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-environment.cookie-handler.engine :as engine]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-cookie-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  ;  {:value (*)}
  ;
  ; return (map)
  [db [_ cookie-id {:keys [value]}]]
  (assoc-in db (db/path :environment/cookie-handler cookie-id) value))

(defn remove-cookie-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ;
  ; return (map)
  [db [_ cookie-id]]
  (dissoc-in db (db/path :environment/cookie-handler cookie-id)))

(defn cookie-set
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  [db [_ cookie-id cookie-props]]
  (r store-cookie-value! db cookie-id cookie-props))

(defn cookie-removed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  [db [_ cookie-id]]
  (r remove-cookie-value! db cookie-id))

(defn cookies-removed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (r db/empty-partition! db :environment/cookie-handler))



;; -- Coeffect events ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inject-cookie-browser-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; return (map)
  [cofx _]
  (let [enabled-by-browser? (.isEnabled goog.net.cookies)]
       (assoc-in cofx (db/meta-item-cofx-path :environment/cookie-handler :enabled-by-browser?)
                      (param enabled-by-browser?))))

(defn inject-system-cookie-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) cookie-name
  ; @param (map) cookie-body
  ;  {:cookie-id (keyword)
  ;   :value (*)}
  ;
  ; @return (map)
  [cofx [_ cookie-name {:keys [cookie-id value]}]]
  (assoc-in cofx (db/cofx-path :environment/cookie-handler cookie-id)
                 (param value)))

(defn inject-system-cookie!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) cookie-name
  ;
  ; @return (map)
  [cofx [_ cookie-name]]
  (let [raw-cookie-body (.get goog.net.cookies cookie-name)
        cookie-body     (reader/string->mixed raw-cookie-body)]
       (r inject-system-cookie-value! cofx cookie-name cookie-body)))

(defn inject-system-cookies!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [cofx _]
  (let [cookie-names (-> goog.net.cookies .getKeys vec)]
       (letfn [(f [cofx cookie-name]
                  (if (engine/cookie-name->system-cookie? cookie-name)
                      (r inject-system-cookie! cofx cookie-name)
                      (return                  cofx)))]
              (reduce f cofx cookie-names))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :environment/cookie-set cookie-set)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :environment/cookie-removed cookie-removed)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :environment/cookies-removed cookies-removed)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-cofx :environment/inject-cookie-browser-settings! inject-cookie-browser-settings!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-cofx :environment/inject-system-cookies! inject-system-cookies!)
