
(ns components.data-table.views
    (:require [components.data-table.helpers    :as data-table.helpers]
              [components.data-table.prototypes :as data-table.prototypes]
              [elements.api                     :as elements]
              [random.api                       :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-table-row-cell
  ; @param (map) cell-props
  [cell-props]
  [:td.c-data-table--td [elements/label cell-props]])

(defn- data-table-row
  ; @param (maps in vector) row-cells
  [row-cells]
  (letfn [(f [cell-list cell-props] (conj cell-list [data-table-row-cell cell-props]))]
         (reduce f [:tr.c-data-table--tr] row-cells)))

(defn- data-table-rows
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:rows (label-props maps in vectors in vector)(opt)}
  [table-id {:keys [rows] :as table-props}]
  (letfn [(f [row-list row-cells] (conj row-list [data-table-row row-cells]))]
         (if-not (empty? rows)
                 [:table.c-data-table--table (reduce f [:tbody.c-data-table--tbody] rows)])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-table-column-cell
  ; @param (map) cell-props
  [cell-props]
  [:td.c-data-table--td [elements/label cell-props]])

(defn- data-table-column
  ; @param (maps in vector) column-cells
  [column-cells]
  (letfn [(f [cell-list cell-props] (conj cell-list [data-table-column-cell cell-props]))]
         (reduce f [:tr.c-data-table--tr] column-cells)))

(defn- data-table-columns
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:columns (label-props maps in vectors in vector)(opt)}
  [table-id {:keys [columns] :as table-props}]
  (letfn [(f [column-list column-cells] (conj column-list [data-table-column column-cells]))]
         (if-not (empty? columns)
                 [:table.c-data-table--table (reduce f [:tbody.c-data-table--tbody] columns)])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-table-label
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:label (metamorphic-content)}
  [_ {:keys [label]}]
  (if label [:div.c-data-table--label [elements/label {:content             label
                                                       :horizontal-position :left
                                                       :selectable?         false}]]))

(defn- data-table
  ; @param (keyword) table-id
  ; @param (map) table-props
  [table-id table-props]
  [:div.c-data-table (data-table.helpers/table-attributes table-id table-props)
                     [data-table-label                    table-id table-props]
                     [data-table-rows                     table-id table-props]
                     [data-table-columns                  table-id table-props]])

(defn component
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :columns (label-props maps in vectors in vector)(opt)
  ;  :disabled? (boolean)(opt)}
  ;   Default: false
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;   Default: :s
  ;  :indent (map)(opt)
  ;  :label (metamorphic-content)
  ;  :outdent (map)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;  :rows (label-props maps in vectors in vector)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [data-table {...}]
  ;
  ; @usage
  ; [data-table :my-data-table {...}]
  ;
  ; @usage
  ; [data-table {:rows [[{:content "Row #1"   :font-weight :bold}
  ;                      {:content "Value #1" :color :muted}
  ;                      {:content "Value #2" :color :muted}]
  ;                     [{...} {...}]]}]
  ([table-props]
   [component (random/generate-keyword) table-props])

  ([table-id table-props]
   (let [table-props (data-table.prototypes/table-props-prototype table-props)]
        [data-table table-id table-props])))
