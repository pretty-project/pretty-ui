
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.21
; Description:
; Version: 2.0.8
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.cookie-handler.effects
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-environment.cookie-handler.subs :as subs]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cookie-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  ;  {:cookie-type (keyword)(opt)
  ;   :max-age (integer)(opt)}
  ;
  ; @return (map)
  ;  {:cookie-type (keyword)
  ;   :max-age (integer)
  ;   :secure (boolean)}
  [cookie-props]
  (merge {:cookie-type :user-experience
          :max-age     -1}
         (param cookie-props)
         {:secure    true
          :same-site "strict"}))



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
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ cookie-id cookie-props]]
      (let [cookie-props (cookie-props-prototype cookie-props)]
           (if (r subs/set-cookie? db cookie-id cookie-props)
               {:fx [:environment/store-browser-cookie! cookie-id cookie-props]}))))

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
      (let [cookie-props (cookie-props-prototype cookie-props)]
           {:fx [:environment/remove-browser-cookie! cookie-id cookie-props]})))

(a/reg-event-fx
  :environment/remove-cookies!
  ; @usage
  ;  [:environment/remove-cookies!]
  {:fx [:environment/remove-browser-cookies!]})

(a/reg-event-fx
  :environment/read-system-cookies!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [(a/inject-cofx :environment/inject-system-cookies!)]
  (fn [{:keys [db]}]
      ; Store injected cookies w/ interceptor ...
      {:db db}))

(a/reg-event-fx
  :environment/store-cookie-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if (r subs/necessary-cookies-enabled? db)
          (let [cookie-settings (r subs/get-cookie-settings db)]
               {:fx [:environment/store-browser-cookie! :cookie-settings
                                                        {:cookie-type :necessary :value cookie-settings}]}))))

(a/reg-event-fx
  :environment/cookie-settings-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:environment/store-cookie-settings!])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-init [:environment/read-system-cookies!]})
