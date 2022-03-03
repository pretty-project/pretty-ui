
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.components.table
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.css            :as css]
              [mid-fruits.loop           :refer [reduce-indexed]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- table-props->row-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) table-props
  ;  {:alternating-rows? (boolean)(opt)}
  ; @param (integer) row-dex
  ;
  ; @return (map)
  ;  {:data-even (boolean)
  ;   :data-odd (boolean)}
  [{:keys [alternating-rows?]} row-dex]
  (if (and (boolean alternating-rows?)
           (even? (inc row-dex)))
      {:data-even true}
      {:data-odd  true}))

(defn- table-props->cell-horizontal-align
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) table-props
  ;  {:columns (maps in vector)(opt)
  ;    [{:horizontal-align (keyword)(opt)}]}
  ; @param (integer) row-dex
  ;
  ; @return (keyword)
  [table-props cell-dex]
  (if-let [horizontal-align (get-in table-props [:columns cell-dex :horizontal-align])]
          (return horizontal-align)
          (return :left)))

(defn- table-props->cell-vertical-padding
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) table-props
  ;  {:vertical-gap (px)(opt)}
  ;
  ; @return (string)
  [{:keys [vertical-gap]}]
  (if vertical-gap (css/px (/ vertical-gap 2))))

(defn- table-props->row-cell-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) table-props
  ;  {:columns (maps in vector)(opt)
  ;    [{:horizontal-align (keyword)(opt)}]}
  ; @param (integer) row-dex
  ;
  ; @return (map)
  ;  {:data-horizontal-align (string)}
  [table-props cell-dex]
  (let [horizontal-align (table-props->cell-horizontal-align table-props cell-dex)
        vertical-padding (table-props->cell-vertical-padding table-props)]
       (cond-> {} (some? vertical-padding) (assoc-in [:style :padding]        (css/vertical-padding vertical-padding))
                  :horizontal-align        (assoc-in [:data-horizontal-align] (param                horizontal-align)))))

(defn- table-props->header-cell-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) table-props
  ; @param (map) column-props
  ;  {:width (string)(opt)}
  ; @param (integer) cell-dex
  ;
  ; @return (map)
  ;  {:data-horizontal-align (string)
  ;   :style (map)
  ;   {:width (string)}}
  [table-props {:keys [width]} cell-dex]
  (let [horizontal-align (table-props->cell-horizontal-align table-props cell-dex)
        vertical-padding (table-props->cell-vertical-padding table-props)]
       (cond-> {} (some? vertical-padding) (assoc-in [:style :padding]        (css/vertical-padding vertical-padding))
                  (some? width)            (assoc-in [:style :width]          (param                width))
                  :horizontal-align        (assoc-in [:data-horizontal-align] (param                horizontal-align)))))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- table-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) table-props
  ;  {:columns (maps in vector or integer)}
  ;
  ; @return (map)
  ;  {:columns (maps in vector)
  ;   :font-size (keyword)
  ;   :layout (keyword)}
  [{:keys [columns] :as table-props}]
  (merge {:font-size :s
          :layout    :row}
         (param table-props)
         (if (integer? columns)
             {:columns (vector/repeat-item {} columns)})))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- table-row-cell
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; @param (*) cell-content
  ;
  ; @return (hiccup)
  [table-id table-props cell-content cell-index]
  [:td.x-table--body-cell (table-props->row-cell-attributes table-props cell-index)
                          [components/content cell-content]])

(defn- table-row
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;  {:columns (maps in vector)(opt)}
  ; @param (vector) row-data
  ; @param (integer) row-dex
  ;
  ; @return (hiccup)
  [table-id {:keys [columns] :as table-props} row-data row-dex]
  (let [row-data (vector/count! row-data (count columns))]
       (reduce-indexed #(conj %1 [table-row-cell table-id table-props %3 %2])
                        [:tr.x-table--body-row (table-props->row-attributes table-props row-dex)]
                        (param row-data))))

(defn- table-rows
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;  {:rows (vectors in vector)(opt)}
  ;
  ; @return (hiccup)
  [table-id {:keys [rows] :as table-props}]
  (reduce-indexed #(conj %1 [table-row table-id table-props %3 %2])
                   [:tbody.x-table--body]
                   (param rows)))

(defn- table-header-cell-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; @param (map) column-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [_ _ {:keys [label]}]
  (if label [:div.x-table--header-cell--label [components/content label]]))

(defn- table-header-cell
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; @param (map) column-props
  ; @param (integer) cell-dex
  ;
  ; @return (hiccup)
  [table-id table-props column-props cell-dex]
  [:th.x-table--header-cell (table-props->header-cell-attributes table-props column-props cell-dex)
                            [table-header-cell-label    table-id table-props column-props]])

(defn- table-header-row
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;  {:columns (maps in vector)}
  ;
  ; @return (hiccup)
  [table-id {:keys [columns] :as table-props}]
  (reduce-indexed #(conj %1 [table-header-cell table-id table-props %3 %2])
                   [:tr.x-table--header-row]
                   (param columns)))

(defn- table-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (hiccup)
  [table-id table-props]
  [:thead.x-table--header [table-header-row table-id table-props]])

(defn- table-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (hiccup)
  [table-id {:keys [label]}]
  (if label [:div.x-table--label [components/content label]]))

(defn- table
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (hiccup)
  [table-id table-props]
  [:<> [table-label table-id table-props]
       [:table.x-table (engine/table-attributes table-id table-props)
                       [table-header            table-id table-props]
                       [table-rows              table-id table-props]]])

(defn element
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ;  {:alternating-rows? (boolean)(opt)
  ;    Default: false
  ;   :class (keyword or keywords in vector)(opt)
  ;   :columns (maps in vector or integer)
  ;    [{:horizontal-align (keyword)(opt)
  ;       :left, :center, :right
  ;       Default: :left
  ;      :label (metamorphic-content)(opt)
  ;      :width (string)(opt)}]
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :horizontal-border (keyword)(opt)
  ;    :normal, :none
  ;    Default: :none
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :rows (metamorphic-contents in vectors in vector)(opt)
  ;    [(vector) row
  ;     [(metamorphic-content) cell-content]]
  ;   :selectable? (boolean)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :vertical-border (keyword)(opt)
  ;    :normal, :none
  ;    Default: :none
  ;   :vertical-gap (px)(opt)}
  ;
  ; @usage
  ;  [elements/table {...}]
  ;
  ; @usage
  ;  [elements/table :my-table {...}]
  ;
  ; @usage
  ;  [elements/table {:columns [{:label "Name"}
  ;                             {:label "Value #1"}
  ;                             {:label "Value #2"}]
  ;                   :rows [["My data #1" 30 50]
  ;                          ["My data #2" 10 90]
  ;                          ["My data #3" 20 75]]
  ;
  ; @usage
  ;  [elements/table {:columns [{:width "200px"}
  ;                             {:width "200px"}
  ;                             {:width "200px"}]
  ;                   :rows [["My data #1" 30 50]
  ;                          ["My data #2" 10 90]
  ;                          ["My data #3" 20 75]]
  ;
  ; @usage
  ;  [elements/table {:columns [{:width "120px"}
  ;                             {}
  ;                             {}]
  ;                   :horizontal-gap "12px"
  ;                   :rows [[[elements/label {:content "My values: "
  ;                                            :horizontal-align :right}
  ;                           "1.40"
  ;                           "4.20"]]]}]
  ;
  ; @usage
  ;  [elements/table {:columns 2
  ;                   :rows [["1.40" "4.20"]
  ;                          ["0.40" "3.20"]]]}]
  ;
  ; @return (component)
  ([table-props]
   [element (a/id) table-props])

  ([table-id table-props]
   (let [table-props (table-props-prototype table-props)]
        [table table-id table-props])))
