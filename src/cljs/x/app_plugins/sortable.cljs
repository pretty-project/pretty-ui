
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.08
; Description:
; Version: v0.9.2
; Compatibility: x4.2.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-plugins.sortable
    (:require ["@dnd-kit/core"      :as core]
              ["@dnd-kit/sortable"  :as sortable]
              ["@dnd-kit/utilities" :as utilities]
              [app-fruits.react     :as react]
              [app-fruits.reagent   :as reagent]
              [mid-fruits.candy     :as candy :refer [param]]
              [mid-fruits.json      :as json]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.map       :as map]
              [mid-fruits.random    :as random]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]))



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
  ;  {:class (string or vector)(opt)
  ;   :style (map)(opt)}
  ;
  ; @return (map)
  ;  {:class (string or vector)
  ;   :id (string)
  ;   :style (map)}
  [sortable-id {:keys [class style]}]
  (cond-> {:id (keyword/to-dom-value sortable-id)}
          (some? class) (assoc :class class)
          (some? style) (assoc :style style)))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- transform-data->transform-css
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) transform-data
  ;  {:scaleX (integer)
  ;   :scaleY (integer)
  ;   :x (integer)
  ;   :y (integer)}
  ;
  ; @return (string)
  [transform-data]
  (.toString (.-Transform CSS)
             (clj->js transform-data)))

(defn- element-id->context-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) element-id
  ; @param (map) sortable-props
  ;  {:common-props (map)(opt)
  ;   :element (component)
  ;   :partition-id (namespaced keyword)}
  ; @param (map) partition-state
  ;
  ; @return (map)
  ;  {:common-props (map)
  ;   :element (component)
  ;   :element-id (keyword)
  ;   :element-props (map)
  ;   :key (string)
  ;   :partition-id (namespaced keyword)}
  [element-id {:keys [common-props element partition-id]} partition-state]
  (let [data-item (db/partition->data-item partition-state element-id)]
       {:common-props  (param common-props)
        :element       (param element)
        :element-id    (param element-id)
        :element-props (param data-item)
        :key           (keyword/to-react-key element-id)
        :partition-id  (param partition-id)}))

(defn- element-id->sortable-element-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
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
  [element-id]
  (json/json->clj (use-sortable #js {:id (name element-id)})))

(defn- sortable-element-props->sortable-sensor-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) sortable-element-props
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
  (merge {:data-position (keyword/to-dom-value :right)
          :ref          #(setNodeRef %)
          :style {; Touch-sensor cursor
                  :cursor (if (:aria-pressed attributes) "grabbing" "grab")

                  ; BUG#7661
                  ;  Egy elem mozgatása közben az onTouchMove esemény hatással
                  ;  van az oldal scroll értékére, ezért a {:touch-action "none"}
                  ;  tulajdonság használata nélkül egyszerre változna a scroll
                  ;  értéke és az elem pozíciója.
                  :touch-action (param "none")}}

         (map/inherit attributes [:tabIndex])
         (param       listeners)))

(defn- sortable-element-props->sortable-element-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) sortable-element-props
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
  (merge {:style {:transform  (transform-data->transform-css transform)
                  :transition (param transition)
                  :z-index    (if (:aria-pressed attributes) 9999)}
          :data-drag-status   (if (:aria-pressed attributes) "active" "inactive")}
         (map/inherit attributes [:aria-describedby :aria-pressed :aria-roledescription :role])))

(defn- element-id->sortable-sensor-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [element-id]
  (let [sortable-element-props (element-id->sortable-element-props element-id)]
       (sortable-element-props->sortable-sensor-attributes sortable-element-props)))

(defn- element-id->sortable-element-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [element-id]
  (let [sortable-element-props (element-id->sortable-element-props element-id)]
       (sortable-element-props->sortable-element-attributes sortable-element-props)))

(defn- data-order->sortable-context-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) data-order
  ;
  ; @return (map)
  ;  {:items (vector)
  ;   :strategy (function)}
  [data-order]
  {:items    data-order
   :strategy rect-sorting-strategy})

(defn- sortable-props->dnd-context-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) sortable-props
  ; @param (vector) sortable-state
  ;
  ; @return (map)
  ;  {:collisionDetection (function)
  ;   :onDragEnd (function)
  ;   :sensors (function)}
  [sortable-props sortable-state]
  {:collisionDetection  (param closest-center)
   :onDragEnd          #(a/dispatch-sync [::handle-drag-end! % sortable-props sortable-state])
   :sensors             (make-sensors)})

(defn- event->origin-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (?) event
  ; @param (vector) sortable-state
  ;  [(vector) data-order
  ;   (function) set-items-f)]
  ;
  ; @return (integer)
  [event [data-order _]]
  (.indexOf data-order (.-id (.-active event))))

(defn- event->target-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (?) event
  ; @param (vector) sortable-state
  ;  [(vector) data-order
  ;   (function) set-items-f)]
  ;
  ; @return (integer)
  [event [data-order _]]
  (.indexOf data-order (.-id (.-over event))))

(defn- event->updated-data-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (?) event
  ; @param (vector) sortable-state
  ;  [(vector) data-order
  ;   (function) set-items-f)]
  ;
  ; @return (strings in json array)
  [event [data-order _ :as sortable-state]]
  (let [origin-dex (event->origin-dex event sortable-state)
        target-dex (event->target-dex event sortable-state)]
       (array-move data-order origin-dex target-dex)))

(defn- event->data-order-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (?) event
  ; @param (vector) sortable-state
  ;
  ; @return (boolean)
  [event sortable-state]
  (let [origin-dex (event->origin-dex event sortable-state)
        target-dex (event->target-dex event sortable-state)]
       (not= origin-dex target-dex)))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- handle-drag-end!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (?) event
  ; @param (map) sortable-props
  ;  {:on-drag-end (metamorphic-event)(opt)
  ;   :on-order-change (metamorphic-event)(opt)
  ;   :partition-id (namespaced keyword)}
  ; @param (vector) sortable-state
  ;  [(vector) data-order
  ;   (function) set-items-f)]
  [event {:keys [on-drag-end on-order-change partition-id]}
         [data-order set-items-f :as sortable-state]]
  (if (event->data-order-changed? event sortable-state)
      ; If data-order changed ...
      (let [updated-data-order (event->updated-data-order event sortable-state)]
           (set-items-f updated-data-order)
           (let [updated-data-order (mapv keyword (json/json->clj updated-data-order))]
                (a/dispatch [:x.app-db/update-data-order! partition-id updated-data-order])
                (a/dispatch-some on-drag-end)
                (a/dispatch-some on-order-change)))
      ; If data-order unchanged ...
      (a/dispatch-some on-drag-end)))

(a/reg-handled-fx ::handle-drag-end! handle-drag-end!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sortable-element
  ; @param (map) context-props
  ;  {:common-props (map)(opt)
  ;   :element (component)
  ;   :element-id (keyword)
  ;   :element-props (map)
  ;   :partition-id (namespaced keyword)}
  ;
  ; @return (hiccup)
  [{:keys [common-props element element-id element-props partition-id]}]
  [:div.x-sortable--element (element-id->sortable-element-attributes element-id)
    [element partition-id element-id element-props common-props]
    [:div.x-sortable--sensor (element-id->sortable-sensor-attributes element-id)]])

(defn- sortable-elements
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ; @param (map) partition-state
  ; @param (strings in json array) data-order
  ;
  ; @return (hiccup)
  [sortable-id sortable-props partition-state data-order]
  (reduce #(let [element-id    (keyword %2)
                 context-props (element-id->context-props element-id sortable-props partition-state)]
                (vector/conj-item %1 [:f> sortable-element context-props]))
           [:div.x-sortable--elements (sortable-elements-attributes sortable-id sortable-props)]
           (param data-order)))

(defn- sortable
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ; @param (map) partition-state
  ; @param (strings in json array) data-order-ref
  ;
  ; @return (component)
  [sortable-id sortable-props partition-state data-order-ref]
  (let [sortable-state (react/use-state data-order-ref)
        data-order     (first  sortable-state)
        set-items-f    (second sortable-state)
        _ (react/use-effect #(set-items-f data-order-ref) #js [data-order-ref])]
       [dnd-context (sortable-props->dnd-context-props sortable-props sortable-state)
         [sortable-context (data-order->sortable-context-props data-order)
           [sortable-elements sortable-id sortable-props partition-state data-order]]]))

(defn- af0120
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ; @param (map) partition-state
  ;  {:data-order (keywords in vector)
  ;   :partition-empty? (boolean)}
  ;
  ; @return (component)
  [sortable-id sortable-props {:keys [data-order partition-empty?] :as partition-state}]
  (if-not partition-empty?
          [:f> sortable sortable-id sortable-props partition-state (clj->js data-order)]))

(defn view
  ; @param (keyword)(opt) sortable-id
  ; @param (map) sortable-props
  ;  {:class (string or vector)(opt)
  ;   :common-props (map)(opt)
  ;   :element (component)
  ;   :on-drag-end (metamorphic-event)(opt)
  ;   :on-drag-start (metamorphic-event)(opt)
  ;    TODO ...
  ;   :on-order-change (metamorphic-event)(opt)
  ;   :partition-id (namespaced keyword)
  ;    Partition must be ordered!
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [plugins/sortable {...}]
  ;
  ; @usage
  ;  [plugins/sortable :my-sortable {...}]
  ;
  ; @usage
  ;  (def db {:my-partition/elements {:data-items {:first {:label "First"}}
  ;                                   :data-order [:first]}})
  ;  (defn my-element [partition-id data-item-id data-item] [:div ...])
  ;  [plugins/sortable :my-sortable {:element       #'my-element
  ;                                  :partition-id  :my-partition/elements}]
  ;
  ; @usage
  ;  (def db {:my-partition/elements {:data-items {:first {:label "First"}}
  ;                                   :data-order [:first]}})
  ;  (defn my-element [partition-id data-item-id data-item common-props] [:div ...])
  ;  [plugins/sortable :my-sortable {:common-props  {...}
  ;                                  :element       #'my-element
  ;                                  :partition-id  :my-partition/elements}]
  ;
  ; @return (component)
  ([sortable-props]
   [view nil sortable-props])

  ([sortable-id {:keys [partition-id] :as sortable-props}]
   (let [sortable-id (a/id sortable-id)]
        [components/subscriber sortable-id
          {:component    #'af0120
           :static-props sortable-props
           :subscriber   [:x.app-db/get-partition-state partition-id]}])))
