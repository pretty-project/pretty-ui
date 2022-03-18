
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v2.2.4
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.sortable.core
    (:require ["@dnd-kit/core"      :as core]
              ["@dnd-kit/sortable"  :as sortable]
              ["@dnd-kit/utilities" :as utilities]
              [mid-fruits.candy     :as candy :refer [param return]]
              [mid-fruits.json      :as json]
              [mid-fruits.loop      :refer [reduce-indexed]]
              [mid-fruits.map       :refer [dissoc-in]]
              [mid-fruits.mixed     :as mixed]
              [mid-fruits.string    :as string]
              [mid-fruits.vector    :as vector]
              [reagent.api          :as reagent]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name sortable-items
;  A rendezendő elemek felsorolása egy vektorban.
;  Pl.: [{:value "My item"} {:value "Your item"}]
;
; @name sortable-item
;  Az egyes rendezendő elemek.
;  Pl.: {:value "My item"}
;
; @name sortable-item-id
;  Az egyes rendezendő elemek egyedi azonosítója.
;  A rendezendő elemek sorrendjének megváltozása után az azonosítóval nem azonosítható az elem.
;  Pl.: "a991876d-6887-4f04-8525-328f090cde5"
;
; @name sortable-element
;  Az egyes rendezendő elemeket megjelenítő komponensek.
;
; @name render-dex
;  Az egyes rendezendő elemek kirenderelési sorrendje.



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
; Ha a {:value-path [...]} tulajdonságként átadott Re-Frame adatbázis útvonalon
; tárolt vektor elemeihez hozzáadsz vagy közülük törölsz, és nem a vektor utolsó
; elemein végzed a műveletet, akkor a felsorolt elemek megfelelő renderelése
; érdekében használd az add-sortable-item! és remove-sortable-item! adatbázis
; függvényeket.



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @dnd-kit/core
(def closest-center  core/closestCenter)
(def keyboard-sensor core/KeyboardSensor)
(def pointer-sensor  core/PointerSensor)
(def touch-sensor    core/TouchSensor)
(def use-sensor      core/useSensor)
(def use-sensors     core/useSensors)

; @dnd-kit/sortable
(def array-move                     sortable/arrayMove)
(def sortable-keyboard-coordinates  sortable/sortableKeyboardCoordinates)
(def vertical-list-sorting-strategy sortable/verticalListSortingStrategy)
(def rect-sorting-strategy          sortable/rectSortingStrategy)
(def use-sortable                   sortable/useSortable)

; @dnd-kit/utilities
(def CSS utilities/CSS)



;; -- Adapted classes ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(def dnd-context      (reagent/adapt-react-class core/DndContext))
(def drag-overlay     (reagent/adapt-react-class core/DragOverlay))
(def sortable-context (reagent/adapt-react-class sortable/SortableContext))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn make-sensors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [keyboard-sensor-props {:coordinateGetter sortable-keyboard-coordinates}]
       (use-sensors (use-sensor pointer-sensor)
                    (use-sensor touch-sensor)
                    (use-sensor keyboard-sensor (json/json->clj keyboard-sensor-props)))))

(defn- sortable-elements-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :style (map)(opt)}
  ;
  ; @return (map)
  ;  {:class (string or vector)
  ;   :id (string)
  ;   :style (map)}
  [sortable-id {:keys [class style]}]
  (cond-> {:id (a/dom-value sortable-id)}
          (some? class) (assoc :class class)
          (some? style) (assoc :style style)))

(defn- sortable-item-dex->sortable-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (integer) sortable-item-dex
  ;
  ; @example
  ;  (sortable-item-dex->sortable-item-id :my-sortable 3)
  ;  =>
  ;  "my-sortable--3"
  ;
  ; @return (string)
  [sortable-id sortable-item-dex]
  (str (name  sortable-id)
       (param "--")
       (param sortable-item-dex)))

(defn- sortable-item-id->sortable-item-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) sortable-item-id
  ;
  ; @example
  ;  (sortable-item-id->sortable-item-dex "my-sortable--3")
  ;  =>
  ;  3
  ;
  ; @return (integer)
  [sortable-item-id]
  (let [sortable-item-dex (string/after-last-occurence sortable-item-id "--")]
       (string/to-integer sortable-item-dex)))

(defn- event->active-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (?) event
  ;
  ; @return (string)
  [event]
  (aget event "active" "id"))

(defn- event->over-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (?) event
  ;
  ; @return (string)
  [event]
  (aget event "over" "id"))

(defn- transform-data->transform-css
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) transform-data
  ;  {:scaleX (integer)
  ;   :scaleY (integer)
  ;   :x (integer)
  ;   :y (integer)}
  ;
  ; @example
  ;  (transform-data->transform-css {:x 0 :y 10 :scaleX 1 :scaleY 1})
  ;  =>
  ;  "translate3d(0px, 10px, 0) scaleX(1) scaleY(1)"
  ;
  ; @return (string)
  [transform-data]
  (.toString (.-Transform CSS)
             (clj->js transform-data)))

(defn- transform-data<-mark-active-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) transform-data
  ; @param (boolean) active-item?
  ;
  ; @return (map)
  ;  {:scaleX (string)
  ;   :scaleY (string)}
  [transform-data active-item?]
  (if active-item? ; If item is active
                   (merge transform-data {:scaleX "1.05"
                                          :scaleY "1.05"})
                   ; If item is NOT active
                   (return transform-data)))

(defn- view-props->sortable-element-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) view-props
  ;  {:common-props (map)(opt)
  ;   :element (component)
  ;   :sortable-items (vector)}
  ; @param (string) sortable-item-id
  ; @param (integer) render-dex
  ;
  ; @return (map)
  ;  {:common-props (map)
  ;   :element (component)
  ;   :key (string)
  ;   :render-dex (integer)
  ;   :sortable-id (keyword)
  ;   :sortable-item (*)
  ;   :sortable-item-id (string)}
  [sortable-id {:keys [common-props element sortable-items]} sortable-item-id render-dex]
  ; Mivel a React az [f:> [my-component ...]] alakban, átadott komponens számára
  ; egyetlen paramétert ad át, ezért egy térképben rendezve adódik a [sortable-element ...]
  ; komponens számára az összes szükséges adat.
  (let [sortable-item (vector/nth-item sortable-items render-dex)]
       {:common-props     common-props
        :element          element
        :key              sortable-item-id
        :render-dex       render-dex
        :sortable-item    sortable-item
        :sortable-id      sortable-id
        :sortable-item-id sortable-item-id}))

(defn- sortable-item-id->sortable-element-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) sortable-item-id
  ;
  ; @return (map)
  ;  {:attributes (map)
  ;    {:aria-describedby (string)
  ;     :aria-pressed (boolean)(opt)
  ;     :aria-roledescription (string)
  ;     :role (string)
  ;     :tabIndex (integer)}
  ;   :listeners (map)
  ;    {:onKeyDown (function)
  ;     :onPointerDown (function)
  ;     :onTouchStart (function)}
  ;   :setNodeRef (function)
  ;    Set up reference node function
  ;   :transform (map)(opt)
  ;   :transition (string)(opt)}
  [sortable-item-id]
  (let [sortable-element-data (use-sortable #js {:id sortable-item-id})]
       (json/json->clj sortable-element-data)))

(defn- sortable-element-data->sortable-sensor-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) sortable-element-data
  ;  {:attributes (map)
  ;    {:aria-pressed (boolean)(opt)
  ;     :tabIndex (integer)}
  ;   :listeners (map)
  ;    {:onKeyDown (function)
  ;     :onPointerDown (function)
  ;     :onTouchStart (function)}
  ;   :setNodeRef (function)
  ;    Set up reference node function}
  ;
  ; @return (map)
  ;  {:data-position (string)
  ;   :onKeyDown (function)
  ;   :onPointerDown (function)
  ;   :onTouchStart (function)
  ;   :ref (function)
  ;   :style (map)
  ;    {:cursor (string)
  ;     :touch-action (string)}
  ;   :tabIndex (integer)}
  [{:keys [attributes listeners setNodeRef]}]
  (let [active-item? (get attributes :aria-pressed)]
       (merge {:data-position (a/dom-value :right)
               :ref          #(setNodeRef %)

               ; WARNING! DEPRECATED!
               ; A drag-overlay-element komponens használatának bevezetése óta,
               ; a sortable-sensor komponensen nem szükséges az inline-style
               ; tulajdonságokat beállítani.
               :style {; Touch-sensor cursor
                       :cursor (if active-item? "grabbing" "grab")

                       ; BUG#7661
                       ;  Egy elem mozgatása közben az onTouchMove esemény hatással
                       ;  van az oldal scroll értékére, ezért a {:touch-action "none"}
                       ;  tulajdonság használata nélkül egyszerre változna a scroll
                       ;  értéke és az elem pozíciója.
                       :touch-action (param "none")}}

               ; WARNING! DEPRECATED!

              (select-keys attributes [:tabIndex])
              (param       listeners))))

(defn- sortable-element-data->sortable-element-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) sortable-element-data
  ;  {:attributes (map)
  ;    {:aria-describedby (string)
  ;     :aria-pressed (boolean)(opt)
  ;     :aria-roledescription (string)
  ;     :role (string)}
  ;   :transform (map)(opt)
  ;   :transition (string)(opt)}
  ;
  ; @return (map)
  ;  {:aria-describedby (string)
  ;   :aria-pressed (boolean)(opt)
  ;   :aria-roledescription (string)
  ;   :data-drag-status (string)
  ;   :role (string)
  ;   :style (map)
  ;    {:transform (string)
  ;     :transition (string)
  ;     :z-index (integer)}}
  [{:keys [attributes transform transition]}]
  (let [active-item?   (get attributes :aria-pressed)
        transform-data (transform-data<-mark-active-item transform active-item?)]
       (merge {:style {:transform  (transform-data->transform-css transform-data)
                       :transition (param transition)

                       ; Legyen nagyobb, mint a többi sortable-element ... és legyen kisebb,
                       ; mint az drag-overlay-element komponens {:z-index ...} tulajdonsága!
                       :z-index    (if active-item? 999)}

               :data-drag-status   (if active-item? "active" "inactive")}
              (select-keys attributes [:aria-describedby :aria-pressed :aria-roledescription :role]))))

(defn- view-props->render-sortable-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:sortable-items (vector)}
  ;
  ; @return (boolean)
  [{:keys [sortable-items]}]
  (vector/nonempty? sortable-items))

(defn- sortable-items->initial-item-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (vector) sortable-items
  ;
  ; @example
  ;  (sortable-items->initial-item-order :my-sortable ["My item" "Your item"])
  ;  =>
  ;  ["my-sortable--0" "my-sortable--1"]
  ;
  ; @return (strings in vector)
  [sortable-id sortable-items]
  (reduce-indexed (fn [initial-item-order sortable-item-dex sortable-item]
                      (let [sortable-item-id (sortable-item-dex->sortable-item-id sortable-id sortable-item-dex)]
                           (conj initial-item-order sortable-item-id)))
                  [] sortable-items))

(defn- view-props->sortable-context-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) view-props
  ;  {:sortable-item-order (strings in vector)}
  ;
  ; @return (map)
  ;  {:items (strings in json array)
  ;   :strategy (function)}
  [sortable-id {:keys [sortable-item-order]}]
  {:items    (clj->js sortable-item-order)
   :strategy (param   rect-sorting-strategy)})

(defn- view-props->dnd-context-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) view-props
  ;
  ; @return (map)
  ;  {:collisionDetection (function)
  ;   :onDragEnd (function)
  ;   :sensors (function)}
  [sortable-id _]
  {:collisionDetection  (param closest-center)
   :onDragEnd          #(a/dispatch-sync [::handle-drag-end! sortable-id %])
   :sensors             (make-sensors)})



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-sortable-prop
  ; @param (keyword) sortable-id
  ; @param (keyword) prop-key
  ;
  ; @usage
  ;  (r sortable/get-sortable-prop db :my-sortable :value-path)
  ;
  ; @return (*)
  [db [_ sortable-id prop-key]]
  (get-in db [:plugins :item-sorter/meta-items sortable-id prop-key]))

; @usage
;  [:sortable/get-sortable-prop :my-sortable :value-path]
(a/reg-sub :sortable/get-sortable-prop get-sortable-prop)

(defn get-sortable-items
  ; @param (keyword) sortable-id
  ;
  ; @return (vector)
  [db [_ sortable-id]]
  (if-let [value-path (get-in db [:plugins :item-sorter/meta-items sortable-id :value-path])]
          (let [sortable-items (get-in db value-path)]
               (mixed/to-vector sortable-items))
          (return [])))

(defn get-sortable-item-count
  ; @param (keyword) sortable-id
  ;
  ; @return (integer)
  [db [_ sortable-id]]
  (let [sortable-items (r get-sortable-items db sortable-id)]
       (count sortable-items)))

(defn- updated-item-order-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ;
  ; @return (boolean)
  [db [_ sortable-id]]
  (let [updated-item-order (r get-sortable-prop  db sortable-id :updated-item-order)
        sortable-items     (r get-sortable-items db sortable-id)]
       ; Ha a {:value-path [...]} tulajdonságként átadott Re-Frame adatbázis útvonalon
       ; tárolt vektor elemeinek száma megváltozik, akkor az updated-item-order
       ; érték érvénytelenné válik.
       (= (count updated-item-order)
          (count sortable-items))))

(defn- get-sortable-item-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ;
  ; @return (strings in vector)
  [db [_ sortable-id]]
  (let [updated-item-order (r get-sortable-prop db sortable-id :updated-item-order)]
       (if (r updated-item-order-valid? db sortable-id)
           (return updated-item-order)
           (let [sortable-items (r get-sortable-items db sortable-id)]
                (sortable-items->initial-item-order sortable-id sortable-items)))))

(defn- get-origin-item-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (?) event
  ;
  ; @return (integer)
  [db [_ sortable-id event]]
  (let [sortable-item-order (r get-sortable-item-order db sortable-id)
        origin-item-id      (event->active-item-id event)]
       (vector/item-first-dex sortable-item-order origin-item-id)))

(defn- get-target-item-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (?) event
  ;
  ; @return (integer)
  [db [_ sortable-id event]]
  (let [sortable-item-order (r get-sortable-item-order db sortable-id)
        target-item-id      (event->over-item-id event)]
       (vector/item-first-dex sortable-item-order target-item-id)))

(defn- sortable-item-order-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (?) event
  ;
  ; @return (boolean)
  [db [_ sortable-id event]]
  (let [origin-item-dex (r get-origin-item-dex db sortable-id event)
        target-item-dex (r get-target-item-dex db sortable-id event)]
       (not= origin-item-dex target-item-dex)))

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ;
  ; @return (map)
  ;  {:sortable-items (vector)
  ;   :sortable-item-order (strings in vector)}
  [db [_ sortable-id]]
  {:sortable-items      (r get-sortable-items      db sortable-id)
   :sortable-item-order (r get-sortable-item-order db sortable-id)})

(a/reg-sub :sortable/get-view-props get-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-sortable-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ;
  ; @return (map)
  [db [_ sortable-id sortable-props]]
  (assoc-in db [:plugins :item-sorter/meta-items sortable-id] sortable-props))

(defn- init-sortable!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ;
  ; @return (map)
  [db [_ sortable-id sortable-props]]
  (r store-sortable-props! db sortable-id sortable-props))

(a/reg-event-db :sortable/init-sortable! init-sortable!)

(defn- destruct-sortable!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ;
  ; @return (map)
  [db [_ sortable-id]]
  (dissoc-in db [:plugins :item-sorter/meta-items sortable-id]))

(a/reg-event-db :sortable/destruct-sortable! destruct-sortable!)

(defn- update-sortable-item-order!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (?) event
  ;
  ; @return (map)
  [db [event-id sortable-id event]]
  (let [origin-item-dex     (r get-origin-item-dex     db sortable-id event)
        target-item-dex     (r get-target-item-dex     db sortable-id event)
        value-path          (r get-sortable-prop       db sortable-id :value-path)
        sortable-item-order (r get-sortable-item-order db sortable-id)
        updated-item-order  (vector/move-item sortable-item-order origin-item-dex target-item-dex)]
              ; Update sortable-items vector
       (-> db (db/apply-item! [event-id value-path vector/move-item origin-item-dex target-item-dex])
              ; Store updated-item-order
              (db/set-item! [event-id [:plugins :item-sorter/meta-items sortable-id :updated-item-order]
                                      (param updated-item-order)]))))

(defn add-sortable-item!
  ; @param (keyword) sortable-id
  ; @param (*) sortable-item
  ;
  ; @usage
  ;  (r sortable/add-sortable-item! db :my-sortable "My item")
  ;
  ; @return (map)
  [db [event-id sortable-id sortable-item]]
  (let [value-path          (r get-sortable-prop       db sortable-id :value-path)
        sortable-item-order (r get-sortable-item-order db sortable-id)
        sortable-item-count (r get-sortable-item-count db sortable-id)
        sortable-item-id    (sortable-item-dex->sortable-item-id sortable-id sortable-item-count)]
      (-> db (db/apply-item! [event-id value-path vector/conj-item sortable-item])
             ; XXX#6511
             ; Abban az esetben, ha a még nem történt elem-mozgatás, akkor az :updated-item-order
             ; még nem elérhető a Re-Frame adatbázisban, ezért szükséges a get-sortable-item-order
             ; függvény használatával olvasni az elemek sorrendjét.
             (db/set-item! [event-id [:plugins :item-sorter/meta-items sortable-id :updated-item-order]]
                           (vector/conj-item sortable-item-order sortable-item-id)))))

; @usage
;  [:sortable/add-sortable-item! :my-sortable "My item"]
(a/reg-event-db :sortable/add-sortable-item! add-sortable-item!)

(defn add-sortable-items!
  ; @param (keyword) sortable-id
  ; @param (vector) sortable-item
  ;
  ; @usage
  ;  (r sortable/add-sortable-items! db :my-sortable ["My item" "Your item"])
  ;
  ; @return (map)
  [db [_ sortable-id sortable-items]]
  ; TODO ...
  (return db))

(a/reg-event-db :sortable/add-sortable-items! add-sortable-items!)

(defn inject-sortable-item!
  ; @param (keyword) sortable-id
  ; @param (*) sortable-item
  ; @param (integer) target-dex
  ;
  ; @usage
  ;  (r sortable/inject-sortable-item! db :my-sortable "My item" 5)
  ;
  ; @return (map)
  [db [event-id sortable-id sortable-item target-dex]]
  (let [value-path          (r get-sortable-prop       db sortable-id :value-path)
        sortable-item-order (r get-sortable-item-order db sortable-id)
        sortable-item-count (r get-sortable-item-count db sortable-id)
        sortable-item-id    (sortable-item-dex->sortable-item-id sortable-id sortable-item-count)]
      (-> db (db/apply-item! [event-id value-path vector/inject-item sortable-item target-dex])
             ; XXX#6511
             (db/set-item! [event-id [:plugins :item-sorter/meta-items sortable-id :updated-item-order]]
                           (vector/inject-item sortable-item-order sortable-item-id target-dex)))))

; @usage
;  [:sortable/inject-sortable-item! :my-sortable "My item" 5]
(a/reg-event-db :sortable/inject-sortable-item! inject-sortable-item!)

(defn remove-sortable-item!
  ; @param (keyword) sortable-id
  ; @param (integer) sortable-item-dex
  ;
  ; @usage
  ;  (r sortable/remove-sortable-item! db :my-sortable 3)
  ;
  ; @return (map)
  [db [event-id sortable-id sortable-item-dex]]
  (let [value-path          (r get-sortable-prop        db sortable-id :value-path)
        sortable-item-order (r get-sortable-item-order  db sortable-id)]
      (-> db (db/apply-item! [event-id value-path vector/remove-nth-item sortable-item-dex])
             ; XXX#6511
             (db/set-item! [event-id [:plugins :item-sorter/meta-items sortable-id :updated-item-order]]
                           (vector/remove-nth-item sortable-item-order sortable-item-dex)))))

; @usage
;  [:sortable/remove-sortable-item! :my-sortable 3]
(a/reg-event-db :sortable/remove-sortable-item! remove-sortable-item!)

(defn remove-sortable-items!
  ; @param (keyword) sortable-id
  ; @param (integers in vector) sortable-item-dex-list
  ;
  ; @usage
  ;  (r sortable/remove-sortable-items! db :my-sortable [0 3])
  ;
  ; @return (map)
  [db [_ sortable-id sortable-item-dex-list]]
  ; TODO ...
  (return db))

; @usage
;  [:sortable/remove-sortable-items! :my-sortable [0 3]]
(a/reg-event-db :sortable/remove-sortable-items! remove-sortable-items!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::handle-drag-end!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (?) event
  (fn [{:keys [db]} [_ sortable-id event]]
      (if (r sortable-item-order-changed? db sortable-id event)
          ; If sortable-item-order changed ...
          {:db (r update-sortable-item-order! db sortable-id event)
           :dispatch-n [(r get-sortable-prop db sortable-id :on-order-change)
                        (r get-sortable-prop db sortable-id :on-drag-end)]}
          ; If sortable-item-order unchanged ...
          {:dispatch (r get-sortable-prop db sortable-id :on-drag-end)})))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn drag-overlay-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  ; A drag-overlay-element komponens eredeti felhasználási módja, hogy az aktív
  ; elem másolatát kirendereli, amíg az aktív elem a rendezés ideje alatt nem látható.
  ;
  ; A drag-overlay-element komponens megvalósított felhasználási módja pedig, hogy
  ; a rendezés ideje alatt egy tartalom nélküli [:div] elemet renderel a kurzor pozícióra,
  ; ami felett a kurzor "grabbing" állapotban látható.
  [:div.x-sortable--drag-overlay-element])

(defn- sortable-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) sortable-element-props
  ;  {:common-props (map)(opt)
  ;   :element (component)
  ;   :render-dex (integer)
  ;   :sortable-id (keyword)
  ;   :sortable-item (*)
  ;   :sortable-item-id (string)}
  [{:keys [common-props element render-dex sortable-id sortable-item sortable-item-id]}]
  (let [sortable-element-data       (sortable-item-id->sortable-element-data            sortable-item-id)
        sortable-element-attributes (sortable-element-data->sortable-element-attributes sortable-element-data)
        sortable-sensor-attributes  (sortable-element-data->sortable-sensor-attributes  sortable-element-data)]
       [:div.x-sortable--element sortable-element-attributes
         [element sortable-id render-dex sortable-item common-props]
         [:div.x-sortable--sensor sortable-sensor-attributes]]))

(defn sortable-element-component
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) view-props
  ; @param (string) sortable-item-id
  ; @param (integer) render-dex
  [sortable-id view-props sortable-item-id render-dex]
  (let [sortable-element-props (view-props->sortable-element-props sortable-id view-props sortable-item-id render-dex)]
       [:f> sortable-element sortable-element-props]))

(defn- sortable-elements
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) view-props
  ;  {:sortable-item-order (strings in vector)}
  [sortable-id {:keys [sortable-item-order] :as view-props}]
  (letfn [(f [sortable-elements render-dex sortable-item-id]
             ; BUG#9084
             ; Mozilla Firefox 89.0.2
             ; macOS Catalina 10.15.7
             ; Abban az esetben, ha a sortable-element-component komponensek
             ; egyedi react-kulcs átadással kerülnek kirenderelésre, akkor
             ; az onDragEnd esemény megtörténésekor a target-element komponensen
             ; történik egy szükségtelen CSS transform.
             ; Ha például a felsorolt elemek magassága 100px, akkor a target-element
             ; komponens -100px távolságról (Y tengelyen) transzformálódik a végleges
             ; helyére.
             ; A target-element komponens onDragEnd esemény előtti {:transform ...}
             ; tulajdonságának értéke: {:transform "translate3d(0px -100px, 0) ...", ...}
             ; A target-element komponens onDragEnd esemény utáni {:transform ...}
             ; tulajdonságának értéke: {:transform nil, ...}
             (conj sortable-elements ;^{:key sortable-item-id}
                   [sortable-element-component sortable-id view-props sortable-item-id render-dex]))]
         (reduce-indexed f [:div.x-sortable--elements (sortable-elements-attributes sortable-id view-props)]
                           (param sortable-item-order))))

(defn- sortable-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) view-props
  [sortable-id view-props]
  [dnd-context (view-props->dnd-context-props sortable-id view-props)
               [sortable-context (view-props->sortable-context-props sortable-id view-props)
                                 [sortable-elements sortable-id view-props]
                                 [drag-overlay [drag-overlay-element]]]])

(defn- sortable-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) view-props
  [sortable-id view-props]
  (if (view-props->render-sortable-items? view-props)
      [:f> sortable-structure sortable-id view-props]))

(defn sortable
  ; @param (keyword)(opt) sortable-id
  ; @param (map) sortable-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :common-props (map)(opt)
  ;   :element (component)
  ;   :on-drag-end (metamorphic-event)(opt)
  ;   :on-drag-start (metamorphic-event)(opt)
  ;    TODO ...
  ;   :on-order-change (metamorphic-event)(opt)
  ;   :style (map)(opt)
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [sortable {...}]
  ;
  ; @usage
  ;  [sortable :my-sortable {...}]
  ;
  ; @usage
  ;  (defn my-element [sortable-id item-dex item] [:div ...])
  ;  [sortable :my-sortable {:element    #'my-element
  ;                          :value-path [:my-items]}]
  ;
  ; @usage
  ;  (defn my-element [sortable-id item-dex item common-props] [:div ...])
  ;  [sortable :my-sortable {:common-props {...}
  ;                          :element      #'my-element
  ;                          :value-path   [:my-items]}]
  ([sortable-props]
   [sortable (a/id) sortable-props])

  ([sortable-id sortable-props]
   [components/stated sortable-id
                      {:render-f    #'sortable-f
                       :base-props  sortable-props
                       :destructor  [:sortable/destruct-sortable! sortable-id]
                       :initializer [:sortable/init-sortable!     sortable-id sortable-props]
                       :subscriber  [:sortable/get-view-props     sortable-id]}]))
