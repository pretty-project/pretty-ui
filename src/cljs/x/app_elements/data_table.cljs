
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.16
; Description:
; Version: v0.2.0
; Compatibility: x4.3.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.data-table
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.loop           :refer [reduce-indexed]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]
              [x.app-elements.table      :as table :refer [table]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (px)
(def DEFAULT-VERTICAL-GAP 20)



;; -- Converters --------------------------------------------------------------
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
           (even? row-dex))
      {:data-even true}
      {:data-odd  true}))

(defn- column-props->header-cell-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) column-props
  ;  {:width (string)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)
  ;   {:width (string)}}
  [{:keys [width]}]
  (if (some? width)
      {:style {:width width}}))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-table-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) table-props
  ;
  ; @return (map)
  [table-props]
  (merge {:alternating-rows? true
          :horizontal-border :normal
          :vertical-gap      DEFAULT-VERTICAL-GAP}
         (param table-props)))

(defn- table-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) table-props
  ;
  ; @return (map)
  [table-props]
  (let [data-table-props (data-table-props-prototype table-props)]
       (table/table-props-prototype data-table-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) table-id
  ;
  ; @return (map)
  [db [_ table-id]]
  (merge (r engine/get-element-view-props db table-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ;  {:alternating-rows? (boolean)(opt)
  ;    Default: true
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :highlight, :none
  ;    Default: :highlight
  ;   :class (string or vector)(opt)
  ;   :columns (maps in vector or integer)
  ;    [{:horizontal-align (keyword)(opt)
  ;       :left, :center, :right
  ;       Default: x.app-elements.table/DEFAULT-CELL-HORIZONTAL-ALIGN
  ;      :label (metamorphic-content)(opt)
  ;      :width (string)(opt)}]
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :helper (metamorphic-content)(opt)
  ;   :horizontal-border (keyword)(opt)
  ;    :normal, :none
  ;    Default: :normal
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
  ;   :vertical-gap (px)(opt)
  ;    Default: DEFAULT-VERTICAL-GAP}
  ;
  ; @usage
  ;  [elements/data-table {...}]
  ;
  ; @usage
  ;  [elements/data-table :my-data-table {...}]
  ;
  ; @usage
  ;  [elemets/data-table {:columns [{:label "Name"}
  ;                                 {:label "Value #1"}
  ;                                 {:label "Value #2"}]
  ;                       :rows [["My data #1" 30 50]
  ;                              ["My data #2" 10 90]
  ;                              ["My data #3" 20 75]]
  ;
  ; @usage
  ;  [elemets/data-table {:columns [{:width "200px"}
  ;                                 {:width "200px"}
  ;                                 {:width "200px"}]
  ;                       :rows [["My data #1" 30 50]
  ;                              ["My data #2" 10 90]
  ;                              ["My data #3" 20 75]]
  ;
  ; @return (component)
  ([table-props]
   [view nil table-props])

  ([table-id table-props]
   (let [table-id    (a/id   table-id)
         table-props (a/prot table-props table-props-prototype)]
        [engine/container table-id
          {:base-props table-props
           :component  table
           :subscriber [::get-view-props table-id]}])))
