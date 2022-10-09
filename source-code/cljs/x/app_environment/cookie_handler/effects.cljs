
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.cookie-handler.effects
    (:require [re-frame.api                                :as r :refer [r]]
              [x.app-environment.cookie-handler.prototypes :as cookie-handler.prototypes]
              [x.app-environment.cookie-handler.subs       :as cookie-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :environment/set-cookie!
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
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ cookie-id cookie-props]]
      (let [cookie-props (cookie-handler.prototypes/cookie-props-prototype cookie-props)]
           (if (r cookie-handler.subs/set-cookie? db cookie-id cookie-props)
               {:fx [:environment/store-browser-cookie! cookie-id cookie-props]}))))

(r/reg-event-fx :environment/remove-cookie!
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
      (let [cookie-props (cookie-handler.prototypes/cookie-props-prototype cookie-props)]
           {:fx [:environment/remove-browser-cookie! cookie-id cookie-props]})))

(r/reg-event-fx :environment/remove-cookies!
  ; @usage
  ;  [:environment/remove-cookies!]
  {:fx [:environment/remove-browser-cookies!]})

(r/reg-event-fx :environment/read-system-cookies!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [(r/inject-cofx :environment/inject-system-cookies!)]
  (fn [{:keys [db]}]
      ; Store injected cookies w/ interceptor ...
      {:db db}))

(r/reg-event-fx :environment/store-cookie-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if (r cookie-handler.subs/necessary-cookies-enabled? db)
          (let [cookie-settings (r cookie-handler.subs/get-cookie-settings db)]
               {:fx [:environment/store-browser-cookie! :cookie-settings
                                                        {:cookie-type :necessary :value cookie-settings}]}))))

(r/reg-event-fx :environment/cookie-settings-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:environment/store-cookie-settings!])
