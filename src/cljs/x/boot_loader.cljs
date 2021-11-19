
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.14
; Description:
; Version: v1.7.2
; Compatibility: x4.4.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader
    (:require [x.app-components.api]
              [x.app-developer.api]
              [x.app-dictionary.api]
              [x.app-environment.api]
              [x.app-locales.api]
              [x.app-log.api]
              [x.app-media.api]
              [x.app-sync.api]
              [x.app-tools.api]
              [x.app-views.api]
              [x.boot-synchronizer]
              [app-fruits.dom     :as dom]
              [app-fruits.reagent :as reagent]
              [mid-fruits.candy   :refer [param return]]
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
;  - Az [x.boot-synchronizer/synchronize-app! ...] esemény az applikáció indításához
;    szükséges adatokat letölti a szerverről.
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



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-RESTART-TARGET "/")

; @constant (ms)
(def RESTART-TIMEOUT 1000)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn boot-app!
  ; @param (component) app
  ;
  ; @usage
  ;  (defn app [ui-structure] [:div#my-wrapper [ui-structure]])
  ;  (x.boot-loader/boot-app! #'app)
  [app]
  ; 1. Let's start!
  (a/dispatch-sync [:x.boot-synchronizer/synchronize-app! app])
  ; 2. A load-handler várjon az XXX#5030 jelre!
  (a/dispatch-sync [:x.app-core/synchronize-loading! :x.boot-loader/build-app!]))

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
;  [:x.boot-loader/render-app! #'app]
(a/reg-handled-fx :x.boot-loader/render-app! render-app!)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-restart-target
  ; @return (string)
  [db _]
  (if-let [restart-target (get-in db (db/path ::primary :restart-target))]
          (return restart-target)
          (r a/get-app-detail db :app-home)))



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
;  [:x.boot-loader/set-restart-target! "/my-route?var=value"]
(a/reg-event-db :x.boot-loader/set-restart-target! set-restart-target!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.boot-loader/refresh-app!
  ; Az aktuális route-ot újraindítás utáni útvonalként használva, elindítja
  ; az [:x.boot-loader/restart-app!] eseményt.
  (fn [{:keys [db]} _]
      (let [current-route-string (r router/get-current-route-string db)]
           [:x.boot-loader/restart-app! {:restart-target current-route-string}])))

(a/reg-event-fx
  :x.boot-loader/restart-app!
  ; A {:restart-target ...} tulajdonságként átadott újraindítás utáni útvonalat
  ; eltárolja majd átirányít a "/reboot" útvonalra.
  ;
  ; @param (map)(opt) context-props
  ;  {:restart-target (string)(opt)}
  ;
  ; @usage
  ;  [:x.boot-loader/restart-app!]
  ;
  ; @usage
  ;  [:x.boot-loader/restart-app! {:restart-target "/my-route?var=value"}]
  (fn [{:keys [db]} [_ {:keys [restart-target]}]]
      (if (string/nonempty? restart-target)
          {:db (r set-restart-target! db restart-target)
           :dispatch [:x.app-router/go-to! "/reboot"]}
          {:dispatch [:x.app-router/go-to! "/reboot"]})))

(a/reg-event-fx
  :x.boot-loader/reboot-app!
  (fn [{:keys [db]} _]
      (let [restart-target (r get-restart-target db)]
           {:dispatch-later [{:ms RESTART-TIMEOUT :dispatch
                              [:x.app-environment.window-handler/go-to! restart-target]}]})))

(a/reg-event-fx
  :x.boot-loader/initialize-app!
  [a/self-destruct!]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:x.boot-loader/initialize-app! #'app]
  (fn [{:keys [db]} [_ app]]
       ; 1. Debug módban indított applikáció bejegyzése
      {:db (if-let [debug-mode (a/debug-mode)]
                   (r a/store-debug-mode! db debug-mode))
       ; 2. Az inicializálási események meghívása (Dispatch on-app-init events)
       :dispatch-n (r a/get-period-events db :on-app-init)
       ; 3. Az inicializálási események lefutása után az applikáció
       ;    betöltésének folytatása
       :dispatch-later [{:ms 100 :dispatch [:x.boot-loader/boot-app! app]}]}))

(a/reg-event-fx
  :x.boot-loader/boot-app!
  [a/self-destruct!]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:x.boot-loader/boot-app! #'app]
  (fn [{:keys [db]} [_ app]]
       ; 1. Az indítási események meghívása (Dispatch on-app-boot events)
      {:dispatch-n (r a/get-period-events db :on-app-boot)
       ; 2. Az indítási események lefutása után az applikáció
       ;    betöltésének folytatása
       :dispatch-later [{:ms 100 :dispatch [:x.boot-loader/build-app! app]}]}))

(a/reg-event-fx
  :x.boot-loader/build-app!
  [a/self-destruct!]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:x.boot-loader/build-app! #'app]
  (fn [{:keys [db]} [_ app]]
      {:dispatch-if
       ; 1. Ha a felhasználó nem vendégként lett azonosítva, akkor
       ;    a bejelentkezési események meghívása (Dispatch on-login events)
       [(r user/user-identified? db) [:x.app-core/login-app!]]
       ;
       :dispatch-later
       [; 2. Az applikáció renderelése
        {:ms   0 :dispatch [:x.boot-loader/render-app! app]}
        ; 3. Az applikáció renderelése utáni események meghívása
        {:ms 100 :dispatch [:x.boot-loader/launch-app!]}
        ; 4. Curtains up!
        ; XXX#5030
        {:ms 500 :dispatch [:x.app-core/->synchron-signal :x.boot-loader/build-app!]}]}))

(a/reg-event-fx
  :x.boot-loader/launch-app!
  [a/self-destruct!]
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {; Az útvonalhoz tartozó esemény meghívása
       ; (Dispatch the current route-event)
       :dispatch [:x.app-router/dispatch-current-route!]
       ; Az applikáció renderelése utáni események meghívása
       ; (Dispatch on-app-launch events)
       :dispatch-n (r a/get-period-events db :on-app-launch)}))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.boot-loader/->app-synchronized
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:x.boot-loader/->app-synchronized #'app]
  (fn [{:keys [db]} [_ app]]
      (let [app-build (r a/get-app-detail db :app-build)]
                         ; 1.
           {:dispatch-n [[:x.app-environment.cookie-handler/set-cookie! :x-app-build {:value app-build}]
                         ; 2.
                         [:x.boot-loader/initialize-app! app]]})))
