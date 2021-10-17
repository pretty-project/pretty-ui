
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.30
; Description:
; Version: v0.5.8
; Compatibility: x3.9.9



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

(def connection-change-listener
     #(a/dispatch [::->connection-changed]))



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

(a/reg-handled-fx
  ::open-new-browser-tab!
  ; @param (string) uri
  ;
  ; @usage
  ;  [:x.app-environment.window-handler/open-new-browser-tab! "www.my-site.com/my-link"]
  (fn [[uri]]
      (.open js/window uri "_blank")))

(a/reg-handled-fx
  ::set-title!
  ; @param (string) title
  ;
  ; @usage
  ;  [:x.app-environment.window-handler/set-title! "My title"]
  (fn [[title]]
      (set! (-> js/document .-title) title)))

(a/reg-handled-fx
  ::reload!
  #(.reload js/window.location true))

(a/reg-handled-fx
  ::go-to-root!
  ; @usage
  ;  [:x.app-environment.window-handler/go-to-root!]
  #(set! (-> js/window .-location .-href) "/"))

(a/reg-handled-fx
  ::go-to!
  ; @param (string) uri
  ;
  ; @usage
  ;  [:x.app-environment.window-handler/go-to! "www.my-site.com/my-link"]
  (fn [[uri]]
      (set! (-> js/window .-location .-href) uri)))

(a/reg-handled-fx
  ::set-interval!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) interval-id
  ; @param (map) interval-props
  ;  {:event (metamorphic-event)
  ;   :interval (ms)}
  ;
  ; @usage
  ;  [:x.app-environment.window-handler/set-interval! :my-interval {:event [:do-something!]
  ;                                                                 :interval 420}]
  (fn [[interval-id {:keys [interval event] :as interval-props}]]
      (let [js-id          (time/set-interval! interval #(a/dispatch event))
            interval-props (assoc interval-props :js-id js-id)]
           (a/dispatch [::store-interval-props! interval-id interval-props]))))

(a/reg-handled-fx
  ::clear-interval!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) js-id
  ;
  ; @usage
  ;  [:x.app-environment.window-handler/clear-interval! :my-interval]
  (fn [[js-id]]
      (time/clear-interval! js-id)))

(a/reg-handled-fx
  ::set-timeout!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) timeout-id
  ; @param (map) timeout-props
  ;  {:event (metamorphic-event)
  ;   :timeout (ms)}
  ;
  ; @usage
  ;  [:x.app-environment.window-handler/set-timeout! :my-timeout {:event [:do-something!]
  ;                                                               :timeout 420}]
  (fn [[timeout-id {:keys [timeout event] :as timeout-props}]]
      (let [js-id         (time/set-timeout! timeout #(a/dispatch event))
            timeout-props (assoc timeout-props :js-id js-id)]
           (a/dispatch [::store-timeout-props! timeout-id timeout-props]))))

(a/reg-handled-fx
  ::clear-timeout!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) js-id
  ;
  ; @usage
  ;  [:x.app-environment.window-handler/clear-timeout! :my-timeout]
  (fn [[js-id]]))
      ; TODO ...



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
  (-> db (assoc-in (db/meta-item-path ::primary :browser-online?)
                   (window/browser-online?))
         (assoc-in (db/meta-item-path ::primary :language)
                   (window/get-language))
         (assoc-in (db/meta-item-path ::primary :user-agent)
                   (window/get-user-agent))))

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
           {:dispatch-if
            [(not (r interval-exists? db interval-id))
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
           {:dispatch-if
            [(not (r timeout-exists? db timeout-id))
             [::set-timeout! timeout-id timeout-props]]})))

(a/reg-event-fx
  ::remove-timeout!
  ; @param (keyword) timeout-id
  (fn [{:keys [db]} [_ timeout-id]]
      (let [{:keys [js-id]} (get-in db (db/path ::timeouts timeout-id))]
           [::clear-timeout! js-id])))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-handled-fx
  ::listen-to-connection-change!
  #(do (dom/add-event-listener! "online"  connection-change-listener)
       (dom/add-event-listener! "offline" connection-change-listener)))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::->connection-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      (let [browser-online? (window/browser-online?)]
           {:x.app-db [[:set-item! (db/meta-item-path ::primary :browser-online?)
                                   (param browser-online?)]]
            :dispatch-if [(param browser-online?)
                          [:x.app-core/connect-app!]
                          [:x.app-core/disconnect-app!]]})))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[::update-window-data!]
                [::listen-to-connection-change!]]})

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [::initialize!]})
