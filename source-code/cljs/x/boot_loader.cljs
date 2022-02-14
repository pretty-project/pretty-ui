
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.14
; Description:
; Version: v2.0.4
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader
    (:require [x.app-components.api]
              [x.app-developer.api]
              [x.app-dictionary.api]
              [x.app-environment.api]
              [x.app-locales.api]
              [x.app-media.api]
              [x.app-sync.api]
              [x.app-tools.api]
              [x.app-views.api]
              [app-fruits.dom     :as dom]
              [app-fruits.reagent :as reagent]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.map     :as map]
              [mid-fruits.string  :as string]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-router.api   :as router]
              [x.app-ui.api       :as ui]
              [x.app-user.api     :as user]))



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  Az applikáció betöltésének folyamata:
;  - Az [core/synchronize-app! ...] esemény az applikáció indításához szükséges
;    adatokat letölti a szerverről.
;  - Az applikáció indításához szükséges adatok letöltődése után az :on-app-init
;    események megtörténése.
;  - Az :on-app-init események megtörténésének kezdete után 100 ms idő elteltével
;    az :on-app-boot események megtörténése.
;  - Az :on-app-boot események megtörténésének kezdete után 100 ms idő elteltével
;    az applikáció felépítése.
;  - Azonosított felhasználó esetén a bejelentkezési események megtörténése.
;  - Applikáció tartalmának renderelése.
;  - Az applikáció tartalmának renderelése után 100 ms idő elteltével
;    az {:on-app-launch ...} események megtörténése.



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-app!
  ; @param (component) app
  ;
  ; @usage
  ;  (defn app [ui-structure] [:div#my-wrapper [ui-structure]])
  ;  (x.boot-loader/start-app! #'app)
  [app]
  (a/dispatch-sync [:boot-loader/start-app! app]))

(defn render-app!
  ; @param (component) app
  ;
  ; @usage
  ;  (defn app [ui-structure] [:div#my-wrapper [ui-structure]])
  ;  (x.boot-loader/render-app! #'app)
  [app]
  (reagent/render [app #'ui/structure]
                  (dom/get-element-by-id "x-app-container")))

; @usage
;  {:boot-loader/render-app! #'app}
(a/reg-fx_ :boot-loader/render-app! render-app!)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-restart-target
  ; @return (string)
  [db _]
  (if-let [restart-target (get-in db (db/path ::primary :restart-target))]
          (return restart-target)
          (r a/get-app-config-item db :app-home)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-restart-target!
  ; @param (string) restart-target
  ;
  ; @usage
  ;  (r set-restart-target! db "/my-route?var=value")
  ;
  ; @return (map)
  [db [_ restart-target]]
  (assoc-in db (db/path ::primary :restart-target)
               (param restart-target)))

; @usage
;  [:boot-loader/set-restart-target! "/my-route?var=value"]
(a/reg-event-db :boot-loader/set-restart-target! set-restart-target!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :boot-loader/refresh-app!
  ; Az aktuális route-ot újraindítás utáni útvonalként használva, elindítja
  ; az [:boot-loader/restart-app! ...] eseményt.
  (fn [{:keys [db]} _]
      (let [current-route-string (r router/get-current-route-string db)]
           [:boot-loader/restart-app! {:restart-target current-route-string}])))

(a/reg-event-fx
  :boot-loader/restart-app!
  ; @param (map)(opt) context-props
  ;  {:restart-target (string)(opt)}
  ;
  ; @usage
  ;  [:boot-loader/restart-app!]
  ;
  ; @usage
  ;  [:boot-loader/restart-app! {:restart-target "/my-route?var=value"}]
  (fn [{:keys [db]} [_ {:keys [restart-target]}]]
      {:environment/go-to! (or restart-target (r get-restart-target db))}))

(a/reg-event-fx
  :boot-loader/start-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:boot-loader/initialize-app! #'app]
  (fn [_ [_ app]]
      {:core/import-lifecycles! nil
       :core/detect-debug-mode! nil
       :dispatch-n [; 1. Let's start!
                    [:core/synchronize-app! app]
                    ; 2. A load-handler várjon az XXX#5030 jelre!
                    [:core/synchronize-loading! :boot-loader/build-app!]]}))

(a/reg-event-fx
  :boot-loader/initialize-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:boot-loader/initialize-app! #'app]
  (fn [{:keys [db]} [_ app]]
      {; 1. Az inicializálási események meghívása (Dispatch on-app-init events)
       :dispatch-n (r a/get-period-events db :on-app-init)
       ; 2. Az inicializálási események lefutása után az applikáció
       ;    betöltésének folytatása
       :dispatch-later [{:ms 100 :dispatch [:boot-loader/boot-app! app]}]}))

(a/reg-event-fx
  :boot-loader/boot-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:boot-loader/boot-app! #'app]
  (fn [{:keys [db]} [_ app]]
       ; 1. Az indítási események meghívása (Dispatch on-app-boot events)
      {:dispatch-n (r a/get-period-events db :on-app-boot)
       ; 2. Az indítási események lefutása után az applikáció
       ;    betöltésének folytatása
       :dispatch-later [{:ms 100 :dispatch [:boot-loader/build-app! app]}]}))

(a/reg-event-fx
  :boot-loader/build-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:boot-loader/build-app! #'app]
  (fn [{:keys [db]} [_ app]]
      {; 1. Az applikáció renderelése
       :boot-loader/render-app! app
       ; 2. Ha a felhasználó nem vendégként lett azonosítva, akkor
       ;    a bejelentkezési események meghívása (Dispatch on-login events)
       :dispatch-if [(r user/user-identified? db) [:core/login-app!]]
       ; ...
       :dispatch-later
       [; 3. Az applikáció renderelése utáni események meghívása
        {:ms 100 :dispatch [:boot-loader/launch-app!]}
        ; 4. Curtains up!
        ; XXX#5030
        {:ms 500 :dispatch [:core/synchron-signal :boot-loader/build-app!]}]}))

(a/reg-event-fx
  :boot-loader/launch-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {; Az útvonalhoz tartozó esemény meghívása
       ; (Dispatch the current route-event)
       :router/dispatch-current-route! nil
       ; Az applikáció renderelése utáni események meghívása
       ; (Dispatch on-app-launch events)
       :dispatch-n (r a/get-period-events db :on-app-launch)}))

(a/reg-event-fx
  :boot-loader/app-synchronized
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ; @param (map) server-response
  ;
  ; @usage
  ;  [:boot-loader/app-synchronized #'app {...}]
  (fn [{:keys [db] :as cofx} [_ app server-response]]
      (let [app-build (r a/get-app-config-item db :app-build)]
           {:dispatch-n [; 1.
                         [:environment/set-cookie! :x-app-build {:value app-build}]
                         ; 2.
                         [:boot-loader/initialize-app! app]]})))
