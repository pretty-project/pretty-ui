
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
              [x.core.api         :as x.core]
              [x.router.api       :as x.router]
              [x.user.api         :as x.user]
              [x.boot-loader.subs :as subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.boot-loader/refresh-app!
  ; Az aktuális route-ot újraindítás utáni útvonalként használva, elindítja a [:x.boot-loader/restart-app! ...] eseményt.
  (fn [{:keys [db]} _]
      (let [current-route-string (r x.router/get-current-route-string db)]
           [:x.boot-loader/restart-app! {:restart-target current-route-string}])))

(r/reg-event-fx :x.boot-loader/restart-app!
  ; @param (map)(opt) context-props
  ;  {:restart-target (string)(opt)}
  ;
  ; @usage
  ;  [:x.boot-loader/restart-app!]
  ;
  ; @usage
  ;  [:x.boot-loader/restart-app! {:restart-target "/my-route?var=value"}]
  (fn [{:keys [db]} [_ {:keys [restart-target]}]]
      {:fx [:x.environment/go-to! (or restart-target (r subs/get-restart-target db))]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.boot-loader/start-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:x.boot-loader/start-app! #'app]
  (fn [_ [_ app]]
      {:fx-n [[:x.core/import-lifecycles!]
              [:x.core/detect-debug-mode!]]
       :dispatch-n [; 1. Let's start!
                    [:x.core/synchronize-app! app]
                    ; 2. A load-handler várjon az :x.boot-loader/build-app! jelre!
                    [:x.core/start-synchron-signal! :x.boot-loader/build-app!]]}))

(r/reg-event-fx :x.boot-loader/init-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:x.boot-loader/init-app! #'app]
  (fn [{:keys [db]} [_ app]]
      {; 1. Az inicializálási események meghívása
       ;    (dispatch on-app-init events)
       :dispatch-n (r x.core/get-period-events db :on-app-init)
       ; 2. Az inicializálási események lefutása után az applikáció betöltésének folytatása
       :dispatch-later [{:ms 100 :dispatch [:x.boot-loader/boot-app! app]}]}))

(r/reg-event-fx :x.boot-loader/boot-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:x.boot-loader/boot-app! #'app]
  (fn [{:keys [db]} [_ app]]
      {; 1. Az aktuális útvonal tulajdonságainak eltárolása
       ;    (szükséges elérhetővé tenni az indítási folyamat eseményei számára)
       :fx [:x.router/read-current-route!]
       ; 2. Az indítási események meghívása
       ;    (dispatch on-app-boot events)
       :dispatch-n (r x.core/get-period-events db :on-app-boot)
       ; 3. Az indítási események lefutása után az applikáció betöltésének folytatása
       :dispatch-later [{:ms 100 :dispatch [:x.boot-loader/build-app! app]}]}))

(r/reg-event-fx :x.boot-loader/build-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:x.boot-loader/build-app! #'app]
  (fn [{:keys [db]} [_ app]]
      {; 1. Az applikáció renderelése
       :fx [:x.boot-loader/render-app! app]
       ; 2. Ha a felhasználó nem vendégként lett azonosítva, akkor a bejelentkezési események meghívása
       ;    (dispatch on-login events)
       :dispatch-if [(r x.user/user-identified? db) [:x.core/login-app!]]
       ; ...
       :dispatch-later
       [; 3. Az applikáció renderelése utáni események meghívása
        {:ms 100 :dispatch [:x.boot-loader/launch-app!]}
        ; 4. Curtains up!
        {:ms 500 :dispatch [:x.core/end-synchron-signal! :x.boot-loader/build-app!]}]}))

(r/reg-event-fx :x.boot-loader/launch-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {; 1. Az útvonalhoz tartozó esemény meghívása
       ;    (dispatch the current route-event)
       :dispatch [:x.router/dispatch-current-route!]
       ; 2. Az applikáció renderelése utáni események meghívása
       ;    (dispatch on-app-launch events)
       :dispatch-n (r x.core/get-period-events db :on-app-launch)}))

(r/reg-event-fx :x.boot-loader/app-synchronized
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ; @param (map) server-response
  ;
  ; @usage
  ;  [:x.boot-loader/app-synchronized #'app {...}]
  (fn [{:keys [db] :as cofx} [_ app server-response]]
      (let [app-build (r x.core/get-app-config-item db :app-build)]
           {:dispatch-n [; 1.
                         [:x.environment/set-cookie! :x-app-build {:value app-build}]
                         ; 2.
                         [:x.boot-loader/init-app! app]]})))
