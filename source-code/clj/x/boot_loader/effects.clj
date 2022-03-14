
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.effects
    (:require [x.app-details        :as details]
              [x.boot-loader.events :as events]
              [x.boot-loader.subs   :as subs]
              [x.server-core.api    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; DEBUG
(def DEBUG? false)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :boot-loader/start-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  (fn [{:keys [db]} [_ server-props]]
      (println details/app-codename "starting server ...")
      {; A szerver indítási paramétereinek eltárolása
       :db (r events/store-server-props! db server-props)
       :dispatch-tick [{:tick 500 :dispatch [:boot-loader/init-server!]}]
       :fx-n          [[:core/import-lifecycles!]
                       [:core/import-app-build!]
                       [:core/config-server!]]}))

(a/reg-event-fx
  :boot-loader/init-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]

      ; DEBUG
      (if DEBUG? (println ":on-server-init-events:"))
      (if DEBUG? (println (str (r a/get-period-events db :on-server-init))))

      (println details/app-codename "initializing server ...")
      {; 1. Az inicializálási események meghívása
       ;    (Dispatch on-server-init events)
       :dispatch   [:core/connect-to-database!]
       :dispatch-n (r a/get-period-events db :on-server-init)
       ; 2. Az inicializálási események lefutása után a szerver betöltésének folytatása
       :dispatch-tick [{:tick 250 :dispatch [:boot-loader/boot-server!]}]}))

(a/reg-event-fx
  :boot-loader/boot-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]

      ; DEBUG
      (if DEBUG? (println ":on-server-boot-events:"))
      (if DEBUG? (println (str (r a/get-period-events db :on-server-boot))))

      (println details/app-codename "booting server ...")
      {; 1. Az indítási események meghívása
       ;    (Dispatch on-server-boot events)
       :dispatch-n    (r a/get-period-events db :on-server-boot)
       :dispatch-tick [; 2. A szerver indítása
                       {:tick  50 :dispatch [:boot-loader/run-server!]}
                       ; 4. Az indítási események lefutása után a szerver betöltésének folytatása
                       {:tick 100 :dispatch [:boot-loader/launch-server!]}]}))

(a/reg-event-fx
  :boot-loader/run-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:fx [:core/run-server! (r subs/get-server-props db)]}))

(a/reg-event-fx
  :boot-loader/launch-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println details/app-codename "launching server ...")
      {; A szerver indítása utáni események meghívása
       ; (Dispatch on-server-launch events)
       :dispatch-n (r a/get-period-events db :on-server-launch)}))
