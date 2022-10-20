
(ns plugins.dnd-kit.views
    (:require ["@dnd-kit/core"      :as dnd-kit.core]
              ["@dnd-kit/sortable"  :as dnd-kit.sortable]
              ["@dnd-kit/utilities" :as dnd-kit.utilities]
              [mid-fruits.random    :as random]
              [reagent.api          :as reagent :refer [ratom]]

              [plugins.dnd-kit.helpers    :as helpers]
              [plugins.dnd-kit.prototypes :as prototypes]
              [plugins.dnd-kit.state      :as state]

              ; TEMP
              [plugins.dnd-kit.utils :refer [to-clj-map]]))



;; -- Redirects ----------------------------------------------------------------
;; -----------------------------------------------------------------------------

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



;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(def DndContext      (reagent/adapt-react-class dnd-kit.core/DndContext))
(def DragOverlay     (reagent/adapt-react-class dnd-kit.core/DragOverlay))
(def SortableContext (reagent/adapt-react-class dnd-kit.sortable/SortableContext))



;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- sortable-debug
  [sortable-id _]
  [:div {}
        [:br] (str "GRABBED-ITEM:   " (get @state/GRABBED-ITEM   sortable-id))
        [:br] (str "SORTABLE-ITEMS: " (get @state/SORTABLE-ITEMS sortable-id))])

(defn- sortable-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ;  {:common-props (map)(opt)
  ;   :item-element (metamorphic-content)
  ;   :item-id-f (function)}
  ; @param (integer) item-dex
  ; @param (*) item
  [sortable-id {:keys [common-props item-id-f item-element]} item-dex item]
  (let [sortable (to-clj-map (useSortable (clj->js {:id (item-id-f item)})))
        {:keys [setNodeRef transform transition]} sortable]
    [:div {;:key  (str (item-id-f item) "--" item-dex)
           :key   (item-id-f item)
           :ref   (js->clj   setNodeRef)
           :style {:transition transition :transform (.toString (.-Transform CSS) (clj->js transform))}}
          (if common-props [item-element sortable-id common-props item-dex item sortable]
                           [item-element sortable-id              item-dex item sortable])]))

(defn- render-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  [sortable-id sortable-props]
  ; :f> needed cause useSortable hook
  (letfn [(f [item-list item-dex item]
             (conj item-list [:f> sortable-item sortable-id sortable-props item-dex item]))]
         (reduce-kv f [:<>] (get @state/SORTABLE-ITEMS sortable-id))))

(defn- dnd-context
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  [sortable-id sortable-props]
  (let [sensors (useSensors (useSensor PointerSensor)
                            (useSensor TouchSensor))]
       (if (-> @state/SORTABLE-ITEMS sortable-id empty? not)
           [DndContext {:sensors            sensors
                        :collisionDetection closestCenter
                        :onDragStart        #(helpers/drag-start-f sortable-id sortable-props %)
                        :onDragEnd          #(helpers/drag-end-f   sortable-id sortable-props %)}
                       [SortableContext {:items (get @state/SORTABLE-ITEMS sortable-id)
                                         :strategy rectSortingStrategy}
                                        [render-items sortable-id sortable-props]]
                       [DragOverlay {:z-index 1} (if (get @state/GRABBED-ITEM sortable-id)
                                                     [:div {:style {:cursor :grabbing :width "100%" :height "100%"}}])]])))

(defn- sortable-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  [sortable-id sortable-props]
  [:<> [:f> dnd-context sortable-id sortable-props]])
      ;[sortable-debug  sortable-id sortable-props]

(defn- sortable
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  [sortable-id sortable-props]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (helpers/sortable-did-mount-f    sortable-id sortable-props))
                       :component-will-unmount (fn [_ _] (helpers/sortable-will-unmount-f sortable-id sortable-props))
                       :component-did-update   (fn [%]   (helpers/sortable-did-update-f   sortable-id %))
                       :reagent-render         (fn [_ sortable-props] [sortable-body      sortable-id sortable-props])}))

(defn body
  ; @param (keyword)(opt) sortable-id
  ; @param (map) sortable-props
  ;  {:common-props (map)(opt)
  ;   :item-element (metamorphic-content)
  ;   :item-id-f (function)(opt)
  ;    Default: return
  ;   :items (vector)
  ;   :on-drag-end (metamorphic-event)(opt)}
  ;   :on-drag-start (metamorphic-event)(opt)}
  ;   :on-order-changed (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja az ... (?)}
  ;
  ; @usage
  ;  [sortable/body {...}]
  ;
  ; @usage
  ;  [sortable/body :my-sortable {...}]
  ;
  ; @usage
  ;  (defn my-item-element [sortable-id item-dex item dnd-kit-props])
  ;  [sortable/body {:item-element #'my-item-element
  ;                  :items        ["My item" "Your item"]}]
  ;
  ; @usage
  ;  (defn my-item-element [sortable-id common-props item-dex item dnd-kit-props])
  ;  [sortable/body {:common-props {...}
  ;                  :item-element #'my-item-element
  ;                  :items        ["My item" "Your item"]}]
  ([sortable-props]
   [body (random/generate-keyword) sortable-props])

  ([sortable-id sortable-props]
   (let [sortable-props (prototypes/sortable-props-prototype sortable-props)]
        [sortable sortable-id sortable-props])))
