
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.14
; Description:
; Version: v3.4.0
; Compatibility: x4.4.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.renderer
    (:require [mid-fruits.candy     :refer [param]]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.loop      :refer [reduce+wormhole]]
              [mid-fruits.map       :as map]
              [mid-fruits.time      :as time]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-ui.engine      :as engine]

              ; TEMP
              ; DEBUG
              [mid-fruits.time :as time]))



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
; @name alternate-id
;  Abban az esetben ha a renderer {:required? true} tulajdonsággal rendelkezik
;  és a renderer {:alternate-id ...} tulajdonságaként megadott azonosítóval
;  rendelkező helyettesítő renderer, elemeket jelenít meg, akkor a renderer
;  React-fából való esetleges lecsatolása nem minősül rendszerhibának.
;  Pl.: {:alternate-id :alternate-partition/elements}
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
(def ILLEGAL-UNMOUNTING-ERROR "Illegal unmounting error")

; @constant (ms)
;  A UI elem animált megjelenítésére rendelkezésre álló idő.
(def REVEAL-ANIMATION-TIMEOUT 350)

; @constant (ms)
;  A UI elem animált eltűntetésére rendelkezésre álló idő.
;
; BUG#4701 (def HIDE-ANIMATION-TIMEOUT 350)
(def HIDE-ANIMATION-TIMEOUT 450)

; DEBUG
; @constant (ms)
(def RENDER-DELAY-OFFSET 0)
; - Ha a render-delay 100 ms várakozásra volt állítva, akkor az egymás után különböző azonosítóval
;   kirenderelt surface elemek villanva jelentek meg (100 ms különbséggel).
; - Ha pedig különböző surface-ek ugyanazt az azonosítót használták, ami több okból sem jó, akkor
;   nem jelentkezett ez a jelenség, mivel nem történt re-render, csak update-elte a surface a tartalmát.
; - Próbaképpen át lett állítva 0 ms várakozásra 2021. 10. 26.
;   Ha ez nem okoz semmilyen problémát hosszútávon, akkor maradjon az új érték megtartva.
;(def RENDER-DELAY-OFFSET 100)

; @constant (ms)
(def DESTROY-DELAY-OFFSET 100)

; @constant (integer)
(def DEFAULT-MAX-ELEMENTS-RENDERED 32)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn renderer-props->db-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) renderer-props
  ;
  ; @return (map)
  [renderer-props]
  (map/inherit (param renderer-props)
               [:alternate-id :destructor :initializer :required? :partition-id
                :rerender-same? :queue-behavior]))

(defn renderer-props->component-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) renderer-props
  ;
  ; @return (map)
  [renderer-props]
  (map/inherit (param renderer-props)
               [:attributes :element]))

(defn- element-props->reveal-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-props
  ;  {:reveal-animated? (boolean)}
  ;
  ; @return (boolean)
  [{:keys [reveal-animated?]}]
  (boolean reveal-animated?))

(defn- element-props->hide-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-props
  ;  {:hide-animated? (boolean)}
  ;
  ; @return (boolean)
  [{:keys [hide-animated?]}]
  (boolean hide-animated?))

(defn- element-props->update-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-props
  ;  {:update-animated? (boolean)}
  ;
  ; @return (boolean)
  [{:keys [update-animated?]}]
  (boolean update-animated?))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- partition-initializer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  ;  {:max-elements-rendered (integer)(opt)}
  ;
  ; @return (metamorphic-event)
  [renderer-id {:keys [max-elements-rendered] :as renderer-props}]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       [:x.app-db/reg-partition! partition-id
        {:ordered? true :meta-items renderer-props}]))

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

(defn- get-renderer-state
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (map)
  [db [_ renderer-id]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (r db/get-partition-state db partition-id)))

(a/reg-sub ::get-renderer-state get-renderer-state)

(defn get-rendered-element-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vector)
  [db [_ renderer-id]]
  (let [renderer-state (r get-renderer-state db renderer-id)]
       (db/partition-state->data-order renderer-state)))

(defn get-invisible-element-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vector)
  [db [_ renderer-id]]
  (let [renderer-state (r get-renderer-state db renderer-id)]
       (db/partition->meta-item renderer-state :invisible-elements)))

(defn get-visible-element-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vector)
  [db [_ renderer-id]]
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

(defn get-upper-visible-element-id
  ; @param (keyword) renderer-id
  ;
  ; @return (keyword)
  [db [_ renderer-id]]
  (vector/last-item (r get-visible-element-order db renderer-id)))

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
  (let [renderer-state (r get-renderer-state db renderer-id)]
       (db/partition->meta-item renderer-state :max-elements-rendered)))

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
  (let [renderer-state (r get-renderer-state db renderer-id)]
       (db/partition->meta-item renderer-state :queue-behavior)))

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
  (let [renderer-state (r get-renderer-state db renderer-id)]
       (boolean (db/partition->meta-item renderer-state :required?))))

(defn- get-alternate-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (keyword)
  [db [_ renderer-id]]
  (let [renderer-state (r get-renderer-state db renderer-id)]
       (db/partition->meta-item renderer-state :alternate-id)))

(defn- renderer-require-error?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (if-let [alternate-id (r get-alternate-id db renderer-id)]
          (boolean (and (r renderer-required?   db renderer-id)
                        (r no-visible-elements? db alternate-id)))
          (boolean (r renderer-required? db renderer-id))))

(defn- renderer-reserved?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [renderer-state (r get-renderer-state db renderer-id)]
       (boolean (db/partition->meta-item renderer-state :reserved?))))

(defn- renderer-free?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (not (r renderer-reserved? db renderer-id)))

(defn get-element-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ renderer-id element-id]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (get-in db (db/path partition-id element-id))))

(defn get-element-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (keyword) prop-id
  ;
  ; @return (*)
  [db [_ renderer-id element-id prop-id]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (get-in db (db/path partition-id element-id prop-id))))

(defn reveal-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (let [element-props (r get-element-props db renderer-id element-id)]
       (element-props->reveal-element-animated? element-props)))

(defn hide-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (let [element-props (r get-element-props db renderer-id element-id)]
       (element-props->hide-element-animated? element-props)))

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
  (boolean (and (r element-rendered?       db renderer-id element-id)
                (not (r element-invisible? db renderer-id element-id)))))

(defn rerender-same?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [renderer-state (r get-renderer-state db renderer-id)]
       (boolean (db/partition->meta-item renderer-state :rerender-same?))))

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
      (param RENDER-DELAY-OFFSET)))

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
           (param RENDER-DELAY-OFFSET))))

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
      (param RENDER-DELAY-OFFSET)))

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
      (param DESTROY-DELAY-OFFSET)))

(defn- render-element-now?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id _]]
  (boolean (and (not (r renderer-reserved?         db renderer-id))
                (or  (r pushed-rendering-enabled?  db renderer-id)
                     (not (r max-elements-reached? db renderer-id))))))

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
  (let [renderer-state (r get-renderer-state db renderer-id)]
       (db/partition->meta-item renderer-state :rendering-queue)))

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
  (boolean (and (r element-rendered? db renderer-id element-id)
                (r rerender-same?    db renderer-id))))

(defn- update-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id element-props]]
  (boolean (and (r element-rendered?   db renderer-id element-id)
                (not (r rerender-same? db renderer-id))
                (element-props->update-element-animated? element-props))))

(defn- update-element-static?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id element-props]]
  (boolean (and (r element-rendered?   db renderer-id element-id)
                (not (r rerender-same? db renderer-id))
                (not (element-props->update-element-animated? element-props)))))

(defn- push-element?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id _]]
  (boolean (and (r max-elements-reached?     db renderer-id)
                (r pushed-rendering-enabled? db renderer-id)
                (not (r element-rendered?    db renderer-id element-id)))))

(defn- render-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id element-props]]
  (boolean (and (not (r max-elements-reached? db renderer-id))
                (not (r element-rendered?     db renderer-id element-id))
                (element-props->reveal-element-animated? element-props))))

(defn- render-element-static?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id element-props]]
  (boolean (and (not (r max-elements-reached? db renderer-id))
                (not (r element-rendered?     db renderer-id element-id))
                (not (element-props->reveal-element-animated? element-props)))))

(defn- destroy-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (boolean (and (r element-visible?       db renderer-id element-id)
                (r hide-element-animated? db renderer-id element-id))))

(defn- destroy-element-static?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (boolean (and (r element-visible?            db renderer-id element-id)
                (not (r hide-element-animated? db renderer-id element-id)))))

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
       (reduce+wormhole (fn [event-list element-id delay]
                            (let [event {:ms delay :dispatch [:x.app-ui/destroy-element! renderer-id element-id]}
                                  destroying-delay (r get-destroying-delay db renderer-id element-id)]
                                 [(vector/conj-item event-list event)
                                  (+ delay destroying-delay)]))
                        (param [])
                        (param visible-element-order)
                        (param 0))))

(defn get-visible-elements-destroying-duration
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (ms)
  [db [_ renderer-id]]
  (let [visible-element-order (r get-visible-element-order db renderer-id)]
       (reduce (fn [duration element-id]
                   (let [destroying-delay (r get-destroying-delay db renderer-id element-id)]
                        (+ duration destroying-delay)))
               (param 0)
               (param visible-element-order))))

(defn get-render-log
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (keyword) action-id
  ;
  ; @usage
  ;  (r renderer/get-render-log db :bubbles :my-bubble :render-requested-at)
  ;
  ; @return (ms)
  [db [_ renderer-id element-id action-id]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (get-in db (db/meta-item-path partition-id :render-log element-id action-id))))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- update-render-log!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (keyword) action-id
  ;
  ; @usage
  ;  (r update-render-log! db :bubbles :my-bubble :render-requested-at)
  ;
  ; @return (map)
  [db [_ renderer-id element-id action-id]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (assoc-in db (db/meta-item-path partition-id :render-log element-id action-id)
                    (time/elapsed))))

(defn- reserve-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (map)
  [db [_ renderer-id]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (assoc-in db (db/meta-item-path partition-id :reserved?)
                    (param true))))

(a/reg-event-db :x.app-ui/reserve-renderer! reserve-renderer!)

(defn- free-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (map)
  [db [_ renderer-id]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (assoc-in db (db/meta-item-path partition-id :reserved?)
                    (param false))))

(defn- store-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (map)
  [db [event-id renderer-id element-id element-props]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (-> db (update-render-log! [event-id renderer-id  element-id :rendered-at])
              (db/add-data-item!  [event-id partition-id element-id element-props]))))

(defn- remove-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [event-id renderer-id element-id]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (-> db (update-render-log!   [event-id renderer-id  element-id :props-removed-at])
              (db/remove-data-item! [event-id partition-id element-id]))))

(a/reg-event-db :x.app-ui/remove-element! remove-element!)

(defn- update-element-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (map)
  [db [event-id renderer-id element-id element-props]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (-> db (update-render-log! [event-id renderer-id element-id :updated-at])
              (assoc-in (db/path partition-id element-id)
                        (param   element-props)))))

(defn set-element-prop!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (keyword) prop-id
  ; @param (*) prop-value
  ;
  ; @return (map)
  [db [_ renderer-id element-id prop-id prop-value]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (assoc-in db (db/path partition-id element-id prop-id)
                    (param prop-value))))

(a/reg-event-db :x.app-ui/set-element-prop! set-element-prop!)

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
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (update-in db [partition-id :data-order] vector/remove-item element-id)))

(a/reg-event-db :x.app-ui/stop-element-rendering! stop-element-rendering!)

(defn- render-element-later!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (map)
  [db [_ renderer-id element-id element-props]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (update-in db (db/meta-item-path partition-id :rendering-queue)
                  vector/conj-item [element-id element-props])))

(a/reg-event-db :x.app-ui/render-element-later! render-element-later!)

(defn- trim-rendering-queue!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (map)
  [db [_ renderer-id]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (update-in db (db/meta-item-path partition-id :rendering-queue)
                  vector/remove-nth-item 0)))

(a/reg-event-db :x.app-ui/trim-rendering-queue! trim-rendering-queue!)

(defn- empty-rendering-queue!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (map)
  [db [_ renderer-id]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (assoc-in db (db/meta-item-path partition-id :rendering-queue)
                    (param []))))

(a/reg-event-db :x.app-ui/empty-rendering-queue! empty-rendering-queue!)

(defn- mark-element-as-invisible!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ renderer-id element-id]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (update-in db (db/meta-item-path partition-id :invisible-elements)
                  vector/conj-item element-id)))

(a/reg-event-db :x.app-ui/mark-element-as-invisible! mark-element-as-invisible!)

(defn- unmark-element-as-invisible!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ renderer-id element-id]]
  (let [partition-id (engine/renderer-id->partition-id renderer-id)]
       (update-in db (db/meta-item-path partition-id :invisible-elements)
                  vector/remove-item element-id)))

(a/reg-event-db :x.app-ui/unmark-element-as-invisible! unmark-element-as-invisible!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/init-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  (fn [{:keys [db]} [_ renderer-id renderer-props]]
      (let [partition-initializer (a/prot renderer-id renderer-props partition-initializer)]
           {:dispatch partition-initializer})))

(a/reg-event-fx
  :x.app-ui/destruct-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  (fn [{:keys [db]} [_ renderer-id _]]
      {:dispatch-if [(r renderer-require-error?   db renderer-id)
                     [::->renderer-unmounted-illegal renderer-id]]}))

(a/reg-event-fx
  :x.app-ui/render-element-animated!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      ;(a/console (str "Render element animated - " (time/elapsed)))
      {:db             (r store-element! db renderer-id element-id element-props)
       :dispatch-later [{:ms REVEAL-ANIMATION-TIMEOUT :dispatch [:x.app-ui/->rendering-ended renderer-id]}]}))

(a/reg-event-fx
  :x.app-ui/render-element-static!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      ;(a/console (str "Render element static - " (time/elapsed)))
      {:db       (r store-element! db renderer-id element-id element-props)
       :dispatch [:x.app-ui/->rendering-ended renderer-id]}))

(a/reg-event-fx
  :x.app-ui/rerender-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      (let [rerender-delay (r get-rerender-delay db renderer-id element-id)]
           {:dispatch [:x.app-ui/destroy-element! renderer-id element-id]
            :dispatch-later
            [{:ms rerender-delay :dispatch
              [:x.app-ui/select-rendering-mode! renderer-id element-id element-props]}]})))

(a/reg-event-fx
  :x.app-ui/update-element-animated!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      {:db       (r update-element-props!  db renderer-id element-id element-props)
       :dispatch [:x.app-ui/->rendering-ended renderer-id]}))

(a/reg-event-fx
  :x.app-ui/update-element-static!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      {:db       (r update-element-props!  db renderer-id element-id element-props)
       :dispatch [:x.app-ui/->rendering-ended renderer-id]}))

(a/reg-event-fx
  :x.app-ui/push-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      (let [pushing-delay            (r get-pushing-delay            db renderer-id)
            lower-visible-element-id (r get-lower-visible-element-id db renderer-id)]
           {:dispatch [:x.app-ui/destroy-element! renderer-id lower-visible-element-id]
            :dispatch-later
            [{:ms pushing-delay :dispatch
              [:x.app-ui/select-rendering-mode! renderer-id element-id element-props]}]})))

(a/reg-event-fx
  :x.app-ui/select-rendering-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      ;(a/console (str "Select rendering mode - " (time/elapsed)))
      (cond (r push-element?                 db renderer-id element-id element-props)
            [:x.app-ui/push-element!            renderer-id element-id element-props]
            (r rerender-element?             db renderer-id element-id element-props)
            [:x.app-ui/rerender-element!        renderer-id element-id element-props]
            (r update-element-animated?      db renderer-id element-id element-props)
            [:x.app-ui/update-element-animated! renderer-id element-id element-props]
            (r update-element-static?        db renderer-id element-id element-props)
            [:x.app-ui/update-element-static!   renderer-id element-id element-props]
            (r render-element-animated?      db renderer-id element-id element-props)
            [:x.app-ui/render-element-animated! renderer-id element-id element-props]
            (r render-element-static?        db renderer-id element-id element-props)
            [:x.app-ui/render-element-static!   renderer-id element-id element-props]

            ; Ha a renderer {:queue-behavior :wait :rerender-same? false} beállítással
            ; renderelne egy már kirenderelt element, akkor egyik render esemény sem
            ; történik meg, ezért szükséges a renderert felszabadítani.
            :else [:x.app-ui/->rendering-ended renderer-id])))

(a/reg-event-fx
  :x.app-ui/request-rendering-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [event-id renderer-id element-id element-props]]
      ;(a/console (str "Render element - " (time/elapsed)))
      (if (r render-element-now? db renderer-id element-id)
          {:db       (-> db (reserve-renderer!  [event-id renderer-id])
                            (update-render-log! [event-id renderer-id element-id :render-requested-at]))
           :dispatch [:x.app-ui/select-rendering-mode! renderer-id element-id element-props]}
          {:dispatch [:x.app-ui/render-element-later!  renderer-id element-id element-props]})))

(a/reg-event-fx
  :x.app-ui/render-element-from-queue?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  (fn [{:keys [db]} [_ renderer-id]]
      (if-let [[element-id element-props] (r get-next-rendering db renderer-id)]
              {:db (r trim-rendering-queue! db renderer-id)
               :dispatch-later
               [{:ms RENDER-DELAY-OFFSET :dispatch
                 [:x.app-ui/render-element! renderer-id element-id element-props]}]})))

(a/reg-event-fx
  :x.app-ui/hide-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ _ element-id]]
      ;(a/console (str "Hide element - " (time/elapsed)))
      [:x.app-environment.element-handler/set-attribute!
        (keyword/to-dom-value element-id) "data-animation" "hide"]))

(a/reg-event-fx
  :x.app-ui/destroy-element-animated!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  (fn [{:keys [db]} [event-id renderer-id element-id]]
      ;(a/console (str "Destroy element animated - " (time/elapsed)))
      {:db       (r mark-element-as-invisible! db renderer-id element-id)
       ; 1.
       :dispatch [:x.app-ui/hide-element! renderer-id element-id]
       ; 2.
       :dispatch-later
       [{:ms HIDE-ANIMATION-TIMEOUT
         :dispatch-n [[:x.app-ui/stop-element-rendering!      renderer-id element-id]
                      [:x.app-ui/remove-element!              renderer-id element-id]
                      [:x.app-ui/unmark-element-as-invisible! renderer-id element-id]
                      [:x.app-ui/render-element-from-queue?!  renderer-id]]}]}))

(a/reg-event-fx
  :x.app-ui/destroy-element-static!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  (fn [{:keys [db]} [event-id renderer-id element-id]]
      ;(a/console (str "Destroy element static - " (time/elapsed)))
      {:db       (-> db (stop-element-rendering! [event-id renderer-id element-id])
                        (remove-element!         [event-id renderer-id element-id]))
       :dispatch [:x.app-ui/render-element-from-queue?! renderer-id]}))

(a/reg-event-fx
  :x.app-ui/destroy-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ renderer-id element-id]]
      ;(a/console (str "Destroy element - " (time/elapsed)))
      {:db       (r update-render-log!                   db renderer-id element-id :destroyed-at)
       :dispatch (cond (r destroy-element-animated?      db renderer-id element-id)
                       [:x.app-ui/destroy-element-animated! renderer-id element-id]
                       (r destroy-element-static?        db renderer-id element-id)
                       [:x.app-ui/destroy-element-static!   renderer-id element-id])}))

(a/reg-event-fx
  :x.app-ui/destroy-all-elements!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  (fn [{:keys [db]} [_ renderer-id]]
      (let [destroying-event-list (r get-visible-elements-destroying-event-list db renderer-id)]
           {:db             (r empty-rendering-queue! db renderer-id)
            :dispatch-later (param destroying-event-list)})))

(a/reg-event-fx
  :x.app-ui/empty-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  (fn [{:keys [db]} [_ renderer-id]]
      [:x.app-ui/destroy-all-elements! renderer-id]))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/->rendering-ended
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  (fn [{:keys [db]} [_ renderer-id]]
      {:db       (r free-renderer!                   db renderer-id)
       :dispatch [:x.app-ui/render-element-from-queue?! renderer-id]}))

(a/reg-event-fx
  ::->renderer-unmounted-illegal
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  (fn [_ [_ renderer-id]]
      [:x.app-core/->error-catched {:error ILLEGAL-UNMOUNTING-ERROR :renderer-id renderer-id}]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- stated-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) element
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:destructor (metamorphic-event)(opt)
  ;   :initializer (metamorphic-event)(opt)}
  ;
  ; @return (component)
  [element element-id {:keys [destructor initializer] :as element-props}]
  [components/stated element-id {:component    element
                                 :destructor   destructor
                                 :initializer  initializer
                                 :static-props element-props}])

(defn- wrapper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  ;  {:attributes (map)(opt)}
  ; @param (map) renderer-state
  ;
  ; @return (hiccup)
  [renderer-id {:keys [attributes]} _]
  (let [wrapper-attributes (assoc attributes :id (keyword/to-dom-value renderer-id))]
       [:div wrapper-attributes]))

(defn- elements
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  ;  {:element (component)}
  ; @param (map) renderer-state
  ;
  ; @return (hiccup)
  [renderer-id {:keys [element] :as renderer-props} renderer-state]
  (let [elements (db/partition->data-items renderer-state)]
       (reduce (fn [wrapper element-id]
                   (let [element-props (get elements element-id)]
                        (vector/conj-item (param wrapper)
                                         ^{:key element-id}
                                          [stated-element element element-id element-props])))
               (wrapper renderer-id renderer-props renderer-state)
               (db/partition-state->data-order renderer-state))))

(defn renderer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  ; @param (map) renderer-state
  ;
  ; @return (hiccup)
  [renderer-id renderer-props renderer-state]
  (if (db/partition->partition-nonempty? renderer-state)
      [elements renderer-id renderer-props renderer-state]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  ;  {:alternate-id (keyword)(opt)
  ;    Only w/ {:required? true}
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
  ;
  ; @usage
  ;  (defn my-element [element-id element-props}])
  ;  [ui/renderer :my-renderer {:element #'my-element}]
  ;
  ; @return (component)
  [renderer-id renderer-props]
  (let [dom-id         (engine/renderer-id->dom-id renderer-id)
        renderer-props (a/prot renderer-props renderer-props-prototype)]
       [components/stated dom-id
         {:component    #'renderer
          :static-props renderer-props
          :destructor   [:x.app-ui/destruct-renderer! renderer-id renderer-props]
          :initializer  [:x.app-ui/init-renderer!     renderer-id renderer-props]
          :subscriber   [::get-renderer-state         renderer-id]}]))
