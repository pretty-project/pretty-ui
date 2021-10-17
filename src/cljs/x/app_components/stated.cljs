
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.10
; Description:
; Version: v2.0.4
; Compatibility: x4.3.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.stated
    (:require [app-fruits.reagent  :as reagent]
              [mid-fruits.candy    :refer [param return]]
              [mid-fruits.map      :as map :refer [dissoc-in]]
              [mid-fruits.vector   :as vector]
              [x.app-components.subscriber  :rename {view subscriber}]
              [x.app-components.transmitter :rename {view transmitter}]
              [x.app-core.api      :as a :refer [r]]
              [x.app-db.api        :as db]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name stated
;  A stated komponens a Reagent életciklusait valósítja meg a {:component #'xyz}
;  tulajdonságként átadott komponens számára.
;
; @name lifetime
;  A {:lifetime ...} tulajdonság használatával a stated komponens a megadott
;  idő elteltével automatikusan lecsatolódik a React-fáról.
;
; @name unmountable?
;  Az {:unmountable? true} tulajdonság használatával a stated komponens
;  lecsatolása Re-Frame esemény meghívásával is lehetségessé válik.
;
; @name destructor
;  A {:destructor ...} tulajdonságként átadott Re-Frame esemény a komponens
;  React-fából történő lecsatolása után történik meg. A komponens újracsatolásakor
;  megtörténő pillanatnyi lecsatolás esetén nem történik meg a destructor esemény.
;
; @name initializer
;  Az {:initializer ...} tulajdonságként átadott Re-Frame esemény a komponens
;  React-fába történő csatolása után történik meg. A komponens újracsatolásakor
;  (remounting) nem ismétlődik meg az initializer esemény megtörténése.
;
; @name updater
;  TODO ...
;
; @name initial-props-path
;  Az {:initial-props-path [...]} tulajdonságként átadott Re-Frame DB adatbázis
;  útvonal megadásával határozható meg, a komponens React-fába csatolásakor az
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
; @name static-props
;  XXX#0001
;
; @name disabler
;  A {:disabler [...]} tulajdonságként átadott Re-Frame subscription vektor
;  használatával a stated komponens feliratkozik a subscription visszatérési
;  értékére, és azt boolean típusként kiértékelve asszociálja a komponens
;  {:base-props {...}} tulajdonságként átadott térképébe. Így a {:disabled? ...}
;  tulajdonság az XXX#0069 logika szerint átadódik a komponensnek.
;
; @name subscriber
;  XXX#7081
;
; @name updater
;  Az {:updater [...]} tulajdonságként átadott Re-Frame esemény a komponens
;  paramétereinek megváltozása után történik meg.



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  Az egyes [stated] komponensek React-fába történő újracsatolódása (remounting)
;  esetlegesen hibát okozhat, ha a destructor esemény megtörténése vagy a komponens
;  tulajdonságainak Re-Frame adatbázisból való eltávolítása nem a megfelelő időben
;  történik meg.
;
; @description
;  A remounting esemény egy egymás után megtörténő unmount majd egy mount eseményekből
;  áll.
;
; @description
;  A [stated] komponens első verzióiban a remounting felismerése úgy működött, hogy
;  az unmount esemény időbeni eltolásával az a (re)mount esemény után történt meg,
;  így meg lehetett vizsgálni, hogy ha egy mounted komponensen (re)mount esemény
;  történik majd az ahhoz tartozó – eredetileg a (re)mount esemény előtt megtörténő –
;  unmount esemény történik, akkor az remounting eseménypárnak számít.
;  Ebből az következett, hogy bizonyos esetekben, ha gyors egymás utánban történt kettő
;  vagy több remounting, akkor az unmount-(re)mount eseménypárok néha összekeveredtek
;  és ez hibákhoz vezetett.
;  Ennek kiküszöbölésére került bevezetésre, hogy minden React-fába történe komponens-
;  -csatolás saját azonosítóval rendelkezik és a destructor esemény megtörténése vagy
;  a komponens tulajdonságainak Re-Frame adatbázisból való eltávolítása késleltetve
;  történik, így lehetséges megvizsgálni, hogy ha a komponens még mindig azzal az azonosítóval
;  van csatolva, amivel a lecsatolásai események megtörténnének, akkor azok végrehajtódhatnak,
;  de ha azóta eltérő azonosítóval újra van csatolva a komponens, akkor a lecsatolási események
;  nem történnek meg.
;
; @description



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def COMPONENT-DESTRUCTION-DELAY 50)

; @constant (keywords in vector)
(def STATED-PROPS
     [:component :destructor :disabler :initializer :initial-props
      :initial-props-path :lifetime :static-props :subscriber
      :unmountable? :updater])



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn extended-props->stated-props
  ; @param (map) extended-props
  ;
  ; @return (map)
  [extended-props]
  (map/inherit extended-props STATED-PROPS))

(defn extended-props->stated-props?
  ; @param (map) extended-props
  ;
  ; @return (map)
  [extended-props]
  ; Ha egy térkép tartalmazza a :component kulcsot, attól még nem számít stated-props térképnek.
  (map/contains-of-keys? (param extended-props)
                         (vector/remove-item STATED-PROPS :component)))

(defn- context-props->component-unmountable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context-props
  ;  {:lifetime (integer)(opt)
  ;   :unmountable? (boolean)(opt)}
  ;
  ; @return (boolean)
  [{:keys [lifetime unmountable?]}]
  (or (integer? lifetime)
      (boolean  unmountable?)))

(defn- context-props->initialize?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context-props
  ;  {:initializer (metamorphic-event)(opt)}
  ;
  ; @return (boolean)
  [{:keys [initializer]}]
  (some? initializer))

(defn- context-props->subscribe?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context-props
  ;  {:subscriber (subscription vector)(opt)}
  ;
  ; @return (boolean)
  [{:keys [subscriber]}]
  (some? subscriber))

(defn- context-props->disability?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context-props
  ;  {:disabler (subscription vector)(opt)}
  ;
  ; @return (boolean)
  [{:keys [disabler]}]
  (some? disabler))

(defn- component-status->component-mounted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-status
  ;
  ; @return (boolean)
  [component-status]
  (not= component-status :unmount!))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-component-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (keyword) prop-id
  ;
  ; @return (*)
  [db [_ component-id prop-id]]
  (get-in db (db/path ::components component-id prop-id)))

(a/reg-sub ::get-component-prop get-component-prop)

(defn- get-component-initial-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:initial-props-path (item-path vector)(opt)}
  ;
  ; @return (map)
  [db [_ component-id {:keys [initial-props-path]}]]
  (if (some? initial-props-path)
      (get-in db initial-props-path)))

(defn- component-mounted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (boolean)
  [db [_ component-id]]
  (let [component-status (r get-component-prop db component-id :status)]
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
  (let [component-status (r get-component-prop db component-id :status)]
       (= component-status :remounting)))

(defn- component-mounted-as?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (keyword) mount-id
  ;
  ; @return (boolean)
  [db [_ component-id mount-id]]
  (let [mounted-as (r get-component-prop db component-id :mount-id)]
       (= mounted-as mount-id)))

(defn- component-initialized?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (boolean)
  [db [_ component-id]]
  (let [component-initialized? (r get-component-prop db component-id :initialized?)]
       (boolean component-initialized?)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- set-component-prop!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (keyword) prop-id
  ; @param (*) prop-value
  ;
  ; @return (map)
  [db [_ component-id prop-id prop-value]]
  (assoc-in db (db/path ::components component-id prop-id) prop-value))

(defn- remove-component-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (map)
  [db [_ component-id]]
  (dissoc-in db (db/path ::components component-id)))

(defn- store-component-initial-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:initial-props (map)
  ;   :initial-props-path (item-path vector)(opt)}
  ;
  ; @return (map)
  [db [_ component-id {:keys [initial-props initial-props-path]}]]
  (if (some?    initial-props-path)
      (assoc-in db initial-props-path initial-props)
      (return   db)))

(defn- remove-component-initial-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:initial-props-path (item-path vector)(opt)}
  ;
  ; @return (map)
  [db [_ component-id {:keys [initial-props-path]}]]
  (if (some?        initial-props-path)
      (dissoc-in db initial-props-path)
      (return    db)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::initialize-component!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:initializer (metamorphic-event)(opt)}
  ; @param (keyword) mount-id
  (fn [{:keys [db]} [event-id component-id {:keys [initializer] :as context-props} mount-id]]
      (if-not (r component-initialized? db component-id)
              {:db       (-> db (store-component-initial-props! [event-id component-id context-props])
                                (set-component-prop!            [event-id component-id :initialized? true]))
               :dispatch (param initializer)})))

(a/reg-event-fx
  ::destruct-component!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:destructor (metamorphic-event)(opt)}
  ; @param (keyword) mount-id
  (fn [{:keys [db]} [event-id component-id {:keys [destructor] :as context-props} mount-id]]
      (if (and (r component-unmounted?  db component-id)
               (r component-mounted-as? db component-id mount-id))
          {:db       (-> db (remove-component-props!         [event-id component-id])
                            (remove-component-initial-props! [event-id component-id context-props]))
           :dispatch (param destructor)})))

(a/reg-event-fx
  :x.app-components/unmount-component!
  ; WARNING! NOT TESTED!
  ;
  ; @param (keyword) component-id
  (fn [{:keys [db]} [_ component-id]]
      {:db (r set-component-prop! db component-id :status :unmount!)}))

(a/reg-event-fx
  :x.app-components/remount-component!
  ; WARNING! NOT TESTED!
  ;
  ; @param (keyword) component-id
  (fn [{:keys [db]} [_ component-id]]
      {:db (r set-component-prop! db component-id :status :remount!)}))



;; -- Status events------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::->component-mounted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:lifetime (ms)(opt)}
  ; @param (keyword) mount-id
  (fn [{:keys [db]} [event-id component-id {:keys [lifetime] :as context-props} mount-id]]
      {:db (-> db (set-component-prop! [event-id component-id :status :mounted])
                  (set-component-prop! [event-id component-id :mount-id mount-id]))

       :dispatch [::initialize-component! component-id context-props mount-id]

       ; Self destruction
       :dispatch-later [(if (some? lifetime)
                            {:ms       (param lifetime)
                             :dispatch [:x.app-components/unmount-component! component-id]})]}))

(a/reg-event-fx
  ::->component-updated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:updater (metamorphic-event)(opt)}
  ; @param (keyword) mount-id
  (fn [{:keys [db]} [_ component-id {:keys [updater] :as context-props} _]]
      {:db       (r set-component-prop! db component-id :status :updated)
       :dispatch (param updater)}))

(a/reg-event-fx
  ::->component-unmounted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ; @param (keyword) mount-id
  (fn [{:keys [db]} [_ component-id context-props mount-id]]
      {:db (r set-component-prop! db component-id :status :unmounted)
       :dispatch-later [{:ms       (param COMPONENT-DESTRUCTION-DELAY)
                         :dispatch [::destruct-component! component-id context-props mount-id]}]}))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- non-subscriber
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;
  ; @return (component)
  [component-id context-props]
  [transmitter component-id context-props])

(defn- subscribe-controller
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;
  ; @return (component)
  [component-id context-props]
  (if (context-props->subscribe? context-props)
      [subscriber     component-id context-props]
      [non-subscriber component-id context-props]))

(defn- non-disabler
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;
  ; @return (component)
  [component-id context-props]
  [subscribe-controller component-id context-props])

(defn- disabler
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:disabler (subscription vector)}
  ;
  ; @return (component)
  [component-id {:keys [disabler] :as context-props}]
  (let [disabled? (a/subscribe disabler)]
       (fn [_ context-props]
           (let [context-props (assoc-in context-props [:base-props :disabled?]
                                                       (boolean @disabled?))]
                [subscribe-controller component-id context-props]))))

(defn- disable-controller
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;
  ; @return (component)
  [component-id context-props]
  (if (context-props->disability? context-props)
      [disabler     component-id context-props]
      [non-disabler component-id context-props]))

(defn- lifecycle-controller
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;
  ; @return (component)
  [component-id context-props]
  (let [mount-id (a/id)]
       (reagent/lifecycles {:component-did-mount    #(a/dispatch [::->component-mounted   component-id context-props mount-id])
                            :component-did-update   #(a/dispatch [::->component-updated   component-id context-props mount-id])
                            :component-will-unmount #(a/dispatch [::->component-unmounted component-id context-props mount-id])
                            :reagent-render          (fn [_ context-props] [disable-controller component-id context-props])})))

(defn- non-unmountable-component
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;
  ; @return (component)
  [component-id context-props]
  [lifecycle-controller component-id context-props])

(defn- unmountable-component
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;
  ; @return (component)
  [component-id context-props]
  (let [component-status (a/subscribe [::get-component-prop component-id :status])]
       (fn [_ context-props] (if (component-status->component-mounted? @component-status)
                                 [lifecycle-controller component-id context-props]))))

(defn- unmount-controller
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;
  ; @return (component)
  [component-id context-props]
  (if (context-props->component-unmountable? context-props)
      [unmountable-component     component-id context-props]
      [non-unmountable-component component-id context-props]))

(defn view
  ; @param (keyword)(opt) component-id
  ;  XXX#4882
  ; @param (map) context-props
  ;  {:base-props (map)(opt)
  ;   :component (component)
  ;   :destructor (metamorphic-event)(opt)
  ;   :disabler (subscription vector)(opt)
  ;   :initializer (metamorphic-event)(opt)
  ;   :initial-props (map)(opt)
  ;   :initial-props-path (item-path vector)(opt)
  ;   :modifier (function)(opt)
  ;   :lifetime (ms)(opt)
  ;   :static-props (map)(opt)
  ;   :subscriber (subscription vector)(opt)
  ;    Return value must be a map!
  ;   :unmountable? (boolean)(opt)
  ;   :updater (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [components/stated {...}]
  ;
  ; @usage
  ;  [components/stated :my-component {...}]
  ;
  ; @usage
  ;  (defn my-component [component-id static-props])
  ;  [components/stated {:component    #'my-component
  ;                      :static-props {...}}]
  ;
  ; @usage
  ;  (defn my-component [component-id dynamic-props])
  ;  [components/stated {:component  #'my-component
  ;                      :subscriber [::get-view-props]}]
  ;
  ; @usage
  ;  (defn my-component [component-id static-props dynamic-props])
  ;  [components/stated {:component    #'my-component
  ;                      :static-props {...}
  ;                      :subscriber   [::get-view-props]}]
  ;
  ; @usage
  ;  (defn my-component [component-id {:keys [disabled?] :as dynamic-props}])
  ;  [components/stated {:component #'my-component
  ;                      :disabler  [::component-disabled?]}]
  ;
  ; @return (component)
  ([context-props]
   [view (a/id) context-props])

  ([component-id context-props]
   [unmount-controller component-id context-props]))
