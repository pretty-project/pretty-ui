

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.data-table
    (:require [mid-fruits.candy                        :refer [param]]
              [x.app-components.api                    :as components]
              [x.app-core.api                          :as a :refer [r]]
              [x.app-elements.element-components.table :as element-components.table :refer [table]]
              [x.app-elements.engine.api               :as engine]))



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
           (even?   row-dex))
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
  (if width {:style {:width width}}))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-table-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) table-props
  ;
  ; @return (map)
  ;  {:alternating-rows? (boolean)
  ;   :horizontal-border (keyword)
  ;   :vertical-gap (px)}
  [table-props]
  (merge {:alternating-rows? true
          :horizontal-border :normal
          :vertical-gap      20}
         (param table-props)))

(defn- table-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) table-props
  ;
  ; @return (map)
  [table-props]
  (let [data-table-props (data-table-props-prototype table-props)]
       (element-components.table/table-props-prototype data-table-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ;  {:alternating-rows? (boolean)(opt)
  ;    Default: true
  ;   :class (keyword or keywords in vector)(opt)
  ;   :columns (maps in vector or integer)
  ;    [{:horizontal-align (keyword)(opt)
  ;       :center, :left, :right
  ;       Default: :left
  ;      :label (metamorphic-content)(opt)
  ;      :width (string)(opt)}]
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :horizontal-border (keyword)(opt)
  ;    :normal, :none
  ;    Default: :normal
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
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
  ;    Default: 20}
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
  ([table-props]
   [element (a/id) table-props])

  ([table-id table-props]
   (let [table-props (table-props-prototype table-props)]
        [table table-id table-props])))
