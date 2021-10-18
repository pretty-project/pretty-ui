
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
    (:require [x.server-developer.api]
              [x.server-router.api]
              [mid-fruits.candy       :refer [param]]
              [mid-fruits.map         :as map]
              [server-fruits.http     :as http]
              [x.app-details          :as details]
              [x.server-core.api      :as a :refer [r]]
              [x.server-db.api        :as db]
              [x.server-installer.api :as installer]))



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
      (println details/app-name "run app")
                       ; A szerver indítási paramétereinek eltárolása
      {:dispatch-tick [{:tick   0 :dispatch [:x.boot-loader/store-server-props! server-props]}
                       ; A konfigurációs fájlok tartalmának eltárolása
                       {:tick   0 :dispatch [:x.server-core/config-app!]}
                       ; A telepítés vizsgálata
                       {:tick 500 :dispatch [:x.boot-loader/check-install!]}]}))

(a/reg-event-fx
  :x.boot-loader/check-install!
  [a/self-destruct!]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println details/app-name "check install")
      (if (r installer/server-installed? db)
          (let [installed-at (r installer/get-installed-at db)]
               (println details/app-name "installed at:" installed-at)
               [:x.boot-loader/initialize-app!])
          [:x.server-installer/install-server!])))

(a/reg-event-fx
  :x.boot-loader/initialize-app!
  [a/self-destruct!]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println details/app-name "initialize app")
       ; 1. Az inicializálási események meghívása (Dispatch on-app-init events)
      {:dispatch-n (r a/get-period-events db :on-app-init)
       ; 2. Az inicializálási események lefutása után az applikáció
       ;    betöltésének folytatása
       :dispatch-later [{:ms 250 :dispatch [:x.boot-loader/boot-app!]}]}))

(a/reg-event-fx
  :x.boot-loader/boot-app!
  [a/self-destruct!]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println details/app-name "boot app")
       ; 1. Az indítási események meghívása (Dispatch on-app-boot events)
      {:dispatch-n (r a/get-period-events db :on-app-boot)
       :dispatch-tick [; 2. A szerver indítása
                       {:tick  50 :dispatch [:x.server-core/run-server! (r get-server-props db)]}
                       ; 4. Az indítási események lefutása után az applikáció betöltésének folytatása
                       {:tick 100 :dispatch [:x.boot-loader/launch-app!]}]}))

(a/reg-event-fx
  :x.boot-loader/launch-app!
  [a/self-destruct!]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println details/app-name "launch app")
      ; A szerver indítása utáni események meghívása (Dispatch on-app-launch events)
      {:dispatch-n (r a/get-period-events db :on-app-launch)}))
