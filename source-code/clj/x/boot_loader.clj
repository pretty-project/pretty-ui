
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.14
; Description:
; Version: v1.2.6
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader
    (:require [x.server-developer.api]
              [x.server-environment.api]
              [x.server-router.api]
              [x.server-views.api]
              [mid-fruits.candy  :refer [param return]]
              [x.app-details     :as details]
              [x.server-core.api :as a :refer [r]]
              [x.server-db.api   :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; DEBUG
(def DEBUG? false)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-server!
  ; @param (map)(opt) server-props
  ;  {:join? (boolean)(opt)
  ;    Default: false
  ;   :port (integer or string)(opt)
  ;    Default: DEFAULT-PORT}
  ([]             (start-server! {}))
  ([server-props] (a/dispatch [:boot-loader/start-server! server-props])))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-server-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path :boot-loader/primary :server-props)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-server-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  ;
  ; @return (map)
  [db [_ server-props]]
  (assoc-in db (db/path :boot-loader/primary :server-props) server-props))

(a/reg-event-db :boot-loader/store-server-props! store-server-props!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :boot-loader/start-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  (fn [_ [_ server-props]]
      (println details/app-codename "starting server ...")
      {:core/import-lifecycles! nil
       :dispatch-tick [; A szerver indítási paramétereinek eltárolása
                       {:tick   0 :dispatch [:boot-loader/store-server-props! server-props]}
                       ; A konfigurációs fájlok tartalmának eltárolása
                       {:tick   0 :dispatch [:core/config-server!]}
                       ; A szerver inicializálása
                       {:tick 500 :dispatch [:boot-loader/initialize-server!]}]}))

(a/reg-event-fx
  :boot-loader/initialize-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println details/app-codename "initializing server ...")
       ; 1. Az inicializálási események meghívása (Dispatch on-server-init events)


      (if DEBUG? (println ":on-server-init-events:"))
      (if DEBUG? (println (str (r a/get-period-events db :on-server-init))))


      {:dispatch   [:core/connect-to-database!]
       :dispatch-n (r a/get-period-events db :on-server-init)
       ; 2. Az inicializálási események lefutása után a szerver
       ;    betöltésének folytatása
       :dispatch-tick [{:tick 250 :dispatch [:boot-loader/boot-server!]}]}))

(a/reg-event-fx
  :boot-loader/boot-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println details/app-codename "booting server ...")
       ; 1. Az indítási események meghívása (Dispatch on-server-boot events)

      (if DEBUG? (println ":on-server-boot-events:"))
      (if DEBUG? (println (str (r a/get-period-events db :on-server-boot))))



      ; TEMP
      (a/->app-built)

      


      {:dispatch-n (r a/get-period-events db :on-server-boot)
       :dispatch-tick [; 2. A szerver indítása
                       {:tick  50 :dispatch [:core/run-server! (r get-server-props db)]}
                       ; 4. Az indítási események lefutása után a szerver betöltésének folytatása
                       {:tick 100 :dispatch [:boot-loader/launch-server!]}]}))

(a/reg-event-fx
  :boot-loader/launch-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println details/app-codename "launching server ...")
      ; A szerver indítása utáni események meghívása (Dispatch on-server-launch events)
      {:dispatch-n (r a/get-period-events db :on-server-launch)}))
