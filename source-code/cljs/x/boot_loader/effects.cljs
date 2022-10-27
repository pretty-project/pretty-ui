
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.effects
    (:require [re-frame.api       :as r :refer [r]]
              [x.app-core.api     :as core]
              [x.app-router.api   :as router]
              [x.app-user.api     :as user]
              [x.boot-loader.subs :as subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :boot-loader/refresh-app!
  ; Az aktuális route-ot újraindítás utáni útvonalként használva, elindítja a [:boot-loader/restart-app! ...] eseményt.
  (fn [{:keys [db]} _]
      (let [current-route-string (r router/get-current-route-string db)]
           [:boot-loader/restart-app! {:restart-target current-route-string}])))

(r/reg-event-fx :boot-loader/restart-app!
  ; @param (map)(opt) context-props
  ;  {:restart-target (string)(opt)}
  ;
  ; @usage
  ;  [:boot-loader/restart-app!]
  ;
  ; @usage
  ;  [:boot-loader/restart-app! {:restart-target "/my-route?var=value"}]
  (fn [{:keys [db]} [_ {:keys [restart-target]}]]
      {:fx [:environment/go-to! (or restart-target (r subs/get-restart-target db))]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :boot-loader/start-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:boot-loader/start-app! #'app]
  (fn [_ [_ app]]
      {:fx-n [[:core/import-lifecycles!]
              [:core/detect-debug-mode!]]
       :dispatch-n [; 1. Let's start!
                    [:core/synchronize-app! app]
                    ; 2. A load-handler várjon az :boot-loader/build-app! jelre!
                    [:core/start-synchron-signal! :boot-loader/build-app!]]}))

(r/reg-event-fx :boot-loader/init-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:boot-loader/init-app! #'app]
  (fn [{:keys [db]} [_ app]]
      {; 1. Az inicializálási események meghívása
       ;    (Dispatch on-app-init events)
       :dispatch-n (r core/get-period-events db :on-app-init)
       ; 2. Az inicializálási események lefutása után az applikáció betöltésének folytatása
       :dispatch-later [{:ms 100 :dispatch [:boot-loader/boot-app! app]}]}))

(r/reg-event-fx :boot-loader/boot-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:boot-loader/boot-app! #'app]
  (fn [{:keys [db]} [_ app]]
       ; 1. Az indítási események meghívása
       ;    (Dispatch on-app-boot events)
      {:dispatch-n (r core/get-period-events db :on-app-boot)
       ; 2. Az indítási események lefutása után az applikáció betöltésének folytatása
       :dispatch-later [{:ms 100 :dispatch [:boot-loader/build-app! app]}]}))

(r/reg-event-fx :boot-loader/build-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:boot-loader/build-app! #'app]
  (fn [{:keys [db]} [_ app]]
      {; 1. Az applikáció renderelése
       :fx [:boot-loader/render-app! app]
       ; 2. Ha a felhasználó nem vendégként lett azonosítva, akkor a bejelentkezési események meghívása
       ;    (Dispatch on-login events)
       :dispatch-if [(r user/user-identified? db) [:core/login-app!]]
       ; ...
       :dispatch-later
       [; 3. Az applikáció renderelése utáni események meghívása
        {:ms 100 :dispatch [:boot-loader/launch-app!]}
        ; 4. Curtains up!
        {:ms 500 :dispatch [:core/end-synchron-signal! :boot-loader/build-app!]}]}))

(r/reg-event-fx :boot-loader/launch-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {; 1. Az útvonalhoz tartozó esemény meghívása
       ;    (Dispatch the current route-event)
       :dispatch [:router/dispatch-current-route!]
       ; 2. Az applikáció renderelése utáni események meghívása
       ;    (Dispatch on-app-launch events)
       :dispatch-n (r core/get-period-events db :on-app-launch)}))

(r/reg-event-fx :boot-loader/app-synchronized
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ; @param (map) server-response
  ;
  ; @usage
  ;  [:boot-loader/app-synchronized #'app {...}]
  (fn [{:keys [db] :as cofx} [_ app server-response]]
      (let [app-build (r core/get-app-config-item db :app-build)]
           {:dispatch-n [; 1.
                         [:environment/set-cookie! :x-app-build {:value app-build}]
                         ; 2.
                         [:boot-loader/init-app! app]]})))
