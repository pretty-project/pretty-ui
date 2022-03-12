
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.lifecycle-handler.side-effects
    (:require [x.mid-core.event-handler            :as event-handler :refer [r]]
              [x.mid-core.lifecycle-handler.engine :as lifecycle-handler.engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-lifecycles!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  ; - A reg-lifecycles! függvény az életciklusok adatait fordítás-időben a LIFES atomban tárolja.
  ; - Az életciklusok adatait a boot-loader a {:core/import-lifecycles! nil} mellékhatás-esemény
  ;   meghívásával másolja a LIFES atomból a Re-Frame adatbázisba.
  (event-handler/dispatch [:db/set-item! [:core :lifecycle-handler/data-items] @lifecycle-handler.engine/LIFES]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-lifecycles!
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
  ;  (reg-lifecycles!
  ;   :namespace/lifecycles
  ;   {...}
  ([lifecycles]
   (reg-lifecycles! (lifecycle-handler.engine/generate-life-id) lifecycles))

  ([life-id lifecycles]

   ; - Az x4.5.1 verzióig a reg-lifecycles! függvény az életciklusok adatait közvetlenül
   ;   (reset! függvény használatával) írta a Re-Frame adatbázisba.
   ; - Ha a forráskódban fordításidőben meghívott adatbázis események is írnak a Re-Frame
   ;   adatbázisba, akkor a reset! függvénnyel írt adatbázis-változások nem minden esetben
   ;   maradnak meg, mivel a reset! függvény nem szinkronizált a Re-Frame event-queue időzítővel,
   ;   ami a még reset! függvény végrehajtódása ELŐTT kiolvashatja az adatbázis tartalmát, értelmezve
   ;   rajta az adatbázis-esemény által okozott változásokat, majd pedig a reset! f. végrehajtódása
   ;   UTÁN elmenteti a megváltozott adatbázist az atomba, amiből így kimarad a reset! függvény
   ;   által beleírt változás.
   ; - A Re-Frame adatbázis atomot NEM szabad reset! függvénnyel írni, mert az esetlegesen
   ;   az írással egy időben megtörténő adatbázis eseményekkel való konkurálás következtében egyes
   ;   változások elveszhetnek!
   (let [namespace (lifecycle-handler.engine/life-id->namespace life-id)]
        (letfn [(f [lifecycles period-id event]
                   (let [event-id (keyword namespace (name period-id))]
                        (event-handler/reg-event-fx event-id event)
                        (assoc lifecycles period-id [event-id])))]
               (swap! lifecycle-handler.engine/LIFES assoc life-id (reduce-kv f {} lifecycles))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-fx :core/import-lifecycles! import-lifecycles!)
