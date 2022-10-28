
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.effects
    (:require [re-frame.api         :as r :refer [r]]
              [x.app-details        :as x.details]
              [x.boot-loader.events :as events]
              [x.boot-loader.subs   :as subs]
              [x.server-core.api    :as x.core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :boot-loader/start-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  (fn [_ [_ server-props]]
      ; A server-props térképet szükséges eltárolni a folyamat elején, hogy a szerver
      ; beállítasai elérhetők legyenek a Re-Frame adatbázisban a teljes indítasi folyamat alatt.
      ; Pl.: A {:dev-mode? ...} esetleg a {:port ...} tulajdonságok értékei elérhetők legyenek
      ;      a névterek számára.
      (println x.details/app-codename "starting server ...")
      {:dispatch-tick [{:tick 500 :dispatch [:boot-loader/init-server! server-props]}]
       :dispatch      [:core/store-server-props! server-props]
       :fx-n          [[:core/import-lifecycles!]
                       [:core/import-app-build!]
                       [:core/import-app-config!]
                       [:core/import-server-config!]]}))

(r/reg-event-fx :boot-loader/init-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  (fn [{:keys [db]} [_ server-props]]
      (println x.details/app-codename "initializing server ...")
      {; 1. Az inicializálási események meghívása
       ;    (Dispatch on-server-init events)
       :dispatch   [:core/connect-to-database!]
       :dispatch-n (r x.core/get-period-events db :on-server-init)
       ; 2. Az inicializálási események lefutása után a szerver betöltésének folytatása
       :dispatch-tick [{:tick 250 :dispatch [:boot-loader/boot-server! server-props]}]}))

(r/reg-event-fx :boot-loader/boot-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  (fn [{:keys [db]} [_ server-props]]
      (println x.details/app-codename "booting server ...")
      {; 1. Az indítási események meghívása
       ;    (Dispatch on-server-boot events)
       :dispatch-n    (r x.core/get-period-events db :on-server-boot)
       :dispatch-tick [; 2. A szerver indítása
                       {:tick  50 :dispatch [:boot-loader/run-server! server-props]}
                       ; 4. Az indítási események lefutása után a szerver betöltésének folytatása
                       {:tick 100 :dispatch [:boot-loader/launch-server! server-props]}]}))

(r/reg-event-fx :boot-loader/run-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  (fn [{:keys [db]} [_ server-props]]
      {:fx [:core/run-server! server-props]}))

(r/reg-event-fx :boot-loader/launch-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  (fn [{:keys [db]} [_ server-props]]
      (println x.details/app-codename "launching server ...")
      {; A szerver indítása utáni események meghívása
       ; (Dispatch on-server-launch events)
       :dispatch-n (r x.core/get-period-events db :on-server-launch)
       :dispatch-tick [{:tick 100 :dispatch [:boot-loader/server-launched server-props]}]}))

(r/reg-event-fx :boot-loader/server-launched
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  (fn [{:keys [db]} _]
      (println x.details/app-codename "server launched")))
