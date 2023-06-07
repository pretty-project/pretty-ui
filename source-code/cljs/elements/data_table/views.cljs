
(ns elements.data-table.views
    (:require [elements.data-table.attributes :as data-table.attributes]
              [elements.data-table.prototypes :as data-table.prototypes]
              [elements.data-table.utils      :as data-table.utils]
              [elements.element.views         :as element.views]
              [hiccup.api                     :as hiccup]
              [metamorphic-content.api        :as metamorphic-content]
              [random.api                     :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-table-cell
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; @param (map) cell-props
  ; {:content (metamorphic-content)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [table-id table-props {:keys [content placeholder] :as cell-props}]
  (let [cell-props (data-table.utils/use-preset table-id table-props cell-props)
        cell-props (data-table.prototypes/cell-props-prototype cell-props)]
       [:div (data-table.attributes/table-cell-attributes table-id table-props cell-props)
             [metamorphic-content/compose content placeholder]]))

(defn- data-table-column
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {}
  ; @param (map) column-props
  ; {}
  [table-id table-props {:keys [cells] :as column-props}]
  (let [column-props (data-table.utils/use-preset table-id table-props column-props)
        column-props (data-table.prototypes/column-props-prototype column-props)]
       [:div (data-table.attributes/table-column-attributes table-id table-props column-props)
             (letfn [(f [cell-props] [data-table-cell table-id table-props cell-props])]
                    (hiccup/put-with [:<>] cells f))]))

(defn- data-table-row
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {}
  ; @param (map) row-props
  ; {}
  [table-id table-props {:keys [cells] :as row-props}]
  (let [row-props (data-table.utils/use-preset table-id table-props row-props)
        row-props (data-table.prototypes/row-props-prototype row-props)]
       [:div (data-table.attributes/table-row-attributes table-id table-props row-props)
             (letfn [(f [cell-props] [data-table-cell table-id table-props cell-props])]
                    (hiccup/put-with [:<>] cells f))]))

(defn- data-table
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {}
  [table-id {:keys [columns rows] :as table-props}]
  [:div (data-table.attributes/table-attributes table-id table-props)
        [element.views/element-label            table-id table-props]
        [:div (data-table.attributes/table-body-attributes table-id table-props)
              (cond columns (letfn [(f [column-props] [data-table-column table-id table-props column-props])] (hiccup/put-with [:<>] columns f))
                    rows    (letfn [(f [row-props]    [data-table-row    table-id table-props row-props])]    (hiccup/put-with [:<>] rows    f)))]])

(defn element
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :columns (maps in vector)(opt)
  ;   [{:cells (maps in vector)
  ;      [{:color (keyword or string)(opt)
  ;         :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;         Default: :inherit
  ;        :content (metamorphic-content)(opt)
  ;        :font-size (keyword)(opt)
  ;         :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;         Default: :s
  ;        :font-weight (keyword)(opt)
  ;         :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  ;         Default :normal
  ;        :horizontal-align (keyword)(opt)
  ;         :center, :left, :right
  ;         Default: :left
  ;        :indent (map)(opt)
  ;        :line-height (keyword)(opt)
  ;         :auto, :inherit, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;         Default: :text-block
  ;        :placeholder (metamorphic-content)(opt)
  ;        :preset (keyword)(opt)
  ;        :selectable? (boolean)(opt)
  ;         Default: true
  ;        :text-overflow (keyword)(opt)
  ;         :ellipsis, :hidden, :wrap
  ;         Default: :ellipsis
  ;        :text-transform (keyword)(opt)
  ;         :capitalize, :lowercase, :uppercase
  ;        :tooltip-content (metamorphic-content)(opt)
  ;        :tooltip-position (keyword)(opt)
  ;         :left, :right}]
  ;     :preset (keyword)(opt)
  ;     :template (string)(opt)
  ;      Default: "repeat(*cell-count*, 1fr)"
  ;     :width (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;      Default: :s}]
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :presets (map)(opt)
  ;   Cell, column and row presets.
  ;  :rows (maps in vector)(opt)
  ;   [{:cells (maps in vector)
  ;     :height (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;      Default: :s
  ;     :preset (keyword)(opt)
  ;     :template (string)(opt)
  ;      Default: "repeat(*cell-count*, 1fr)"}]
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [data-table {...}]
  ;
  ; @usage
  ; [data-table :my-data-table {...}]
  ;
  ; @usage
  ; [data-table {:columns [{:cells [{:content "Cell #1"}
  ;                                 {:content "Cell #2" :horizontal-align :right}]
  ;                         :line-height :m
  ;                         :template "1fr 40px"}]}]
  ;              :horizontal-gap :xs
  ;              :vertical-gap :xs
  ([table-props]
   [element (random/generate-keyword) table-props])

  ([table-id table-props]
   (let [] ; table-props (data-table.prototypes/table-props-prototype table-props)
        [data-table table-id table-props])))
