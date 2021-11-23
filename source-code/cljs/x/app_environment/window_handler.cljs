
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.30
; Description:
; Version: v0.6.4
; Compatibility: x4.4.6



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
  (a/dispatch [::->connection-changed]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- interval-exists?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) interval-id
  ;
  ; @return (boolean)
  [db [_ interval-id]]
  (some? (get-in db (db/path ::intervals interval-id))))

(defn- timeout-exists?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) timeout-id
  ;
  ; @return (boolean)
  [db [_ timeout-id]]
  (some? (get-in db (db/path ::timeouts timeout-id))))

(defn browser-online?
  ; @return (boolean)
  [db _]
  (boolean (get-in db (db/meta-item-path ::primary :browser-online?))))

(defn browser-offline?
  ; @return (boolean)
  [db _]
  (not (r browser-online? db)))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- open-new-browser-tab!
  ; @param (string) uri
  [uri]
  (.open js/window uri "_blank"))

; @usage
;  [:x.app-environment.window-handler/open-new-browser-tab! "www.my-site.com/my-link"]
(a/reg-handled-fx ::open-new-browser-tab! open-new-browser-tab!)

(defn- set-title!
  ; @param (string) title
  [title]
  (set! (-> js/document .-title) title))

; @usage
;  [:x.app-environment.window-handler/set-title! "My title"]
(a/reg-handled-fx ::set-title! set-title!)

(defn- reload!
  []
  (.reload js/window.location true))

; @usage
;  [:x.app-environment.window-handler/reload!]
(a/reg-handled-fx ::reload! reload!)

(defn- go-to-root!
  []
  (set! (-> js/window .-location .-href) "/"))

; @usage
;  [:x.app-environment.window-handler/go-to-root!]
(a/reg-handled-fx ::go-to-root!)

(defn- go-to!
  ; @param (string) uri
  [uri]
  (set! (-> js/window .-location .-href) uri))

; @usage
;  [:x.app-environment.window-handler/go-to! "www.my-site.com/my-link"]
(a/reg-handled-fx ::go-to! go-to!)

(defn- set-interval!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) interval-id
  ; @param (map) interval-props
  ;  {:event (metamorphic-event)
  ;   :interval (ms)}
  [interval-id {:keys [interval event] :as interval-props}]
  (fn [[interval-id {:keys [interval event] :as interval-props}]]
      (let [js-id          (time/set-interval! interval #(a/dispatch event))
            interval-props (assoc interval-props :js-id js-id)]
           ; TODO ...
           ; Re-Frame adatbázis helyett helyi atomban legyenek tárolva!
           (a/dispatch [::store-interval-props! interval-id interval-props]))))

; @usage
;  [:x.app-environment.window-handler/set-interval! :my-interval {:event [:do-something!]
;                                                                 :interval 420}]
(a/reg-handled-fx ::set-interval! set-interval!)

(defn- clear-interval!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) js-id
  [js-id]
  (time/clear-interval! js-id))

; @usage
;  [:x.app-environment.window-handler/clear-interval! :my-interval]
(a/reg-handled-fx ::clear-interval! clear-interval!)

(defn- set-timeout!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) timeout-id
  ; @param (map) timeout-props
  ;  {:event (metamorphic-event)
  ;   :timeout (ms)}
  [timeout-id {:keys [timeout event] :as timeout-props}]
  (let [js-id         (time/set-timeout! timeout #(a/dispatch event))
        timeout-props (assoc timeout-props :js-id js-id)]
       ; TODO ...
       ; Re-Frame adatbázis helyett helyi atomban legyenek tárolva!
       (a/dispatch [::store-timeout-props! timeout-id timeout-props])))

; @usage
;  [:x.app-environment.window-handler/set-timeout! :my-timeout {:event [:do-something!]
;                                                               :timeout 420}]
(a/reg-handled-fx ::set-timeout! set-timeout!)

(defn- clear-timeout!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) js-id
  [js-id])
  ; TODO ...

; @usage
;  [:x.app-environment.window-handler/clear-timeout! :my-timeout]
(a/reg-handled-fx ::clear-timeout! clear-timeout!)



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
  (assoc-in db (db/path ::intervals interval-id) interval-props))

(a/reg-event-db ::store-interval-props! store-interval-props!)

(defn- store-timeout-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) timeout-id
  ; @param (map) timeout-props
  ;
  ; @return (map)
  [db [_ timeout-id timeout-props]]
  (assoc-in db (db/path ::timeouts timeout-id) timeout-props))

(a/reg-event-db ::store-timeout-props! store-timeout-props!)

(defn- update-window-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (-> db (assoc-in (db/meta-item-path ::primary :browser-online?) (window/browser-online?))
         (assoc-in (db/meta-item-path ::primary :language)        (window/get-language))
         (assoc-in (db/meta-item-path ::primary :user-agent)      (window/get-user-agent))))

(a/reg-event-db ::update-window-data! update-window-data!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::reg-interval!
  ; @param (keyword)(opt) interval-id
  ; @param (map) interval-props
  ;  {:event (metamorphic-event)
  ;   :interval (ms)}
  (fn [{:keys [db]} event-vector]
      (let [interval-id    (a/event-vector->second-id   event-vector)
            interval-props (a/event-vector->first-props event-vector)]
           {:dispatch-if [(not (r interval-exists? db interval-id))
                          [::set-interval! interval-id interval-props]]})))

(a/reg-event-fx
  ::remove-interval!
  ; @param (keyword) interval-id
  (fn [{:keys [db]} [_ interval-id]]
      (let [{:keys [js-id]} (get-in db (db/path ::intervals interval-id))]
           [::clear-interval! js-id])))

(a/reg-event-fx
  ::reg-timeout!
  ; @param (keyword)(opt) timeout-id
  ; @param (map) timeout-props
  ;  {:event (metamorphic-event)
  ;   :timeout (ms)}
  (fn [{:keys [db]} event-vector]
      (let [timeout-id    (a/event-vector->second-id   event-vector)
            timeout-props (a/event-vector->first-props event-vector)]
           {:dispatch-if [(not (r timeout-exists? db timeout-id))
                          [::set-timeout! timeout-id timeout-props]]})))

(a/reg-event-fx
  ::remove-timeout!
  ; @param (keyword) timeout-id
  (fn [{:keys [db]} [_ timeout-id]]
      (let [{:keys [js-id]} (get-in db (db/path ::timeouts timeout-id))]
           [::clear-timeout! js-id])))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- listen-to-connection-change!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (do (dom/add-event-listener! "online"  connection-change-listener)
      (dom/add-event-listener! "offline" connection-change-listener)))

(a/reg-handled-fx ::listen-to-connection-change! listen-to-connection-change!)



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::->connection-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [browser-online? (window/browser-online?)]
           {:db          (assoc-in db (db/meta-item-path ::primary :browser-online?) browser-online?)
            :dispatch-if [browser-online? [:x.app-core/connect-app!]
                                          [:x.app-core/disconnect-app!]]})))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init {:dispatch-n [[::update-window-data!]
                              [::listen-to-connection-change!]]}})
