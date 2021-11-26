
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.07
; Description:
; Version: 1.4.6
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.cookie-handler
    (:require [goog.net.cookies]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map :refer [dissoc-in]]
              [mid-fruits.reader  :as reader]
              [mid-fruits.string  :as string]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (nil)
(def COOKIE-DOMAIN nil)

; @constant (string)
(def COOKIE-PATH "/")

; @constant (map)
(def COOKIE-NAME-PREFIXES {:analytics       "xa"
                           :necessary       "xn"
                           :user-experience "xue"})

; @constant (map)
(def COOKIE-TYPES (map/swap COOKIE-NAME-PREFIXES))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cookie-type->cookie-name-prefix
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-type
  ;
  ; @return (string)
  [cookie-type]
  (get COOKIE-NAME-PREFIXES cookie-type))

(defn- cookie-name-prefix->cookie-type
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) cookie-name-prefix
  ;
  ; @return (keyword)
  [cookie-name-prefix]
  (get COOKIE-TYPES cookie-name-prefix))

(defn- cookie-id->cookie-name
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
        string-cookie-id   (keyword/to-dom-value cookie-id)]
       (str cookie-name-prefix "-" string-cookie-id)))

(defn- cookie-name->cookie-name-prefix
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
  (string/before-first-occurence cookie-name "-"))

(defn- cookie-name->analytics-cookie?
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
        analytics-cookie-name-prefix (get COOKIE-NAME-PREFIXES :analytics)]
       (= cookie-name-prefix analytics-cookie-name-prefix)))

(defn- cookie-name->necessary-cookie?
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
        necessary-cookie-name-prefix (get COOKIE-NAME-PREFIXES :necessary)]
       (= cookie-name-prefix necessary-cookie-name-prefix)))

(defn- cookie-name->user-experience-cookie?
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
        user-experience-cookie-name-prefix (get COOKIE-NAME-PREFIXES :user-experience)]
       (= cookie-name-prefix user-experience-cookie-name-prefix)))

(defn- cookie-name->system-cookie?
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
       (map/contains-value? COOKIE-NAME-PREFIXES cookie-name-prefix)))

(defn- cookie-name->cookie-id
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
      (keyword (param cookie-name))))

(defn cookie-setting-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) setting-id
  ;
  ; @return (vector)
  [setting-id]
  (db/path ::cookies :cookie-settings setting-id))

(defn- cookie-name-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) cookie-name
  ;
  ; @return (boolean)
  [cookie-name]
  (.isValidName goog.net.cookies cookie-name))

(defn- cookie-body-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) cookie-body
  ;
  ; @return (boolean)
  [cookie-body]
  (let [string-cookie-body (str cookie-body)]
       (.isValidValue goog.net.cookies string-cookie-body)))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cookie-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  ;  {:cookie-type (keyword)(opt)
  ;   :max-age (integer)(opt)}
  ;
  ; @return (map)
  ;  {:cookie-type (keyword)
  ;   :domain (nil)
  ;   :max-age (integer)
  ;   :path (string)
  ;   :secure (boolean)}
  [cookie-props]
  (merge {:cookie-type :user-experience
          :max-age     -1}
         (param cookie-props)
         {:domain    COOKIE-DOMAIN
          :path      COOKIE-PATH
          :secure    true
          :same-site "strict"}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-stored-cookies
  ; @usage
  ;  (r environment/get-stored-cookies db)
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path ::cookies)))

(defn any-cookies-stored?
  ; @usage
  ;  (r environment/any-cookies-stored? db)
  ;
  ; @return (boolean)
  [db _]
  (let [stored-cookies (r get-stored-cookies db)]
       (map/nonempty? stored-cookies)))

; @usage
;  [:environment/any-cookies-stored?]
(a/reg-sub :environment/any-cookies-stored? any-cookies-stored?)

(defn get-cookie-value
  ; @param (keyword) cookie-id
  ;
  ; @usage
  ;  (r environment/get-cookie-value db :my-cookie)
  ;
  ; @return (*)
  [db [_ cookie-id]]
  (get-in db (db/path ::cookies cookie-id)))

(defn cookies-enabled-by-browser?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db (db/meta-item-path ::cookies :enabled-by-browser?))))

(defn analytics-cookies-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db (cookie-setting-path :analytics-cookies-enabled?))))

(defn necessary-cookies-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db (cookie-setting-path :necessary-cookies-enabled?))))

(defn user-experience-cookies-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db (cookie-setting-path :user-experience-cookies-enabled?))))

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

(defn- set-cookie?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  ;
  ; @return (boolean)
  [db [_ cookie-id {:keys [cookie-type]}]]
  (boolean (or (and (= cookie-type :analytics)
                    (r analytics-cookies-enabled? db))
               (and (= cookie-type :necessary)
                    (r necessary-cookies-enabled? db))
               (and (= cookie-type :user-experience)
                    (r user-experience-cookies-enabled? db)))))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-cookie-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  ;  {:value (*)}
  ;
  ; return (map)
  [db [_ cookie-id {:keys [value]}]]
  (assoc-in db (db/path ::cookies cookie-id)
               (param value)))

(defn- remove-cookie-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ;
  ; return (map)
  [db [_ cookie-id]]
  (dissoc-in db (db/path ::cookies cookie-id)))



;; -- Coeffect events ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- inject-cookie-browser-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; return (map)
  [cofx _]
  (let [enabled-by-browser? (.isEnabled goog.net.cookies)]
       (assoc-in cofx (db/meta-item-cofx-path ::cookies :enabled-by-browser?)
                      (param enabled-by-browser?))))

(a/reg-cofx :environment/inject-cookie-browser-settings! inject-cookie-browser-settings!)

(defn- inject-system-cookie-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) cookie-name
  ; @param (map) cookie-body
  ;  {:cookie-id (keyword)
  ;   :value (*)}
  ;
  ; @return (map)
  [cofx [_ cookie-name {:keys [cookie-id value]}]]
  (assoc-in cofx (db/cofx-path ::cookies cookie-id)
                 (param value)))

(defn- inject-system-cookie!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) cookie-name
  ;
  ; @return (map)
  [cofx [_ cookie-name]]
  (let [raw-cookie-body (.get goog.net.cookies cookie-name)
        cookie-body     (reader/string->mixed raw-cookie-body)]
       (r inject-system-cookie-value! cofx cookie-name cookie-body)))

(defn- inject-system-cookies!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [cofx _]
  (let [cookie-names (vec (.getKeys goog.net.cookies))]
       (reduce (fn [cofx cookie-name]
                   (if (cookie-name->system-cookie? cookie-name)
                       (r inject-system-cookie! cofx cookie-name)
                       (return cofx)))
               (param cofx)
               (param cookie-names))))

(a/reg-cofx :environment/inject-system-cookies! inject-system-cookies!)



;; -- Effect-events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/set-cookie!
  ; @param (keyword)(opt) cookie-id
  ; @param (map) cookie-props
  ;  {:cookie-type (keyword)(opt)
  ;    :analytics, :necessary, :user-experience
  ;    Default: :user-experience
  ;   :max-age (sec)(opt)
  ;    Use -1 to set a session cookie.
  ;    Default: -1
  ;   :value (*)}
  ;
  ; @usage
  ;  [:environment/set-cookie! {...}]
  ;
  ; @usage
  ;  [:environment/set-cookie! :my-cookie {...}]
  (fn [{:keys [db]} event-vector]
      (let [cookie-id    (a/event-vector->second-id   event-vector)
            cookie-props (a/event-vector->first-props event-vector)
            cookie-props (a/prot cookie-props cookie-props-prototype)]
           {:dispatch-if [(r set-cookie? db cookie-id cookie-props)
                          [:environment/store-browser-cookie! cookie-id cookie-props]]})))

(a/reg-event-fx
  :environment/remove-cookie!
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  ;  {:cookie-type (keyword)(opt)
  ;    :analytics, :necessary, :user-experience
  ;    Default: :user-experience}
  ;
  ; @usage
  ;  [:environment/remove-cookie! :my-cookie]
  ;
  ; @usage
  ;  [:environment/remove-cookie! :my-cookie {:cookie-type :necessary}]
  (fn [{:keys [db]} [_ cookie-id cookie-props]]
      (let [cookie-props (a/prot cookie-props cookie-props-prototype)]
           [:environment/remove-browser-cookie! cookie-id cookie-props])))

(a/reg-event-fx
  :environment/remove-cookies!
  ; @usage
  ;  [:environment/remove-cookies!]
  [:environment/clear-browser-cookies!])

(a/reg-event-fx
  :environment/read-system-cookies!
  [(a/inject-cofx :environment/inject-system-cookies!)]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      ; Store injected cookies w/ interceptor ...
      {:db db}))

(a/reg-event-fx
  :environment/store-cookie-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [cookie-settings (r get-cookie-settings db)]
           [:environment/set-cookie! :cookie-settings
                                     {:cookie-type :necessary
                                      :value       cookie-settings}])))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-browser-cookie!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) cookie-id
  ; @param (map) cookie-props
  ;  {:cookie-type (keyword)
  ;   :domain (string)
  ;   :max-age (sec)
  ;   :path (string)
  ;   :secure (boolean)
  ;   :same-site (string)
  ;   :value (*)}
  [cookie-id {:keys [max-age path domain secure same-site value] :as cookie-props}]
  (let [cookie-name (cookie-id->cookie-name cookie-id cookie-props)
        cookie-body (str {:cookie-id cookie-id :value value})]
       (try (.set goog.net.cookies cookie-name cookie-body
                  max-age path domain secure same-site)
            (a/dispatch [:environment/->cookie-set cookie-id cookie-props]))))

(a/reg-handled-fx :environment/store-browser-cookie! store-browser-cookie!)

(defn- remove-browser-cookie!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  [cookie-id cookie-props]
  (let [cookie-name (cookie-id->cookie-name cookie-id cookie-props)]
       (try (.remove goog.net.cookies cookie-name COOKIE-PATH COOKIE-DOMAIN)
            (a/dispatch [:environment/->cookie-removed cookie-id]))))

(a/reg-handled-fx :environment/remove-browser-cookie! remove-browser-cookie!)

(defn- remove-browser-cookies!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (try (.clear goog.net.cookies)
       (a/dispatch [:environment/->cookies-removed])))

(a/reg-handled-fx :environment/remove-browser-cookies! remove-browser-cookies!)



;; -- Status-events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/->cookie-settings-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:environment/store-cookie-settings!])

(defn- ->cookie-set
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  [db [_ cookie-id cookie-props]]
  (r store-cookie-value! db cookie-id cookie-props))

(a/reg-event-db :environment/->cookie-set ->cookie-set)

(defn- ->cookie-removed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  [db [_ cookie-id]]
  (r remove-cookie-value! db cookie-id))

(a/reg-event-db :environment/->cookie-removed ->cookie-removed)

(defn- ->cookies-removed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (r db/empty-partition! db ::cookies))

(a/reg-event-db :environment/->cookies-removed ->cookies-removed)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [:environment/read-system-cookies!]})
