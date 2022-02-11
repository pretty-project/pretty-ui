
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.30
; Description:
; Version: v0.7.6
; Compatibility: x4.5.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.window-handler
    (:require [app-fruits.dom    :as dom]
              [app-fruits.window :as window]
              [mid-fruits.candy  :refer [param]]
              [mid-fruits.time   :as time]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- connection-change-listener
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (function)
  []
  (a/dispatch [:environment/connection-changed]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn interval-exists?
  ; @param (keyword) interval-id
  ;
  ; @usage
  ;  (r environment/interval-exists? db :my-interval)
  ;
  ; @return (boolean)
  [db [_ interval-id]]
  (let [interval-props (get-in db (db/path :environment/intervals interval-id))]
       (some? interval-props)))

(defn timeout-exists?
  ; @param (keyword) timeout-id
  ;
  ; @usage
  ;  (r environment/timeout-exists? db :my-timeout)
  ;
  ; @return (boolean)
  [db [_ timeout-id]]
  (let [timeout-props (get-in db (db/path :environment/timeouts timeout-id))]
       (some? timeout-props)))

(defn browser-online?
  ; @usage
  ;  (r environment/browser-online? db)
  ;
  ; @return (boolean)
  [db _]
  (let [browser-online? (get-in db (db/meta-item-path :environment/window-data :browser-online?))]
       (boolean browser-online?)))

(defn browser-offline?
  ; @usage
  ;  (r environment/browser-offline? db)
  ;
  ; @return (boolean)
  [db _]
  (let [browser-online? (get-in db (db/meta-item-path :environment/window-data :browser-online?))]
       (not browser-online?)))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn open-new-browser-tab!
  ; @param (string) uri
  ;
  ; @usage
  ;  (environment/open-new-browser-tab! "www.my-site.com/my-link")
  [uri]
  (.open js/window uri "_blank"))

; @usage
;  {:environment/open-new-browser-tab! "www.my-site.com/my-link"}
;
; @usage
;  [:environment/open-new-browser-tab! "www.my-site.com/my-link"]
(a/reg-handled-fx :environment/open-new-browser-tab! open-new-browser-tab!)

(defn set-window-title!
  ; @param (string) title
  ;
  ; @usage
  ;  (environment/set-window-title! "My title")
  [title]
  (set! (-> js/document .-title) title))

; @usage
;  {:environment/set-window-title! "My title"}
;
; @usage
;  [:environment/set-window-title! "My title"]
(a/reg-handled-fx :environment/set-window-title! set-window-title!)

(defn reload-window!
  ; @usage
  ;  (environment/reload-window!)
  []
  (.reload js/window.location true))

; @usage
;  {:environment/reload-window!}
;
; @usage
;  [:environment/reload-window!]
(a/reg-handled-fx :environment/reload-window! reload-window!)

(defn go-to-root!
  ; @usage
  ;  (environment/go-to-root!)
  []
  (set! (-> js/window .-location .-href) "/"))

; @usage
;  {:environment/go-to-root!}
;
; @usage
;  [:environment/go-to-root!]
(a/reg-handled-fx :environment/go-to-root!)

(defn go-to!
  ; @param (string) uri
  ;
  ; @usage
  ;  (environment/go-to! "www.my-site.com/my-link")
  [uri]
  (set! (-> js/window .-location .-href) uri))

; @usage
;  {:environment/go-to! "www.my-site.com/my-link"}
;
; @usage
;  [:environment/go-to! "www.my-site.com/my-link"]
(a/reg-handled-fx :environment/go-to! go-to!)

(defn set-interval!
  ; @param (keyword) interval-id
  ; @param (map) interval-props
  ;  {:event (metamorphic-event)
  ;   :interval (ms)}
  ;
  ; @usage
  ;  (environment/set-interval! :my-interval {:event [:do-something!]
  ;                                           :interval 420})
  [interval-id {:keys [interval event] :as interval-props}]
  (fn [[interval-id {:keys [interval event] :as interval-props}]]
      (let [js-id          (time/set-interval! interval #(a/dispatch event))
            interval-props (assoc interval-props :js-id js-id)]
           ; TODO ...
           ; Re-Frame adatbázis helyett helyi atomban legyenek tárolva!
           (a/dispatch [:environment/store-interval-props! interval-id interval-props]))))

; @usage
;  {:environment/set-interval! :my-interval {:event [:do-something!] :interval 420}}
;
; @usage
;  [:environment/set-interval! :my-interval {:event [:do-something!] :interval 420}]
(a/reg-handled-fx :environment/set-interval! set-interval!)

(defn clear-interval!
  ; @param (keyword) js-id
  ;
  ; @usage
  ;  (environment/clear-interval! :my-interval)
  [js-id]
  (time/clear-interval! js-id))

; @usage
;  {:environment/clear-interval! :my-interval}
;
; @usage
;  [:environment/clear-interval! :my-interval]
(a/reg-handled-fx :environment/clear-interval! clear-interval!)

(defn set-timeout!
  ; @param (keyword) timeout-id
  ; @param (map) timeout-props
  ;  {:event (metamorphic-event)
  ;   :timeout (ms)}
  ;
  ; @usage
  ;  (environment/set-timeout! :my-timeout {:event [:do-something!]
  ;                                         :timeout 420})
  [timeout-id {:keys [timeout event] :as timeout-props}]
  (let [js-id         (time/set-timeout! timeout #(a/dispatch event))
        timeout-props (assoc timeout-props :js-id js-id)]
       ; TODO ...
       ; Re-Frame adatbázis helyett helyi atomban legyenek tárolva!
       (a/dispatch [:environment/store-timeout-props! timeout-id timeout-props])))

; @usage
;  {:environment/set-timeout! :my-timeout {:event [:do-something!] :timeout 420}}
;
; @usage
;  [:environment/set-timeout! :my-timeout {:event [:do-something!] :timeout 420}]
(a/reg-handled-fx :environment/set-timeout! set-timeout!)

(defn clear-timeout!
  ; @param (keyword) js-id
  ;
  ; @usage
  ;  (environment/clear-timeout! :my-timeout)
  [js-id])
  ; TODO ...

; @usage
;  {:environment/clear-timeout! :my-timeout}
;
; @usage
;  [:environment/clear-timeout! :my-timeout]
(a/reg-handled-fx :environment/clear-timeout! clear-timeout!)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-interval-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) interval-id
  ; @param (map) interval-props
  ;
  ; @return (map)
  [db [_ interval-id interval-props]]
  (assoc-in db (db/path :environment/intervals interval-id) interval-props))

(a/reg-event-db :environment/store-interval-props! store-interval-props!)

(defn- store-timeout-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) timeout-id
  ; @param (map) timeout-props
  ;
  ; @return (map)
  [db [_ timeout-id timeout-props]]
  (assoc-in db (db/path :environment/timeouts timeout-id) timeout-props))

(a/reg-event-db :environment/store-timeout-props! store-timeout-props!)

(defn- update-window-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (-> db (assoc-in (db/meta-item-path :environment/window-data :browser-online?) (window/browser-online?))
         (assoc-in (db/meta-item-path :environment/window-data :language)        (window/get-language))
         (assoc-in (db/meta-item-path :environment/window-data :user-agent)      (window/get-user-agent))))

(a/reg-event-db :environment/update-window-data! update-window-data!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/reg-interval!
  ; @param (keyword)(opt) interval-id
  ; @param (map) interval-props
  ;  {:event (metamorphic-event)
  ;   :interval (ms)}
  ;
  ; @usage
  ;  [:environment/reg-interval! :my-interval {:event [:do-something!] :interval 420}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ interval-id interval-props]]
      (if-not (r interval-exists? db interval-id)
              {:environment/set-interval! [interval-id interval-props]})))

(a/reg-event-fx
  :environment/remove-interval!
  ; @param (keyword) interval-id
  ;
  ; @usage
  ;  [:environment/remove-interval! :my-interval]
  (fn [{:keys [db]} [_ interval-id]]
      (if-let [{:keys [js-id]} (get-in db (db/path :environment/intervals interval-id))]
              {:environment/clear-interval! js-id})))

(a/reg-event-fx
  :environment/reg-timeout!
  ; @param (keyword)(opt) timeout-id
  ; @param (map) timeout-props
  ;  {:event (metamorphic-event)
  ;   :timeout (ms)}
  ;
  ; @usage
  ;  [:environment/reg-timeout! :my-timeout {:event [:do-something!] :timeout 420}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ timeout-id timeout-props]]
      (if-not (r timeout-exists? db timeout-id)
              {:environment/set-timeout! [timeout-id timeout-props]})))

(a/reg-event-fx
  :environment/remove-timeout!
  ; @param (keyword) timeout-id
  ;
  ; @usage
  ;  [:environment/remove-timeout! :my-timeout]
  (fn [{:keys [db]} [_ timeout-id]]
      (if-let [{:keys [js-id]} (get-in db (db/path :environment/timeouts timeout-id))]
              {:environment/clear-timeout! js-id})))

(a/reg-event-fx
  :environment/connection-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [browser-online? (window/browser-online?)]
           {:db (assoc-in db (db/meta-item-path :environment/window-data :browser-online?) browser-online?)
            :dispatch-if [browser-online? [:core/connect-app!]
                                          [:core/disconnect-app!]]})))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- listen-to-connection-change!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (do (dom/add-event-listener!  "online" connection-change-listener)
      (dom/add-event-listener! "offline" connection-change-listener)))

(a/reg-handled-fx :environment/listen-to-connection-change! listen-to-connection-change!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-init {:dispatch-n [[:environment/update-window-data!]
                              [:environment/listen-to-connection-change!]]}})
