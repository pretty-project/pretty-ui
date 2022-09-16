
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.10
; Description:
; Version: v2.6.0
; Compatibility: x4.5.7



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.stated
    (:require [mid-fruits.candy             :refer [param return]]
              [mid-fruits.map               :refer [dissoc-in]]
              [mid-fruits.random            :as random]
              [reagent.api                  :as reagent]
              [x.app-components.engine      :as engine]
              [x.app-components.subscriber  :rename {component subscriber}]
              [x.app-components.transmitter :rename {component transmitter}]
              [x.app-core.api               :as a :refer [r]]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name stated
; - A stated komponens a Reagent életciklusait valósítja meg a {:render-f ...}
;   tulajdonságként átadott komponensként meghívott render-függvény számára.
; + A Shadow CLJS általi újrarendereléskor megakadályozza az életciklusok megtörténését.
;
; @name destructor
;  - A {:destructor ...} tulajdonságként átadott Re-Frame esemény a komponens
;    React-fából történő lecsatolása után történik meg. A komponens újracsatolásakor (remounting)
;    megtörténő pillanatnyi lecsatolás esetén nem történik meg a destructor esemény.
;  - Mivel a destructor esemény megtörténésekor a Re-Frame adatbázis már nem tartalmazza
;    az initial-props-path Re-Frame adatbázis útvonalon tárolt értéket, így azt a destructor
;    esemény utolsó paraméterként kapja meg.
;
; @name initializer
;  Az {:initializer ...} tulajdonságként átadott Re-Frame esemény a komponens
;  React-fába csatolása után történik meg. A komponens újracsatolásakor
;  (remounting) nem ismétlődik meg az initializer esemény megtörténése.
;
; @name updater
;  Az {:updater [...]} tulajdonságként átadott Re-Frame esemény a komponens
;  paramétereinek megváltozása után történik meg.
;
; @name initial-props-path
;  Az {:initial-props-path [...]} tulajdonságként átadott Re-Frame adatbázis
;  útvonal megadásával határozható meg a komponens React-fába csatolásakor az
;  {:initial-props {...}} tulajdonságként átadott térkép elérési útvonala.
;
; @name initial-props
;  XXX#0001
;
; @name base-props
;  XXX#0001
;
; @name modifier
;  XXX#0001
;
; @name subscriber
;  XXX#7081



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  XXX#8099
;  A :component-did-update életciklus nem kapja meg a komponens új paramétereit,
;  ezért a stated komponens nem képes a komponens megváltozott initial-props
;  paramétereit a Re-Frame adatbázisban felülírni!
;
; @description
;  Az egyes stated komponensek React-fába újracsatolódása (remounting)
;  esetlegesen hibát okozhat, ha a destructor esemény megtörténése vagy a komponens
;  tulajdonságainak Re-Frame adatbázisból való eltávolítása nem a megfelelő időben
;  történik meg.
;
; @description
;  A remounting esemény egy egymás után megtörténő unmount majd egy mount eseményekből áll.
;
; @description
;  A stated komponens első verzióiban a Shadow CLJS remounting felismerése úgy működött,
;  hogy az unmount esemény időbeni eltolásával az a (re)mount esemény után történt meg,
;  így meg lehetett vizsgálni, hogy ha egy mounted komponensen (re)mount esemény
;  történik majd az ahhoz tartozó – eredetileg a (re)mount esemény előtt megtörténő –
;  unmount esemény történik, akkor az remounting eseménypárnak számít.
;  Ebből az következett, hogy bizonyos esetekben, ha gyors egymás utánban történt kettő
;  vagy több remounting, akkor az unmount-(re)mount eseménypárok néha időbeni átfedésbe kerültek
;  egymással és ez hibákhoz vezetett.
;  Ennek kiküszöbölésére került bevezetésre, hogy minden React-fába történe komponens-
;  -csatolás saját azonosítóval rendelkezik (mount-id) és a destructor esemény megtörténése
;  vagy a komponens tulajdonságainak Re-Frame adatbázisból való eltávolítása késleltetve
;  történik, így lehetséges megvizsgálni, hogy ha a komponens még mindig azzal az azonosítóval
;  van csatolva, amivel a lecsatolásai események megtörténnének, akkor azok végrehajtódhatnak,
;  de ha azóta eltérő azonosítóval újra van csatolva a komponens, akkor a lecsatolási események
;  nem történnek meg.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def COMPONENT-DESTRUCTION-DELAY 50)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-component-current-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:initial-props-path (vector)(opt)}
  ;
  ; @return (map)
  [db [_ component-id {:keys [initial-props-path]}]]
  (if initial-props-path (get-in db initial-props-path)))

(defn- component-mounted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (boolean)
  [db [_ component-id]]
  (let [component-status (r engine/get-component-prop db component-id :status)]
       (or (= component-status :mounted)
           (= component-status :remounting)
           (= component-status :remounted)
           (= component-status :updated))))

(defn- component-unmounted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (boolean)
  [db [_ component-id]]
  (let [component-mounted? (r component-mounted? db component-id)]
       (not component-mounted?)))

(defn- component-remounting?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (boolean)
  [db [_ component-id]]
  (let [component-status (r engine/get-component-prop db component-id :status)]
       (= component-status :remounting)))

(defn- component-mounted-as?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (keyword) mount-id
  ;
  ; @return (boolean)
  [db [_ component-id mount-id]]
  (let [mounted-as (r engine/get-component-prop db component-id :mount-id)]
       (= mounted-as mount-id)))

(defn- component-initialized?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (boolean)
  [db [_ component-id]]
  (let [component-initialized? (r engine/get-component-prop db component-id :initialized?)]
       (boolean component-initialized?)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-component-initial-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:initial-props (map)
  ;   :initial-props-path (vector)(opt)}
  ;
  ; @return (map)
  [db [_ component-id {:keys [initial-props initial-props-path]}]]
  (if initial-props-path (assoc-in db initial-props-path initial-props)
                         (return   db)))

(defn- remove-component-initial-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:initial-props-path (vector)(opt)}
  ;
  ; @return (map)
  [db [_ component-id {:keys [initial-props-path]}]]
  (if initial-props-path (dissoc-in db initial-props-path)
                         (return    db)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :components/initialize-component!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ; @param (keyword) mount-id
  (fn [_ _]))
  ; Az x4.4.8 verzióig a komponensek inicializálása a [:components/initialize-component! ...]
  ; eseményben történt, amely esemény a komponensek gyorsabb felépülése érdekében összevonásra
  ; került a [:components/component-mounted ...] eseménnyel.

(a/reg-event-fx
  :components/destruct-component!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:destructor (metamorphic-event)(opt)}
  ; @param (keyword) mount-id
  (fn [{:keys [db]} [_ component-id {:keys [destructor] :as context-props} mount-id]]
      (if (and (r component-unmounted?  db component-id)
               (r component-mounted-as? db component-id mount-id))
          {:db       (as-> db % (r engine/remove-component-props!  % component-id)
                                (r remove-component-initial-props! % component-id context-props))
           :dispatch (if-let [current-props (r get-component-current-props db component-id context-props)]
                             ; If current-props is NOT nil ...
                             (a/metamorphic-event<-params destructor current-props)
                             ; If current-props is nil ...
                             (param destructor))})))

(a/reg-event-fx
  :components/component-mounted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:initializer (metamorphic-event)(opt)}
  ; @param (keyword) mount-id
  (fn [{:keys [db]} [_ component-id {:keys [initializer] :as context-props} mount-id]]
      (if-not (r component-initialized? db component-id)
              ; If component is NOT initialized ...
              {:db (as-> db % (r engine/set-component-prop!     % component-id :status :mounted)
                              (r engine/set-component-prop!     % component-id :mount-id mount-id)
                              (r engine/set-component-prop!     % component-id :initialized? true)
                              (r store-component-initial-props! % component-id context-props))
               :dispatch initializer}
              ; If component is initialized ...
              {:db (r engine/set-component-prop! db component-id :mount-id mount-id)})))

(a/reg-event-fx
  :components/component-updated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:updater (metamorphic-event)(opt)}
  ; @param (keyword) mount-id
  (fn [{:keys [db]} [_ component-id {:keys [updater] :as context-props} _]]
      ; WARNING! DEPRECATED! DO NOT USE!
      ; - A komponens tulajdonságait :component-did-update eseménykor nem lehetséges aktualizálni
      ;   a Re-Frame adatbázisban, kevés értelme van a [:components/component-updated ...]
      ;   eseményt alkalmazni.
      ; - A reagent komponensek paramétereinek megváltozásakor a Re-Frame adatbázisba írás
      ;   negatívan befolyásolja az applikáció teljesítményét!
      {;:db (r engine/set-component-prop! db component-id :status :updated)
       :dispatch updater}))

(a/reg-event-fx
  :components/component-unmounted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ; @param (keyword) mount-id
  (fn [{:keys [db]} [_ component-id context-props mount-id]]
      {:db             (r engine/set-component-prop! db component-id :status :unmounted)
       :dispatch-later [{:ms COMPONENT-DESTRUCTION-DELAY
                         :dispatch [:components/destruct-component! component-id context-props mount-id]}]}))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- non-subscriber
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  [component-id context-props]
  [transmitter component-id context-props])

(defn- subscribe-controller
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:subscriber (subscription-vector)(opt)}
  [component-id context-props]
  (if (:subscriber context-props) [subscriber     component-id context-props]
                                  [non-subscriber component-id context-props]))

(defn- lifecycle-controller
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  [component-id context-props]
  (let [mount-id (random/generate-keyword)]
       (reagent/lifecycles {:component-did-mount    #(a/dispatch [:components/component-mounted   component-id context-props mount-id])
                           ; WARNING! DEPRECATED! DO NOT USE!
                            ;:component-did-update   #(a/dispatch [:components/component-updated   component-id context-props mount-id])
                            ;:component-did-update (fn [this _] ;(println (str "dd")))
                            ;                          (let [new  (reagent.core/argv this)]
                            ;                               (println (str new)))
                            ;          (fn [this old-argv]        ;; reagent provides you the entire "argv", not just the "props"
                            ;            (let [new-argv (rest (reagent/argv this))]
                            ;              (do-something new-argv old-argv)]))
                            :component-will-unmount #(a/dispatch [:components/component-unmounted component-id context-props mount-id])
                            :reagent-render          (fn [_ context-props] [subscribe-controller component-id context-props])})))

(defn component
  ; @param (keyword)(opt) component-id
  ;  XXX#4882
  ; @param (map) context-props
  ;  {:base-props (map)(opt)
  ;   :component (component)(opt)
  ;    Only w/o {:render-f ...}
  ;   :destructor (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja az initial-props-path Re-Frame adatbázis
  ;    útvonalon tárolt értéket.
  ;   :initializer (metamorphic-event)(opt)
  ;   :initial-props (map)(opt)
  ;   :initial-props-path (vector)(opt)
  ;   :modifier (function)(opt)
  ;   :render-f (function)(opt)
  ;    Only w/o {:component ...}
  ;   :subscriber (subscription-vector)(opt)
  ;    A visszatérési értéknek térkép típusnak kell lennie!
  ;   :updater (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [components/stated {...}]
  ;
  ; @usage
  ;  [components/stated :my-component {...}]
  ;
  ; @usage
  ;  (defn my-component [component-id component-props])
  ;  [components/stated :my-component
  ;                     {:render-f   #'my-component
  ;                      :subscriber [:get-my-component-props]}]
  ;
  ; @usage
  ;  (defn my-component [component-id component-props])
  ;  [components/stated {:component  [my-component :my-component]
  ;                      :subscriber [:get-my-component-props]}]
  ([context-props]
   [component (random/generate-keyword) context-props])

  ([component-id context-props]
   [lifecycle-controller component-id context-props]))
