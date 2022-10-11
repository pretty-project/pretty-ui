
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.cookie-handler.events
    (:require [mid-fruits.candy                         :refer [return]]
              [mid-fruits.map                           :refer [dissoc-in]]
              [mid-fruits.reader                        :as reader]
              [re-frame.api                             :as r :refer [r]]
              [x.app-environment.cookie-handler.helpers :as cookie-handler.helpers]))



;; ----------------------------------------------------------------------------
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
  (assoc-in db [:environment :cookie-handler/stored-cookies cookie-id] value))

(defn remove-cookie-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ;
  ; return (map)
  [db [_ cookie-id]]
  (dissoc-in db [:environment :cookie-handler/stored-cookies cookie-id]))

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
  (dissoc-in db [:environment :cookie-handler/stored-cookies]))



;; -- Coeffect events ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inject-cookie-browser-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; return (map)
  [cofx _]
  (let [enabled-by-browser? (.isEnabled goog.net.cookies)]
       (assoc-in cofx [:db :environment :cookie-handler/meta-items :enabled-by-browser?] enabled-by-browser?)))

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
  (assoc-in cofx [:environment :cookie-handler/stored-cookies cookie-id] value))

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
                  (if (cookie-handler.helpers/cookie-name->system-cookie? cookie-name)
                      (r inject-system-cookie! cofx cookie-name)
                      (return                  cofx)))]
              (reduce f cofx cookie-names))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :environment/cookie-set cookie-set)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :environment/cookie-removed cookie-removed)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :environment/cookies-removed cookies-removed)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-cofx :environment/inject-cookie-browser-settings! inject-cookie-browser-settings!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-cofx :environment/inject-system-cookies! inject-system-cookies!)
