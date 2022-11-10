
; WARNING
; Az x.app-ui.renderer névtér újraírása szükséges, a Re-Frame adatbázis események
; számának csökkentése érdekében!



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.renderer
    (:require [mid-fruits.candy  :refer [param return]]
              [hiccup.api        :as hiccup]
              [mid-fruits.vector :as vector]
              [re-frame.api      :as r :refer [r]]
              [reagent.api       :as reagent]
              [time.api          :as time]
              [x.app-db.api      :as x.db]
              [x.app-ui.engine   :as engine]))



;; -- Bugs --------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; BUG#8493
;  Ha megváltoznak egy UI elem adatbázisban tárolt tulajdonságai, akkor
;  a UI elemen kirenderelt tartalom is újra renderelődik, ugyanis a kirenderelt
;  tartalom egyik felmenő komponense a UI elem wrapper komponense, aminek
;  a megváltozó tulajdonságok miatt változik az egyik paramétere és ez a teljes
;  komponens újrarenderelését okozza.



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name renderer
;  A UI renderer egy partíció data-item elemeit sorolja fel egy komponens-sorozat
;  komponenseinek paraméterként átadva.
;  A UI renderer az egyes partíciókat rendezett partícióként (ordered-partition)
;  hozza létre.
;  Az elemek felsorolása a partíció data-order vektorában rögzített
;  sorrend szerint történik.
;
; @name attributes
;  Az attributes paraméterként átadott térkép a komponens-sorozatot
;  körülvevő [:div] elem attribútumait tartalmazhatja.
;
; @name renderer destructor
;  TODO ...
;
; @name renderer initializer
;  TODO ...
;
; @name element destructor
;  TODO ...
;
; @name element initializer
;  TODO ...
;
; @name required?
;  Abban az esetben, ha a renderer {:required? true} tulajdonsággal rendelkezik,
;  akkor a renderer React-fából való esetleges lecsatolása rendszerhibának minősül.
;
; @name alternate-renderer
;  Abban az esetben ha a renderer {:required? true} tulajdonsággal rendelkezik
;  és a renderer {:alternate-renderer ...} tulajdonságaként megadott azonosítóval
;  rendelkező helyettesítő renderer, elemeket jelenít meg, akkor a renderer
;  React-fából való esetleges lecsatolása nem minősül rendszerhibának.
;  Pl. {:alternate-renderer :alternate-partition/elements}
;
; @name element-rendered?
;  Az elem a React-fába csatolva.
;
; @name element-visible?
;  Az elem a React-fába csatolva és nincs megjelölve invisible-element
;  tulajdonsággal.
;
; @name any-element-rendered?
;  A renderer partíciója tartalmaz legalább egy kirenderelt elemet.
;
; @name any-element-visible?
;  A renderer partíciója tartalmaz legalább egy olyan kirenderelt elemet, amely
;  nincs megjelölve invisible-element-ként.
;
; @name invisible-element
;  Az element a megszűntetésének kezdetekor invisible-element jelölést kap.
;  Amíg az element animált eltűnése történik, az element a renderer partíciójában
;  megtalálható.
;
; @name queue-behavior
;  {:queue-behavior :wait} Ha a partíció elemeinek száma elérte a maximálisan
;  kirenderelhető elemek számát, akkor az újonnan hozzáadott elemek addig
;  nem renderelődnek, amíg a kirenderelt elemek száma nem kezd el csökkenni.
;
;  {:queue-behavior :push} Ha a partíció elemeinek száma elérte a maximálisan
;  kirenderelhető elemek számát, akkor minden újabb elem hozzáadása kitörli
;  a legrégebben hozzáadott elemet.
;
;  {:queue-behavior :ignore} Ha a partíció elemeinek száma eléri a maximálisan
;  kirenderelhető elemek számát, akkor az újabb elemek hozzáadása nem történik meg.
;
; @name rerender-same?
;  TODO ...
;
; @name renderer-reserved?
;  TODO ...



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def ILLEGAL-UNMOUNTING-ERROR "Illegal unmounting error!")

; @constant (ms)
;  A UI elem animált megjelenítésére rendelkezésre álló idő.
(def REVEAL-ANIMATION-TIMEOUT 350)

; @constant (ms)
;  A UI elem animált eltűntetésére rendelkezésre álló idő.
;
; BUG#4701 (def HIDE-ANIMATION-TIMEOUT 350)
(def HIDE-ANIMATION-TIMEOUT 450)

; @constant (ms)
(def UPDATE-ANIMATION-TIMEOUT 350)

; DEBUG
; @constant (ms)
(def RENDER-DELAY-OFFSET 0)
; - Ha a render-delay 100 ms várakozásra volt állítva, akkor az egymás után különböző azonosítóval
;   kirenderelt surface elemek villanva jelentek meg (100 ms különbséggel).
; - Ha pedig különböző surface-ek ugyanazt az azonosítót használták, ami több okból sem jó, akkor
;   nem jelentkezett ez a jelenség, mivel nem történt re-render, csak update-elte a surface a tartalmát.
; - Próbaképpen át lett állítva 0 ms várakozásra 2021. 10. 26.
;   Ha ez nem okoz semmilyen problémát hosszútávon, akkor maradjon az új érték megtartva.
;(def RENDER-DELAY-OFFSET 100)

; @constant (ms)
(def DESTROY-DELAY-OFFSET 100)

; @constant (integer)
(def DEFAULT-MAX-ELEMENTS-RENDERED 32)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-props
  ;
  ; @return (map)
  ;  {:hide-animated? (boolean)
  ;   :reveal-animated? (boolean)}
  [element-props]
  (merge {:hide-animated?   true
          :reveal-animated? true}
         (param element-props)))

(defn- renderer-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) renderer-props
  ;
  ; @return (map)
  ;  {:invisible-elements (vector)
  ;   :max-elements-rendered (integer)
  ;   :queue-behavior (keyword)
  ;   :required? (boolean)
  ;   :rerender-same? (boolean)}
  [renderer-props]
  (merge {:invisible-elements    []
          :max-elements-rendered DEFAULT-MAX-ELEMENTS-RENDERED
          :queue-behavior        :wait
          :required?             false
          :rerender-same?        false}
         (param renderer-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (keywords in vector)
  [db [_ renderer-id]]
  (let [data-order-key (engine/data-key renderer-id :data-order)]
       (get-in db [:ui data-order-key])))

(r/reg-sub :ui/get-element-order get-element-order)

(defn get-rendered-element-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vector)
  [db [_ renderer-id]]
  (let [data-order-key (engine/data-key renderer-id :data-order)]
       (get-in db [:ui data-order-key])))

(defn get-invisible-element-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vector)
  [db [_ renderer-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (get-in db [:ui meta-items-key :invisible-elements])))

(defn get-visible-element-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vector)
  [db [_ renderer-id]]
  ; WARNING!
  ; A visible-element order vektor külön kell majd tárolni!
  ; Ne a feliratkozások számítsák ki az értékét!
  (let [rendered-element-order (r get-rendered-element-order db renderer-id)
        invisible-element-ids  (r get-invisible-element-ids  db renderer-id)]
       (vector/remove-items rendered-element-order invisible-element-ids)))

(defn any-element-rendered?
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [rendered-element-order (r get-rendered-element-order db renderer-id)]
       (vector/nonempty? rendered-element-order)))

(defn any-element-invisible?
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [invisible-element-ids (r get-invisible-element-ids db renderer-id)]
       (vector/nonempty? invisible-element-ids)))

(defn any-element-visible?
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [visible-element-order (r get-visible-element-order db renderer-id)]
       (vector/nonempty? visible-element-order)))

(defn no-visible-elements?
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [any-element-visible? (r any-element-visible? db renderer-id)]
       (not any-element-visible?)))

(defn get-lower-visible-element-id
  ; @param (keyword) renderer-id
  ;
  ; @return (keyword)
  [db [_ renderer-id]]
  (vector/first-item (r get-visible-element-order db renderer-id)))

(defn get-max-elements-rendered
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (integer)
  [db [_ renderer-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (get-in db [:ui meta-items-key :max-elements-rendered])))

(defn max-elements-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [max-elements-rendered (r get-max-elements-rendered db renderer-id)
        visible-element-order (r get-visible-element-order db renderer-id)]
       (vector/count? visible-element-order max-elements-rendered)))

(defn- get-renderer-queue-behavior
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (keyword)
  [db [_ renderer-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (get-in db [:ui meta-items-key :queue-behavior])))

(defn- pushed-rendering-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [queue-behavior (r get-renderer-queue-behavior db renderer-id)]
       (= queue-behavior :push)))

(defn- ignore-rendering?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [queue-behavior (r get-renderer-queue-behavior db renderer-id)]
       (and (= queue-behavior :ignore)
            (r max-elements-reached? db renderer-id))))

(defn- renderer-required?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (get-in db [:ui meta-items-key :required?])))

(defn- get-alternate-renderer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (keyword)
  [db [_ renderer-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (get-in db [:ui meta-items-key :alternate-renderer])))

(defn- renderer-require-error?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (if-let [alternate-renderer (r get-alternate-renderer db renderer-id)]
          (and (r renderer-required?   db renderer-id)
               (r no-visible-elements? db alternate-renderer))
          (r renderer-required? db renderer-id)))

(defn- renderer-reserved?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (get-in db [:ui meta-items-key :reserved?])))

(defn- renderer-free?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (not (get-in db [:ui meta-items-key :reserved?]))))

(defn get-element-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ renderer-id element-id]]
  (let [data-items-key (engine/data-key renderer-id :data-items)]
       (get-in db [:ui data-items-key element-id])))

(r/reg-sub :ui/get-element-props get-element-props)

(defn get-element-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ renderer-id element-id prop-key]]
  (let [data-items-key (engine/data-key renderer-id :data-items)]
       (get-in db [:ui data-items-key element-id prop-key])))

(defn reveal-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (let [reveal-animated? (r get-element-prop db renderer-id element-id :reveal-animated?)]
       (boolean reveal-animated?)))

(defn hide-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (let [hide-animated? (r get-element-prop db renderer-id element-id :hide-animated?)]
       (boolean hide-animated?)))

(defn element-rendered?
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (let [rendered-element-order (r get-rendered-element-order db renderer-id)]
       (vector/contains-item? rendered-element-order element-id)))

(defn element-invisible?
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (let [invisible-element-ids (r get-invisible-element-ids db renderer-id)]
       (vector/contains-item? invisible-element-ids element-id)))

(defn element-visible?
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (and      (r element-rendered?  db renderer-id element-id)
       (not (r element-invisible? db renderer-id element-id))))

(defn rerender-same?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (get-in db [:ui meta-items-key :rerender-same?])))

(defn get-rerender-delay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (ms)
  [db [_ renderer-id element-id]]
  (if (r hide-element-animated? db renderer-id element-id)
      (+ HIDE-ANIMATION-TIMEOUT RENDER-DELAY-OFFSET)
      (return RENDER-DELAY-OFFSET)))

(defn- get-pushing-delay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (ms)
  [db [_ renderer-id]]
  (let [lower-visible-element-id (r get-lower-visible-element-id db renderer-id)]
       (if (r hide-element-animated? db renderer-id lower-visible-element-id)
           (+ HIDE-ANIMATION-TIMEOUT RENDER-DELAY-OFFSET)
           (return RENDER-DELAY-OFFSET))))

(defn- get-queue-rendering-delay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (ms)
  [db [_ renderer-id element-id]]
  (if (r hide-element-animated? db renderer-id element-id)
      (+ HIDE-ANIMATION-TIMEOUT RENDER-DELAY-OFFSET)
      (return RENDER-DELAY-OFFSET)))

(defn- get-destroying-delay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (ms)
  [db [_ renderer-id element-id]]
  (if (r hide-element-animated? db renderer-id element-id)
      (+ HIDE-ANIMATION-TIMEOUT DESTROY-DELAY-OFFSET)
      (return DESTROY-DELAY-OFFSET)))

(defn- render-element-now?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id _]]
  (and (r renderer-free? db renderer-id)
       (or      (r pushed-rendering-enabled? db renderer-id)
           (not (r max-elements-reached?     db renderer-id)))))

(defn- get-rendering-queue
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vectors in vector)
  ;  [[(keyword) element-id
  ;    (map) element-props]
  ;   [...]]
  [db [_ renderer-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (get-in db [:ui meta-items-key :rendering-queue])))

(defn- get-next-rendering
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vector)
  ;  [(keyword) element-id
  ;   (map) element-props]
  [db [_ renderer-id]]
  (let [rendering-queue (r get-rendering-queue db renderer-id)]
       (vector/first-item rendering-queue)))

(defn- rerender-element?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id _]]
  (and (r element-rendered? db renderer-id element-id)
       (r rerender-same?    db renderer-id)))

(defn- update-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:update-animated? (boolean)(opt)}
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id {:keys [update-animated?]}]]
  (and      (r element-rendered? db renderer-id element-id)
       (not (r rerender-same?    db renderer-id))
       (boolean update-animated?)))

(defn- update-element-static?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:update-animated? (boolean)(opt)}
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id {:keys [update-animated?]}]]
  (and      (r element-rendered? db renderer-id element-id)
       (not (r rerender-same?    db renderer-id))
       (not update-animated?)))

(defn- push-element?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id _]]
  (and      (r max-elements-reached?     db renderer-id)
            (r pushed-rendering-enabled? db renderer-id)
       (not (r element-rendered?         db renderer-id element-id))))

(defn- render-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:reveal-animated? (boolean)(opt)}
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id {:keys [reveal-animated?]}]]
  (and (not (r max-elements-reached? db renderer-id))
       (not (r element-rendered?     db renderer-id element-id))
       (boolean reveal-animated?)))

(defn- render-element-static?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:reveal-animated? (boolean)(opt)}
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id {:keys [reveal-animated?]}]]
  (and (not (r max-elements-reached? db renderer-id))
       (not (r element-rendered?     db renderer-id element-id))
       (not reveal-animated?)))

(defn- destroy-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (and (r element-visible?       db renderer-id element-id)
       (r hide-element-animated? db renderer-id element-id)))

(defn- destroy-element-static?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (and      (r element-visible?       db renderer-id element-id)
       (not (r hide-element-animated? db renderer-id element-id))))

(defn get-visible-elements-destroying-event-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (maps in vector)
  ;  [{:ms (ms)
  ;    :dispatch (metamorphic-event)}]
  [db [_ renderer-id]]
  (let [visible-element-order (r get-visible-element-order db renderer-id)]
       (letfn [(f [event-list dex element-id]
                  ; Az időzített esemény-lista aktuális elemének eltűntetési eseményének időértéke,
                  ; az utolsó (előző) elem időértéke ({:ms ...}), összeadva az előző elem eltűnéséhez
                  ; szükséges idővel
                  (if (= dex 0)
                      ; The first destroying event in the list ...
                      [{:dispatch [:ui/destroy-element! renderer-id element-id] :ms 0}]
                      ; The other destroying events in the list ...
                      (let [prev-element-id  (get-in event-list [(dec dex) :dispatch 2])
                            prev-event-delay (get-in event-list [(dec dex) :ms])
                            destroying-delay (r get-destroying-delay db renderer-id prev-element-id)]
                           (conj event-list {:dispatch [:ui/destroy-element! renderer-id element-id]
                                             :ms       (+ prev-event-delay destroying-delay)}))))]
              (reduce-kv f [] visible-element-order))))

(defn get-visible-elements-destroying-duration
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (ms)
  [db [_ renderer-id]]
  (let [visible-element-order (r get-visible-element-order db renderer-id)]
       (letfn [(f [duration element-id]
                  (let [destroying-delay (r get-destroying-delay db renderer-id element-id)]
                       (+ duration destroying-delay)))]
              (reduce f 0 visible-element-order))))

(defn get-render-log
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (keyword) action-key
  ;
  ; @usage
  ;  (r renderer/get-render-log db :bubbles :my-bubble :render-requested-at)
  ;
  ; @return (ms)
  [db [_ renderer-id element-id action-key]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (get-in db [:ui meta-items-key :render-log element-id action-key])))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- update-render-log!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (keyword) action-key
  ;
  ; @usage
  ;  (r update-render-log! db :bubbles :my-bubble :render-requested-at)
  ;
  ; @return (map)
  [db [_ renderer-id element-id action-key]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (assoc-in db [:ui meta-items-key :render-log element-id action-key]
                    (time/elapsed))))

(defn- reserve-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (map)
  [db [_ renderer-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (assoc-in db [:ui meta-items-key :reserved?] true)))

(r/reg-event-db :ui/reserve-renderer! reserve-renderer!)

(defn- free-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (map)
  [db [_ renderer-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (assoc-in db [:ui meta-items-key :reserved?] false)))

(defn- store-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (map)
  [db [_ renderer-id element-id element-props]]
  (let [data-items-key (engine/data-key renderer-id :data-items)
        data-order-key (engine/data-key renderer-id :data-order)]
       (as-> db % (r update-render-log! % renderer-id  element-id :rendered-at)
                  (r x.db/set-item!     % [:ui data-items-key element-id] element-props)
                  (r x.db/apply-item!   % [:ui data-order-key] vector/move-item-to-last element-id))))

(defn- remove-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ renderer-id element-id]]
  (let [data-items-key (engine/data-key renderer-id :data-items)]
       (as-> db % (r update-render-log! % renderer-id  element-id :props-removed-at)
                  (r x.db/remove-item!  % [:ui data-items-key element-id]))))

(r/reg-event-db :ui/remove-element! remove-element!)

(defn- update-element-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (map)
  [db [_ renderer-id element-id element-props]]
  (let [data-items-key (engine/data-key renderer-id :data-items)]
       (as-> db % (r update-render-log! % renderer-id element-id :updated-at)
                  (r x.db/set-item!     % [:ui data-items-key element-id] element-props))))

(defn set-element-prop!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (keyword) prop-key
  ; @param (*) prop-value
  ;
  ; @return (map)
  [db [_ renderer-id element-id prop-key prop-value]]
  (let [data-items-key (engine/data-key renderer-id :data-items)]
       (assoc-in db [:ui data-items-key element-id prop-key] prop-value)))

(r/reg-event-db :ui/set-element-prop! set-element-prop!)

(defn- stop-element-rendering!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Az element-props eltávolítása előtt, szükséges leállítani a UI elem renderelését.
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ renderer-id element-id]]
  (let [data-order-key (engine/data-key renderer-id :data-order)]
       (update-in db [:ui data-order-key] vector/remove-item element-id)))

(r/reg-event-db :ui/stop-element-rendering! stop-element-rendering!)

(defn- render-element-later!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (map)
  [db [_ renderer-id element-id element-props]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (update-in db [:ui meta-items-key :rendering-queue]
                     vector/conj-item [element-id element-props])))

(r/reg-event-db :ui/render-element-later! render-element-later!)

(defn- trim-rendering-queue!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (map)
  [db [_ renderer-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (update-in db [:ui meta-items-key :rendering-queue]
                     vector/remove-nth-item 0)))

(r/reg-event-db :ui/trim-rendering-queue! trim-rendering-queue!)

(defn- empty-rendering-queue!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (map)
  [db [_ renderer-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (assoc-in db [:ui meta-items-key :rendering-queue] [])))

(r/reg-event-db :ui/empty-rendering-queue! empty-rendering-queue!)

(defn- mark-element-as-invisible!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ renderer-id element-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (update-in db [:ui meta-items-key :invisible-elements]
                     vector/conj-item element-id)))

(r/reg-event-db :ui/mark-element-as-invisible! mark-element-as-invisible!)

(defn- unmark-element-as-invisible!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ renderer-id element-id]]
  (let [meta-items-key (engine/data-key renderer-id :meta-items)]
       (update-in db [:ui meta-items-key :invisible-elements]
                     vector/remove-item element-id)))

(r/reg-event-db :ui/unmark-element-as-invisible! unmark-element-as-invisible!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :ui/init-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  (fn [{:keys [db]} [_ renderer-id renderer-props]]
      (let [data-items-key (engine/data-key renderer-id :data-items)
            data-order-key (engine/data-key renderer-id :data-order)
            meta-items-key (engine/data-key renderer-id :meta-items)]
           {:db (as-> db % (r x.db/set-item! % [:ui data-items-key] {})
                           (r x.db/set-item! % [:ui data-order-key] [])
                           (r x.db/set-item! % [:ui meta-items-key] renderer-props))})))

(r/reg-event-fx :ui/destruct-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  (fn [{:keys [db]} [_ renderer-id _]]
      {:dispatch-if [(r renderer-require-error?   db renderer-id)
                     [:ui/renderer-unmounted-illegal renderer-id]]}))

(r/reg-event-fx :ui/render-element-animated!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      {:db             (r store-element! db renderer-id element-id element-props)
       :dispatch-later [{:ms REVEAL-ANIMATION-TIMEOUT :dispatch [:ui/rendering-ended renderer-id]}]}))

(r/reg-event-fx :ui/render-element-static!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      {:db       (r store-element!   db renderer-id element-id element-props)
       :dispatch [:ui/rendering-ended renderer-id]}))

(r/reg-event-fx :ui/rerender-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      (let [rerender-delay (r get-rerender-delay db renderer-id element-id)]
           {:dispatch [:ui/destroy-element! renderer-id element-id]
            :dispatch-later
            [{:ms rerender-delay :dispatch [:ui/select-rendering-mode! renderer-id element-id element-props]}]})))

(r/reg-event-fx :ui/update-element-animated!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      {:db (r update-element-props! db renderer-id element-id element-props)
       :fx [:environment/set-element-attribute! (hiccup/value element-id) "data-animation" "update"]
       :dispatch [:ui/rendering-ended renderer-id]
       :dispatch-later
       [{:ms UPDATE-ANIMATION-TIMEOUT
         :fx [:environment/remove-element-attribute! (hiccup/value element-id) "data-animation"]}]}))

(r/reg-event-fx :ui/update-element-static!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      {:db       (r update-element-props! db renderer-id element-id element-props)
       :dispatch [:ui/rendering-ended renderer-id]}))

(r/reg-event-fx :ui/push-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      (let [pushing-delay            (r get-pushing-delay            db renderer-id)
            lower-visible-element-id (r get-lower-visible-element-id db renderer-id)]
           {:dispatch [:ui/destroy-element! renderer-id lower-visible-element-id]
            :dispatch-later
            [{:ms pushing-delay :dispatch [:ui/select-rendering-mode! renderer-id element-id element-props]}]})))

(r/reg-event-fx :ui/select-rendering-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      (cond (r push-element?            db renderer-id element-id element-props)
            [:ui/push-element!             renderer-id element-id element-props]
            (r rerender-element?        db renderer-id element-id element-props)
            [:ui/rerender-element!         renderer-id element-id element-props]
            (r update-element-animated? db renderer-id element-id element-props)
            [:ui/update-element-animated!  renderer-id element-id element-props]
            (r update-element-static?   db renderer-id element-id element-props)
            [:ui/update-element-static!    renderer-id element-id element-props]
            (r render-element-animated? db renderer-id element-id element-props)
            [:ui/render-element-animated!  renderer-id element-id element-props]
            (r render-element-static?   db renderer-id element-id element-props)
            [:ui/render-element-static!    renderer-id element-id element-props]

            ; Ha a renderer {:queue-behavior :wait :rerender-same? false} beállítással
            ; renderelne egy már kirenderelt element, akkor egyik render esemény sem
            ; történik meg, ezért szükséges a renderert felszabadítani.
            :return [:ui/rendering-ended renderer-id])))

(r/reg-event-fx :ui/request-rendering-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      (if (r render-element-now? db renderer-id element-id)
          {:db       (as-> db % (r reserve-renderer!  % renderer-id)
                                (r update-render-log! % renderer-id element-id :render-requested-at))
           :dispatch [:ui/select-rendering-mode! renderer-id element-id element-props]}
          {:dispatch [:ui/render-element-later!  renderer-id element-id element-props]})))

(r/reg-event-fx :ui/render-element-from-queue?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  (fn [{:keys [db]} [_ renderer-id]]
      (if-let [[element-id element-props] (r get-next-rendering db renderer-id)]
              {:db             (r trim-rendering-queue! db renderer-id)
               :dispatch-later [{:ms       RENDER-DELAY-OFFSET
                                 :dispatch [:ui/request-rendering-element! renderer-id element-id element-props]}]})))

(r/reg-event-fx :ui/destroy-element-animated!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ renderer-id element-id]]
      {:db (r mark-element-as-invisible! db renderer-id element-id)
       ; 1.
       ; Hide element ...
       :fx [:environment/set-element-attribute! (hiccup/value element-id) "data-animation" "hide"]
       ; 2.
       :dispatch-later
       [{:ms HIDE-ANIMATION-TIMEOUT
         :dispatch-n [[:ui/stop-element-rendering!      renderer-id element-id]
                      [:ui/remove-element!              renderer-id element-id]
                      [:ui/unmark-element-as-invisible! renderer-id element-id]
                      [:ui/render-element-from-queue?!  renderer-id]]}]}))

(r/reg-event-fx :ui/destroy-element-static!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ renderer-id element-id]]
      {:db       (as-> db % (r stop-element-rendering! % renderer-id element-id)
                            (r remove-element!         % renderer-id element-id))
       :dispatch [:ui/render-element-from-queue?! renderer-id]}))

(r/reg-event-fx :ui/destroy-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ renderer-id element-id]]
      {:db       (r update-render-log!              db renderer-id element-id :destroyed-at)
       :dispatch (cond (r destroy-element-animated? db renderer-id element-id)
                       [:ui/destroy-element-animated!  renderer-id element-id]
                       (r destroy-element-static?   db renderer-id element-id)
                       [:ui/destroy-element-static!    renderer-id element-id])}))

(r/reg-event-fx :ui/destroy-all-elements!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  (fn [{:keys [db]} [_ renderer-id]]
      (let [destroying-event-list (r get-visible-elements-destroying-event-list db renderer-id)]
           {:db             (r empty-rendering-queue! db renderer-id)
            :dispatch-later (param destroying-event-list)})))

(r/reg-event-fx :ui/empty-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  (fn [{:keys [db]} [_ renderer-id]]
      [:ui/destroy-all-elements! renderer-id]))

(r/reg-event-fx :ui/rendering-ended
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  (fn [{:keys [db]} [_ renderer-id]]
      {:db       (r free-renderer!             db renderer-id)
       :dispatch [:ui/render-element-from-queue?! renderer-id]}))

(r/reg-event-fx :ui/renderer-unmounted-illegal
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  (fn [cofx [_ renderer-id]]
      [:core/error-catched {:error ILLEGAL-UNMOUNTING-ERROR :cofx cofx}]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- wrapper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  ;  {:attributes (map)(opt)}
  [renderer-id {:keys [attributes]}]
  (let [dom-id (engine/renderer-id->dom-id renderer-id)
        wrapper-attributes (assoc attributes :id (hiccup/value dom-id))]
       [:div wrapper-attributes]))

(defn- elements
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  ;  {:element (component)}
  [renderer-id {:keys [element] :as renderer-props}]
  (letfn [(f [wrapper element-id]
             (conj wrapper ^{:key element-id} [element element-id]))]
         (let [element-order @(r/subscribe [:ui/get-element-order renderer-id])]
              (reduce f (wrapper renderer-id renderer-props) element-order))))

(defn renderer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  [renderer-id renderer-props]
  ; BUG#0009
  ; Ha egy renderer nem jelenít meg elemeket, akkor a wrapper komponense nincs
  ; a React-fába csatolva.
  ; Ennek következtében a {flex-direction: row-reverse} beállítással használt
  ; [:div#x-app-ui-structure] elemben addig a pillanatig, amíg nem jelenik meg
  ; az [:div#x-app-surface] elem, addig az [:div#x-app-sidebar] elem a viewport
  ; jobb oldalán jelenik meg.
  ; Ennek elkerülése érdekében a wrapper komponensek mindenképpen megjelennek,
  ; így nem fordulhat elő olyan pillanat az applikáció betöltése közben, hogy
  ; a sidebar a surface hiánya miatt a jobb oldalon jelenne meg.
  (let [element-order @(r/subscribe [:ui/get-element-order renderer-id])]
       (if (vector/nonempty? element-order)
           [elements renderer-id renderer-props]
           [wrapper  renderer-id renderer-props])))

(defn component
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  ;  {:alternate-renderer (keyword)(opt)
  ;    W/ {:required? true}
  ;   :element (component)
  ;   :max-elements-rendered (integer)(opt)
  ;    Default: DEFAULT-MAX-ELEMENTS-RENDERED
  ;   :queue-behavior (keyword)(opt)
  ;    :ignore, :push, :wait
  ;    Default: :wait
  ;   :required? (boolean)(opt)
  ;    Default: false
  ;   :rerender-same? (boolean)(opt)
  ;    Default: false}
  [renderer-id renderer-props]
  (let [renderer-props (renderer-props-prototype renderer-props)]
       (reagent/lifecycles (engine/renderer-id->dom-id renderer-id)
                           {:reagent-render         (fn []             [renderer               renderer-id renderer-props])
                            :component-will-unmount (fn [] (r/dispatch [:ui/destruct-renderer! renderer-id renderer-props]))
                            :component-did-mount    (fn [] (r/dispatch [:ui/init-renderer!     renderer-id renderer-props]))})))
