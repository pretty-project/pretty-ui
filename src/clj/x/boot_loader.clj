
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.14
; Description:
; Version: v0.9.2
; Compatibility: x4.2.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader
    (:require [x.server-router.api]
              [mid-fruits.candy   :refer [param]]
              [mid-fruits.map     :as map]
              [server-fruits.http :as http]
              [x.server-developer.api]
              [x.server-core.api  :as a :refer [r]]
              [x.server-db.api    :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn run-app!
  ; @param (map)(opt) server-props
  ;  {:join? (boolean)(opt)
  ;    Default: false
  ;   :port (integer or string)(opt)
  ;    Default: DEFAULT-PORT}
  ([]             (run-app! {}))
  ([server-props] (a/dispatch [:x.boot-loader/run-app! server-props])))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-server-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path ::primary :server-props)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-server-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  ;
  ; @return (map)
  [db [_ server-props]]
  (assoc-in db (db/path ::primary :server-props)
               (param server-props)))

(a/reg-event-db :x.boot-loader/store-server-props! store-server-props!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.boot-loader/run-app!
  [a/self-destruct!]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  (fn [_ [_ server-props]]
      {:dispatch-n [[:x.boot-loader/store-server-props! server-props]
                    [:x.boot-loader/initialize-app!]]}))

(a/reg-event-fx
  :x.boot-loader/initialize-app!
  [a/self-destruct!]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
       ; 1. Az inicializálási események meghívása (Dispatch on-app-init events)
      {:dispatch-n (r a/get-period-events db :on-app-init)
       ; 2. Az inicializálási események lefutása után az applikáció
       ;    betöltésének folytatása
       :dispatch-tick [{:tick 10 :dispatch [:x.boot-loader/boot-app!]}]}))

(a/reg-event-fx
  :x.boot-loader/boot-app!
  [a/self-destruct!]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
       ; 1. Az indítási események meghívása (Dispatch on-app-boot events)
      {:dispatch-n (r a/get-period-events db :on-app-boot)
                       ; 3. Az applikáció konfigurálása
       :dispatch-tick [{:tick  0 :dispatch [:x.server-core/config-app!]}
                       ; 4. A szerver indítása
                       {:tick 10 :dispatch [:x.server-core/run-server! (r get-server-props db)]}
                       ; 5. Az indítási események lefutása után az applikáció
                       ;    betöltésének folytatása
                       {:tick 20 :dispatch [:x.boot-loader/launch-app!]}]}))

(a/reg-event-fx
  :x.boot-loader/launch-app!
  [a/self-destruct!]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      ; A szerver indítása utáni események meghívása (Dispatch on-app-launch events)
      {:dispatch-n (r a/get-period-events db :on-app-launch)}))
