
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.23
; Description:
; Version: v2.0.6
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.lifecycle-handler
    (:require [mid-fruits.candy         :refer [param return]]
              [mid-fruits.random        :as random]
              [x.mid-core.event-handler :as event-handler :refer [r]]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name on-server-init
;  Az on-server-boot előtt lefutó események.
;
; @name on-server-boot
;  A szerver indítása előtt lefutó események.
;
; @name on-server-launch
;  A szerver indítása után lefutó események.
;
; @name on-app-init
;  Az on-app-boot előtt lefutó események.
;
; @name on-app-boot
;  Az applikáció első renderelése előtt lefutó események.
;
; @name on-app-launch
;  Az applikáció első renderelése után lefutó események.
;
; @name on-login
;  (Nem vendég) bejelentkezéskor lefutó események.
;
; @name on-browser-online
;  Nomen est omen.
;
; @name on-browser-offline
;  Nomen est omen.



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(def LIFES (atom {}))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- generate-life-id
  ; @return (namespaced keyword)
  []
  (keyword (random/generate-string) "lifecycles"))

(defn- life-id->namespace
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword or namespaced keyword) life-id
  ;
  ; @return (string)
  [life-id]
  (if-let [namespace (namespace life-id)]
          (return namespace)
          (random/generate-string)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-lifes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db [::lifes :data-items]))

(defn get-period-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) period-id
  ;
  ; @return (vector)
  [db [_ period-id]]
  (letfn [(f [o dex x] (if-let [period (get x period-id)]
                               (conj   o period)
                               (return o)))]
         (reduce-kv f [] (r get-lifes db))))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- import-lifecycles!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  ; - A reg-lifecycles függvény az életciklusok adatait fordítás-időben a LIFES atomban tárolja.
  ; - Az életciklusok adatait a boot-loader a {:core/import-lifecycles! nil} mellékhatás-esemény
  ;   meghívásával másolja a LIFES atomból a Re-Frame adatbázisba.
  (event-handler/dispatch [:db/set-item! [::lifes :data-items] @LIFES]))

(event-handler/reg-fx :core/import-lifecycles! import-lifecycles!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-lifecycles
  ; @param (namespaced keyword)(opt) life-id
  ; @param (map) lifecycles
  ;  {:on-app-init        (metamorphic-event)(opt)
  ;   :on-app-boot        (metamorphic-event)(opt)
  ;   :on-app-launch      (metamorphic-event)(opt)
  ;   :on-browser-offline (metamorphic-event)(opt)
  ;   :on-browser-online  (metamorphic-event)(opt)
  ;   :on-login           (metamorphic-event)(opt)
  ;   :on-server-boot     (metamorphic-event)(opt)
  ;   :on-server-init     (metamorphic-event)(opt)
  ;   :on-server-launch   (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  (reg-lifecycles
  ;   :namespace/lifecycles
  ;   {...}
  ([lifecycles]
   (reg-lifecycles (generate-life-id) lifecycles))

  ([life-id lifecycles]
   ; DEBUG
;   (println (str (count (keys @LIFES)))
;            (str life-id)]

   ; - Az x4.5.1 verzióig a reg-lifecycles függvény az életciklusok adatait közvetlenül
   ;   (reset! függvény használatával) írta a Re-Frame adatbázisba.
   ; - Ha a forráskódban fordításidőben meghívott adatbázis események is írtak a Re-Frame
   ;   adatbázisba, akkor a reset! függvénnyel írt adatbázis-változások nem minden esetben
   ;   maradtak meg, mivel a reset! függvény nem szinkronizált a Re-Frame event-queue időzítővel,
   ;   ami a még reset! függvény végrehajtódása ELŐTT kiolvasta az adatbázis tartalmát, értelmezte
   ;   rajta az adatbázis-esemény által okozott változásokat, majd pedig a reset! f. végrehajtódása
   ;   UTÁN elmentette a megváltozott adatbázist az atomba, amiből így kimaradt a reset! függvény
   ;   által beleírt változás.
   ; - A Re-Frame adatbázis atomba NEM szabad reset! függvénnyel írni, mert az esetlegesen
   ;   az írással egy időben megtörténő adatbázis eseményekkel való konkurálás következtében egyes
   ;   változások elveszhetnek!
   (let [namespace (life-id->namespace life-id)]
        (letfn [(f [lifecycles period-id event]
                  (let [event-id (keyword namespace (name period-id))]
                       (event-handler/reg-event-fx event-id event)
                       (assoc lifecycles period-id [event-id])))]
               (swap! LIFES assoc life-id (reduce-kv f {} lifecycles))))))
