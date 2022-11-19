
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.dnd-kit.views
    (:require ["@dnd-kit/core"            :as dnd-kit.core]
              ["@dnd-kit/sortable"        :as dnd-kit.sortable]
              ["@dnd-kit/utilities"       :as dnd-kit.utilities]
              [random.api                 :as random]
              [plugins.dnd-kit.helpers    :as helpers]
              [plugins.dnd-kit.prototypes :as prototypes]
              [plugins.dnd-kit.state      :as state]
              [plugins.reagent.api        :as reagent :refer [component? ratom]]))



;; -----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @dnd-kit/core
(def closestCenter  dnd-kit.core/closestCenter)
(def closestCorners dnd-kit.core/closestCorners)
(def TouchSensor    dnd-kit.core/TouchSensor)
(def MouseSensor    dnd-kit.core/MouseSensor)
(def PointerSensor  dnd-kit.core/PointerSensor)
(def useSensor      dnd-kit.core/useSensor)
(def useSensors     dnd-kit.core/useSensors)

; @dnd-kit/sortable
(def arrayMove           dnd-kit.sortable/arrayMove)
(def rectSortingStrategy dnd-kit.sortable/rectSortingStrategy)
(def useSortable         dnd-kit.sortable/useSortable)

; @dnd-kit.utilities
(def CSS dnd-kit.utilities/CSS)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def DndContext      (reagent/adapt-react-class dnd-kit.core/DndContext))
(def DragOverlay     (reagent/adapt-react-class dnd-kit.core/DragOverlay))
(def SortableContext (reagent/adapt-react-class dnd-kit.sortable/SortableContext))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sortable-debug
  [sortable-id _]
  [:div {}
        [:br] (str "SORTABLE-ID:    " sortable-id)
        [:br] (str "SORTABLE-STATE: " (get @state/SORTABLE-STATE sortable-id))])

(defn- sortable-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ;  {:item-element (metamorphic-content)
  ;   :item-id-f (function)}
  ; @param (integer) item-dex
  ; @param (string) item-id
  [sortable-id {:keys [item-id-f item-element]} item-dex item-id]
  (let [dnd-kit-props (js->clj (useSortable (clj->js {:id item-id})) :keywordize-keys true)
        {:keys [attributes isDragging listeners setNodeRef transform transition]} dnd-kit-props
        handle-attributes (merge attributes listeners {:tab-index -1})
        item-attributes   {:key   item-id
                          ;:key   (str item-id "--" item-dex)
                           :ref   (js->clj   setNodeRef)
                           :style {:transition transition :transform (.toString (.-Transform CSS) (clj->js transform))}
                           :data-dragging isDragging}
        item (get-in @state/SORTABLE-STATE [sortable-id :sortable-items item-id])]
       (cond (component? item-element)
             (conj       item-element item-dex item {:item-attributes item-attributes :handle-attributes handle-attributes :dragging? isDragging})
             (fn?        item-element)
             [           item-element item-dex item {:item-attributes item-attributes :handle-attributes handle-attributes :dragging? isDragging}])))

(defn- render-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  [sortable-id sortable-props]
  ; :f> needed cause useSortable hook
  (letfn [(f [item-list item-dex item-id]
             (conj item-list [:f> sortable-item sortable-id sortable-props item-dex item-id]))]
         (reduce-kv f [:<>] (get-in @state/SORTABLE-STATE [sortable-id :item-order]))))

(defn- dnd-context
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  [sortable-id sortable-props]
  (let [sensors (useSensors (useSensor PointerSensor)
                            (useSensor TouchSensor))]
       [DndContext {:sensors            sensors
                    :collisionDetection closestCenter
                    :onDragStart        #(helpers/drag-start-f sortable-id sortable-props %)
                    :onDragEnd          #(helpers/drag-end-f   sortable-id sortable-props %)}
                   [SortableContext {:items (get-in @state/SORTABLE-STATE [sortable-id :item-order])
                                     :strategy rectSortingStrategy}
                                    [render-items sortable-id sortable-props]]
                   [DragOverlay {:z-index 1} (if (get-in @state/SORTABLE-STATE [sortable-id :grabbed-item])
                                                 [:div {:style {:cursor :grabbing :width "100%" :height "100%"}}])]]))

(defn- sortable-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  [sortable-id sortable-props]
  ; BUG#0047 (plugins.dnd-kit.helpers)
  (if (-> @state/SORTABLE-STATE sortable-id :item-order empty? not)
      [:<> [:f> dnd-context sortable-id sortable-props]]))
          ;[sortable-debug  sortable-id sortable-props]

(defn- sortable
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  [sortable-id sortable-props]
  ; BUG#0047 (plugins.dnd-kit.helpers)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (helpers/sortable-did-mount-f    sortable-id sortable-props))
                       :component-will-unmount (fn [_ _] (helpers/sortable-will-unmount-f sortable-id sortable-props))
                       :component-did-update   (fn [%]   (helpers/sortable-did-update-f   sortable-id %))
                       :reagent-render         (fn [_ sortable-props] [sortable-body sortable-id sortable-props])}))

(defn body
  ; @param (keyword)(opt) sortable-id
  ; @param (map) sortable-props
  ;  {:item-element (component or symbol)
  ;   :item-id-f (function)(opt)
  ;    Default: return
  ;   :items (vector)
  ;   :on-drag-end (function)(opt)
  ;   :on-drag-start (function)(opt)
  ;   :on-order-changed (function)(opt)}
  ;
  ; @usage
  ;  [body {...}]
  ;
  ; @usage
  ;  [body :my-sortable {...}]
  ;
  ; @usage
  ;  (defn my-item-element [item-dex item drag-props]
  ;                        (let [{:keys [dragging? handle-attributes item-attributes]} drag-props]))
  ;  [body :my-sortable {:item-element #'my-item-element
  ;                      :items        ["My item" "Your item"]}]
  ;
  ; @usage
  ;  (defn my-item-element [my-param item-dex item drag-props]
  ;                        (let [{:keys [dragging? handle-attributes item-attributes]} drag-props]))
  ;  [body :my-sortable {:item-element [my-item-element "My value"]
  ;                      :items        ["My item" "Your item"]}]
  ;
  ; @usage
  ;  (defn on-drag-start-f    [sortable-id sortable-props])
  ;  (defn on-drag-end-f      [sortable-id sortable-props])
  ;  (defn on-order-changed-f [sortable-id sortable-props items])
  ;  [body :my-sortable {:on-drag-start    on-drag-start-f
  ;                      :on-drag-end      on-drag-end-f
  ;                      :on-order-changed on-order-changed-f}]
  ([sortable-props]
   [body (random/generate-keyword) sortable-props])

  ([sortable-id sortable-props]
   (let [sortable-props (prototypes/sortable-props-prototype sortable-props)]
        [sortable sortable-id sortable-props])))
