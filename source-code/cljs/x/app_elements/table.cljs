
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.16
; Description:
; Version: v0.2.0
; Compatibility: x4.3.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.table
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.css            :as css]
              [mid-fruits.keyword        :as keyword]
              [mid-fruits.loop           :refer [reduce-indexed]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-CELL-HORIZONTAL-ALIGN :left)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-props->row-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
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

(defn- view-props->cell-horizontal-align
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:columns (maps in vector)(opt)
  ;    [{:horizontal-align (keyword)(opt)}]}
  ; @param (integer) row-dex
  ;
  ; @return (keyword)
  [view-props cell-dex]
  (if-let [horizontal-align (get-in view-props [:columns cell-dex :horizontal-align])]
          (keyword/to-dom-value horizontal-align)
          (keyword/to-dom-value DEFAULT-CELL-HORIZONTAL-ALIGN)))

(defn- view-props->cell-vertical-padding
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:vertical-gap (px)(opt)}
  ;
  ; @return (string)
  [{:keys [vertical-gap]}]
  (if (some? vertical-gap)
      (css/px (/ vertical-gap 2))))

(defn- view-props->row-cell-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:columns (maps in vector)(opt)
  ;    [{:horizontal-align (keyword)(opt)}]}
  ; @param (integer) row-dex
  ;
  ; @return (map)
  ;  {:data-horizontal-align (string)}
  [view-props cell-dex]
  (let [horizontal-align (view-props->cell-horizontal-align view-props cell-dex)
        vertical-padding (view-props->cell-vertical-padding view-props)]
       (cond-> {}
               (some? vertical-padding) (assoc-in [:style :padding] (css/vertical-padding vertical-padding))
               :horizontal-align        (assoc :data-horizontal-align horizontal-align))))

(defn- view-props->header-cell-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ; @param (map) column-props
  ;  {:width (string)(opt)}
  ; @param (integer) cell-dex
  ;
  ; @return (map)
  ;  {:data-horizontal-align (string)
  ;   :style (map)
  ;   {:width (string)}}
  [view-props {:keys [width]} cell-dex]
  (let [horizontal-align (view-props->cell-horizontal-align view-props cell-dex)
        vertical-padding (view-props->cell-vertical-padding view-props)]
       (cond-> {}
               (some? vertical-padding) (assoc-in [:style :padding] (css/vertical-padding vertical-padding))
               (some? width)            (assoc-in [:style :width]   width)
               :horizontal-align        (assoc :data-horizontal-align horizontal-align))))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- table-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) table-props
  ;  {:columns (maps in vector or integer)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :columns (maps in vector)
  ;   :font-size (keyword)
  ;   :layout (keyword)}
  [{:keys [columns] :as table-props}]
  (merge {:color     :highlight
          :font-size :s
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
  ; @param (map) view-props
  ; @param (*) cell-content
  ;
  ; @return (hiccup)
  [table-id view-props cell-content cell-index]
  [:td.x-table--body-cell
    (view-props->row-cell-attributes view-props cell-index)
    [components/content {:content cell-content}]])

(defn- table-row
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) view-props
  ;  {:columns (maps in vector)(opt)}
  ; @param (vector) row-data
  ; @param (integer) row-dex
  ;
  ; @return (hiccup)
  [table-id {:keys [columns] :as view-props} row-data row-dex]
  (let [row-data (vector/count! row-data (count columns))]
       (reduce-indexed #(vector/conj-item %1 [table-row-cell table-id view-props %2 %3])
                        [:tr.x-table--body-row (view-props->row-attributes view-props row-dex)]
                        (param row-data))))

(defn- table-rows
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) view-props
  ;  {:rows (vectors in vector)(opt)}
  ;
  ; @return (hiccup)
  [table-id {:keys [rows] :as view-props}]
  (reduce-indexed #(vector/conj-item %1 [table-row table-id view-props %2 %3])
                   [:tbody.x-table--body]
                   (param rows)))

(defn- table-header-cell-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) view-props
  ; @param (map) column-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [_ _ {:keys [label]}]
  (if (some? label)
      [:div.x-table--header-cell--label
        [components/content {:content label}]]))

(defn- table-header-cell
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) view-props
  ; @param (map) column-props
  ; @param (integer) cell-dex
  ;
  ; @return (hiccup)
  [table-id view-props column-props cell-dex]
  [:th.x-table--header-cell
    (view-props->header-cell-attributes view-props column-props cell-dex)
    [table-header-cell-label table-id view-props column-props]])

(defn- table-header-row
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) view-props
  ;  {:columns (maps in vector)}
  ;
  ; @return (hiccup)
  [table-id {:keys [columns] :as view-props}]
  (reduce-indexed #(vector/conj-item %1 [table-header-cell table-id view-props %2 %3])
                   [:tr.x-table--header-row]
                   (param columns)))

(defn- table-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [table-id view-props]
  [:thead.x-table--header
    [table-header-row table-id view-props]])

(defn- table-label
  [table-id {:keys [label]}]
  (if (some? label)
      [:div.x-table--label [components/content {:content label}]]))

(defn- table
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [table-id view-props]
  [:<> [table-label table-id view-props]
       [:table.x-table
         (engine/element-attributes table-id view-props)
         [table-header table-id view-props]
         [table-rows   table-id view-props]]])

(defn view
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ;  {:alternating-rows? (boolean)(opt)
  ;    Default: false
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :highlight, :none
  ;    Default: :highlight
  ;   :class (string or vector)(opt)
  ;   :columns (maps in vector or integer)
  ;    [{:horizontal-align (keyword)(opt)
  ;       :left, :center, :right
  ;       Default: DEFAULT-CELL-HORIZONTAL-ALIGN
  ;      :label (metamorphic-content)(opt)
  ;      :width (string)(opt)}]
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :horizontal-border (keyword)(opt)
  ;    :normal, :none
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
   [view nil table-props])

  ([table-id table-props]
   (let [table-id    (a/id   table-id)
         table-props (a/prot table-props table-props-prototype)]
        [table table-id table-props])))
