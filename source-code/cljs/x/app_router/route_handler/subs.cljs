
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.subs
    (:require [mid-fruits.candy    :refer [param return]]
              [mid-fruits.uri      :as uri]
              [mid-fruits.vector   :as vector]
              [reitit.frontend     :as reitit.frontend]
              [x.app-core.api      :as a :refer [r]]
              [x.app-router.engine :as engine]
              [x.app-user.api      :as user]
              [x.app-router.route-handler.engine :as route-handler.engine]
              [x.mid-router.route-handler.subs   :as route-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-router.route-handler.subs
(def get-app-home     route-handler.subs/get-app-home)
(def get-resolved-uri route-handler.subs/get-resolved-uri)



;; -- Debug subscriptions -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-debug-route-string
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (string)
  [db [_ route-string]]
  (if-let [debug-mode (r a/get-debug-mode db)]
          (uri/uri<-query-param route-string debug-mode)
          (return               route-string)))



;; -- Router subscriptions ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-client-routes
  ; @return (map)
  [db _]
  (get-in db [:router :route-handler/client-routes]))

(defn- get-router-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (vector)
  [db _]
  (let [client-routes (r get-client-routes db)]
       (engine/routes->router-routes client-routes)))

(defn- get-router
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (?)
  [db _]
  (let [router-routes (r get-router-routes db)]
       (reitit.frontend/router router-routes)))

(defn- get-route-match
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-template
  ;
  ; @return (map)
  ;  https://github.com/metosin/reitit
  [db [_ route-template]]
  (let [router (r get-router db)]
       (reitit.frontend/match-by-path router route-template)))

(defn- route-template-exists?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (boolean)
  [db [_ route-string]]
  (let [route-match (r get-route-match db route-string)]
       (boolean route-match)))



;; -- Route subscriptions -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-exists?
  ; @param (keyword) route-id
  ;
  ; @usage
  ;  (r router/route-exists? db :my-route)
  ;
  ; @return boolean)
  [db [_ route-id]]
  (let [route-props (get-in db [:router :route-handler/client-routes route-id])]
       (some? route-props)))

(defn- route-restricted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (let [route-restricted? (get-in db [:router :route-handler/client-routes route-id :restricted?])]
       (boolean route-restricted?)))

(defn- require-authentication?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (and (r route-restricted?       db route-id)
       (r user/user-unidentified? db)))



;; -- Match subscriptions -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- match-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (keyword)
  [db [_ route-string]]
  (if (r route-template-exists? db route-string)
      (let [route-match (r get-route-match db route-string)]
           (get-in route-match [:data :name]))
      (return :page-not-found)))



;; -- Current route subscriptions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-route-string
  ; @usage
  ;  (r router/get-current-route-string db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:router :route-handler/meta-items :route-string]))

(defn get-current-route-id
  ; @usage
  ;  (r router/get-current-route-id db)
  ;
  ; @return (keyword)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (r match-route-id db current-route-string)))

(defn get-current-route-path
  ; @usage
  ;  (r router/get-current-route-path db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (uri/uri->path current-route-string)))

(defn get-current-route-template
  ; @usage
  ;  (r router/get-current-route-template db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-id (r get-current-route-id db)]
       (get-in db [:router :route-handler/client-routes current-route-id :route-template])))

(defn get-current-route-path-params
  ; @usage
  ;  (r router/get-current-route-path-params db)
  ;
  ; @return (map)
  [db _]
  (let [current-route-string   (r get-current-route-string   db)
        current-route-template (r get-current-route-template db)]
       (uri/uri->path-params current-route-string current-route-template)))

(defn get-current-route-path-param
  ; @param (keyword) param-key
  ;
  ; @usage
  ;  (r router/get-current-route-path-param db :my-param)
  ;
  ; @return (string)
  [db [_ param-key]]
  (let [current-route-path-params (r get-current-route-path-params db)]
       (get current-route-path-params param-key)))

(defn current-route-path-param?
  ; @param (keyword) param-key
  ; @param (string) param-value
  ;
  ; @usage
  ;  (r router/current-route-path-param? db :my-param "My value")
  ;
  ; @return (boolean)
  [db [_ param-key param-value]]
  (let [current-route-path-param (r get-current-route-path-param db param-key)]
       (= current-route-path-param param-value)))

(defn get-current-route-query-params
  ; @usage
  ;  (r router/get-current-route-query-params db)
  ;
  ; @return (map)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (uri/uri->query-params current-route-string)))

(defn get-current-route-query-param
  ; @param (keyword) param-key
  ;
  ; @usage
  ;  (r router/get-current-route-query-param db :my-param)
  ;
  ; @return (string)
  [db [_ param-key]]
  (let [current-route-query-params (r get-current-route-query-params db)]
       (get current-route-query-params param-key)))

(defn current-route-query-param?
  ; @param (keyword) param-key
  ; @param (string) param-value
  ;
  ; @usage
  ;  (r router/current-route-query-param? db :my-param "My value")
  ;
  ; @return (boolean)
  [db [_ param-key param-value]]
  (let [current-route-query-param (r get-current-route-query-param db param-key)]
       (= current-route-query-param param-value)))

(defn get-current-route-fragment
  ; @usage
  ;  (r router/get-current-route-fragment db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-string (r get-current-route-string db)]
       (uri/uri->fragment current-route-string)))

(defn get-current-route-parent
  ; @usage
  ;  (r router/get-current-route-parent db)
  ;
  ; @return (string)
  [db _]
  (let [current-route-id (r get-current-route-id db)]
       (get-in db [:router :route-handler/client-routes current-route-id :route-parent])))



;; -- Handle subscriptions ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn at-home?
  ; @usage
  ;  (r router/at-home? db)
  ;
  ; @return (boolean)
  [db _]
  (let [app-home           (r get-app-home           db)
        current-route-path (r get-current-route-path db)]
       (= current-route-path app-home)))

(defn- route-id-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (boolean)
  [db [_ route-id]]
  (let [current-route-id (r get-current-route-id db)]
       (not= route-id current-route-id)))

(defn- reload-same-path?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (boolean)
  [db [_ route-string]]
  (let [current-route-string (r get-current-route-string db)]
       (and route-handler.engine/RELOAD-SAME-PATH? (= route-string current-route-string))))

(defn- get-history
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keywords in vector)
  [db _]
  (get-in db [:router :route-handler/meta-items :history]))

(defn- get-previous-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (let [history (r get-history db)]
       (vector/last-item history)))

(defn get-router-data
  ; @usage
  ;  (r router/get-router-data db)
  ;
  ; @return (map)
  [db _]
  {:route-string       (r get-current-route-string       db)
   :route-id           (r get-current-route-id           db)
   :route-path         (r get-current-route-path         db)
   :route-template     (r get-current-route-template     db)
   :route-path-params  (r get-current-route-path-params  db)
   :route-query-params (r get-current-route-query-params db)
   :route-fragment     (r get-current-route-fragment     db)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :router/route-template-exists? route-template-exists?)

; @usage
;  [:router/get-current-route-string]
(a/reg-sub :router/get-current-route-string get-current-route-string)

; @usage
;  [:router/get-current-route-id]
(a/reg-sub :router/get-current-route-id get-current-route-id)

; @usage
;  [:router/get-current-route-path]
(a/reg-sub :router/get-current-route-path get-current-route-path)

; @usage
;  [:router/get-current-route-template]
(a/reg-sub :router/get-current-route-template get-current-route-template)

; @usage
;  [:router/get-current-route-path-params]
(a/reg-sub :router/get-current-route-path-params get-current-route-path-params)

; @usage
;  [:router/get-current-route-path-param :my-param]
(a/reg-sub :router/get-current-route-path-param get-current-route-path-param)

; @usage
;  [:router/current-route-path-param? :my-param "My value"]
(a/reg-sub :router/current-route-path-param? current-route-path-param?)

; @usage
;  [:router/get-current-route-query-param]
(a/reg-sub :router/get-current-route-query-params get-current-route-query-params)

; @usage
;  [:router/get-current-route-query-param :my-param]
(a/reg-sub :router/get-current-route-query-param get-current-route-query-param)

; @usage
;  [:router/current-route-query-param? :my-param "My value"]
(a/reg-sub :router/current-route-query-param? current-route-query-param?)

; @usage
;  [:router/get-current-route-fragment]
(a/reg-sub :router/get-current-route-fragment get-current-route-fragment)

; @usage
;  [:router/get-current-route-parent]
(a/reg-sub :router/get-current-route-parent get-current-route-parent)

; @usage
;  [:router/at-home?]
(a/reg-sub :router/at-home? at-home?)

; @usage
;  [:router/get-router-data]
(a/reg-sub :router/get-router-data get-router-data)
