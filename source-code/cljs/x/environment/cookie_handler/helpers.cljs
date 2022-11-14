
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.cookie-handler.helpers
    (:require [hiccup.api                          :as hiccup]
              [mid-fruits.map                      :as map]
              [mid-fruits.string                   :as string]
              [x.environment.cookie-handler.config :as cookie-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cookie-type->cookie-name-prefix
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-type
  ;
  ; @return (string)
  [cookie-type]
  (get cookie-handler.config/COOKIE-NAME-PREFIXES cookie-type))

(defn cookie-name-prefix->cookie-type
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) cookie-name-prefix
  ;
  ; @return (keyword)
  [cookie-name-prefix]
  (get cookie-handler.config/COOKIE-TYPES cookie-name-prefix))

(defn cookie-id->cookie-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  ;  {:cookie-type (keyword)}
  ;
  ; @example
  ;  (cookie-id->cookie-name :my-cookie {:cookie-type :user-experience})
  ;  =>
  ;  "xue-my-cookie"
  ;
  ; @return (string)
  [cookie-id {:keys [cookie-type]}]
  (let [cookie-name-prefix (cookie-type->cookie-name-prefix cookie-type)
        string-cookie-id   (hiccup/value cookie-id)]
       (str cookie-name-prefix "-" string-cookie-id)))

(defn cookie-name->cookie-name-prefix
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) cookie-name
  ;
  ; @example
  ;  (cookie-name->cookie-name-prefix "xn-my-cookie")
  ;  =>
  ;  "xn"
  ;
  ; @return (string)
  [cookie-name]
  (string/before-first-occurence cookie-name "-" {:return? false}))

(defn cookie-name->analytics-cookie?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) cookie-name
  ;
  ; @example
  ;  (cookie-name->system-cookie? "xa-my-cookie")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [cookie-name]
  (let [cookie-name-prefix (cookie-name->cookie-name-prefix cookie-name)
        analytics-cookie-name-prefix (get cookie-handler.config/COOKIE-NAME-PREFIXES :analytics)]
       (= cookie-name-prefix analytics-cookie-name-prefix)))

(defn cookie-name->necessary-cookie?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) cookie-name
  ;
  ; @example
  ;  (cookie-name->system-cookie? "xn-my-cookie")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [cookie-name]
  (let [cookie-name-prefix (cookie-name->cookie-name-prefix cookie-name)
        necessary-cookie-name-prefix (get cookie-handler.config/COOKIE-NAME-PREFIXES :necessary)]
       (= cookie-name-prefix necessary-cookie-name-prefix)))

(defn cookie-name->user-experience-cookie?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) cookie-name
  ;
  ; @example
  ;  (cookie-name->system-cookie? "xue-my-cookie")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [cookie-name]
  (let [cookie-name-prefix (cookie-name->cookie-name-prefix cookie-name)
        user-experience-cookie-name-prefix (get cookie-handler.config/COOKIE-NAME-PREFIXES :user-experience)]
       (= cookie-name-prefix user-experience-cookie-name-prefix)))

(defn cookie-name->system-cookie?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) cookie-name
  ;
  ; @example
  ;  (cookie-name->system-cookie? "xn-my-cookie")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [cookie-name]
  (let [cookie-name-prefix (cookie-name->cookie-name-prefix cookie-name)]
       (map/contains-value? cookie-handler.config/COOKIE-NAME-PREFIXES cookie-name-prefix)))

(defn cookie-name->cookie-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) cookie-name
  ;
  ; @example
  ;  (cookie-name->cookie-id "xn-my-cookie")
  ;  =>
  ;  :my-cookie
  ;
  ; @return (keyword)
  [cookie-name]
  (if (cookie-name->system-cookie? cookie-name)
      (keyword (string/after-first-occurence cookie-name "-"))
      (keyword cookie-name)))

(defn cookie-name-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) cookie-name
  ;
  ; @return (boolean)
  [cookie-name]
  (.isValidName goog.net.cookies cookie-name))

(defn cookie-body-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) cookie-body
  ;
  ; @return (boolean)
  [cookie-body]
  (let [string-cookie-body (str cookie-body)]
       (.isValidValue goog.net.cookies string-cookie-body)))
