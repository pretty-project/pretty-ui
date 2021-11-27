
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.10
; Description:
; Version: v2.2.4
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.stated
    (:require [app-fruits.reagent  :as reagent]
              [mid-fruits.candy    :refer [param return]]
              [mid-fruits.map      :as map :refer [dissoc-in]]
              [mid-fruits.vector   :as vector]
              [x.app-components.engine      :as engine]
              [x.app-components.subscriber  :rename {component subscriber}]
              [x.app-components.transmitter :rename {component transmitter}]
              [x.app-core.api      :as a :refer [r]]
              [x.app-db.api        :as db]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name stated
;  A stated komponens a Reagent életciklusait valósítja meg a {:component #'xyz}
;  tulajdonságként átadott komponens számára.
;
; @name destructor
;  A {:destructor ...} tulajdonságként átadott Re-Frame esemény a komponens
;  React-fából történő lecsatolása után történik meg. A komponens újracsatolásakor
;  megtörténő pillanatnyi lecsatolás esetén nem történik meg a destructor esemény.
;  Mivel a destructor esemény megtörténésekor a Re-Frame adatbázis már nem tartalmazza
;  az initial-props-path Re-Frame adatbázis útvonalon tárolt értéket, így azt a destructor
;  esemény utolsó paraméterként kapja meg.
;
; @name initializer
;  Az {:initializer ...} tulajdonságként átadott Re-Frame esemény a komponens
;  React-fába történő csatolása után történik meg. A komponens újracsatolásakor
;  (remounting) nem ismétlődik meg az initializer esemény megtörténése.
;
; @name updater
;  TODO ...
;
; @name initial-props-path
;  Az {:initial-props-path [...]} tulajdonságként átadott Re-Frame adatbázis
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
;  -csatolás saját azonosítóval rendelkezik (mount-id) és a destructor esemény megtörténése vagy
;  a komponens tulajdonságainak Re-Frame adatbázisból való eltávolítása késleltetve
;  történik, így lehetséges megvizsgálni, hogy ha a komponens még mindig azzal az azonosítóval
;  van csatolva, amivel a lecsatolásai események megtörténnének, akkor azok végrehajtódhatnak,
;  de ha azóta eltérő azonosítóval újra van csatolva a komponens, akkor a lecsatolási események
;  nem történnek meg.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def COMPONENT-DESTRUCTION-DELAY 50)

; @constant (keywords in vector)
(def STATED-PROPS
     [:component :destructor :disabler :initializer :initial-props
      :initial-props-path :static-props :subscriber :updater])



;; -- Helpers -----------------------------------------------------------------
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



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-component-initial-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:initial-props-path (item-path vector)(opt)}
  ;
  ; @return (map)
  [db [_ component-id {:keys [initial-props-path]}]]
  (if (some?     initial-props-path)
      (get-in db initial-props-path)))

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
  :components/initialize-component!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:initializer (metamorphic-event)(opt)}
  ; @param (keyword) mount-id
  (fn [{:keys [db]} [_ component-id {:keys [initializer] :as context-props} mount-id]]
      (if-not (r component-initialized? db component-id)
              {:db       (as-> db % (r store-component-initial-props! % component-id context-props)
                                    (r engine/set-component-prop!     % component-id :initialized? true))
               :dispatch (param initializer)})))

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
          (let [initial-props (r get-component-initial-props db component-id context-props)
                destructor    (a/metamorphic-event<-params destructor initial-props)]
               {:db       (as-> db % (r engine/remove-component-props!  % component-id)
                                     (r remove-component-initial-props! % component-id context-props))
                :dispatch (param destructor)}))))



;; -- Status events------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :components/->component-mounted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ; @param (keyword) mount-id
  (fn [{:keys [db]} [_ component-id context-props mount-id]]
      {:db       (as-> db % (r engine/set-component-prop! % component-id :status :mounted)
                            (r engine/set-component-prop! % component-id :mount-id mount-id))
       :dispatch [:components/initialize-component! component-id context-props mount-id]}))

(a/reg-event-fx
  :components/->component-updated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:updater (metamorphic-event)(opt)}
  ; @param (keyword) mount-id
  (fn [{:keys [db]} [_ component-id {:keys [updater] :as context-props} _]]
      {:db       (r engine/set-component-prop! db component-id :status :updated)
       :dispatch (param updater)}))

(a/reg-event-fx
  :components/->component-unmounted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ; @param (keyword) mount-id
  (fn [{:keys [db]} [_ component-id context-props mount-id]]
      {:db (r engine/set-component-prop! db component-id :status :unmounted)
       :dispatch-later [{:ms       (param COMPONENT-DESTRUCTION-DELAY)
                         :dispatch [:components/destruct-component! component-id context-props mount-id]}]}))



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
  (if (context-props->subscribe?   context-props)
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
      [disabler     component-id  context-props]
      [non-disabler component-id  context-props]))

(defn- lifecycle-controller
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;
  ; @return (component)
  [component-id context-props]
  (let [mount-id (a/id)]
       (reagent/lifecycles {:component-did-mount    #(a/dispatch [:components/->component-mounted   component-id context-props mount-id])
                            :component-did-update   #(a/dispatch [:components/->component-updated   component-id context-props mount-id])
                            :component-will-unmount #(a/dispatch [:components/->component-unmounted component-id context-props mount-id])
                            :reagent-render          (fn [_ context-props] [disable-controller component-id context-props])})))

(defn component
  ; @param (keyword)(opt) component-id
  ;  XXX#4882
  ; @param (map) context-props
  ;  {:base-props (map)(opt)
  ;   :component (component)
  ;   :destructor (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja az initial-props path Re-Frame adatbázis
  ;    útvonalon tárolt értéket.
  ;   :disabler (subscription vector)(opt)
  ;   :initializer (metamorphic-event)(opt)
  ;   :initial-props (map)(opt)
  ;   :initial-props-path (item-path vector)(opt)
  ;   :modifier (function)(opt)
  ;   :static-props (map)(opt)
  ;   :subscriber (subscription vector)(opt)
  ;    Return value must be a map!
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
   [component (a/id) context-props])

  ([component-id context-props]
   [lifecycle-controller component-id context-props]))
