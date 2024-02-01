
(ns pretty-tables.data-table.views
    (:require [fruits.hiccup.api                     :as hiccup]
              [fruits.random.api                     :as random]
              [metamorphic-content.api               :as metamorphic-content]
              [pretty-tables.data-table.attributes :as data-table.attributes]
              [pretty-tables.data-table.prototypes :as data-table.prototypes]
              [pretty-elements.engine.api                     :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                           :as reagent]))

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
  (let [cell-props (pretty-presets.engine/apply-preset         cell-props)
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
  (let [column-props (pretty-presets.engine/apply-preset           column-props)
        column-props (data-table.prototypes/column-props-prototype column-props)]
       [:div (data-table.attributes/table-column-attributes table-id table-props column-props)
             (letfn [(f0 [cell-props] [data-table-cell table-id table-props cell-props])]
                    (hiccup/put-with [:<>] cells f0))]))

(defn- data-table-row
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {}
  ; @param (map) row-props
  ; {}
  [table-id table-props {:keys [cells] :as row-props}]
  (let [row-props (pretty-presets.engine/apply-preset        row-props)
        row-props (data-table.prototypes/row-props-prototype row-props)]
       [:div (data-table.attributes/table-row-attributes table-id table-props row-props)
             (letfn [(f0 [cell-props] [data-table-cell table-id table-props cell-props])]
                    (hiccup/put-with [:<>] cells f0))]))

(defn- data-table
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {}
  [table-id {:keys [columns rows] :as table-props}]
  [:div (data-table.attributes/table-attributes table-id table-props)
        [:div (data-table.attributes/table-body-attributes table-id table-props)
              (cond columns (letfn [(f0 [column-props] [data-table-column table-id table-props column-props])] (hiccup/put-with [:<>] columns f0))
                    rows    (letfn [(f0 [row-props]    [data-table-row    table-id table-props row-props])]    (hiccup/put-with [:<>] rows    f0)))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  [table-id table-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    table-id table-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount table-id table-props))
                       :reagent-render         (fn [_ table-props] [data-table table-id table-props])}))

(defn view
  ; @note
  ; Cells take the same properties as the 'text' element.
  ;
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :columns (maps in vector)(opt)
  ;   [{:cells (maps in vector)
  ;     :preset (keyword)(opt)
  ;     :template (string)(opt)
  ;      Default: "repeat(*cell-count*, 1fr)"
  ;     :width (keyword, px or string)(opt)
  ;      Default: :s}]
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :rows (maps in vector)(opt)
  ;   [{:cells (maps in vector)
  ;     :height (keyword, px or string)(opt)
  ;      Default: :s
  ;     :preset (keyword)(opt)
  ;     :template (string)(opt)
  ;      Default: "repeat(*cell-count*, 1fr)"}]
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
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
   [view (random/generate-keyword) table-props])

  ([table-id table-props]
   ; @note (tutorials#parametering)
   (fn [_ table-props]
       (let [table-props (pretty-presets.engine/apply-preset          table-id table-props)
             table-props (data-table.prototypes/table-props-prototype table-id table-props)]
            [view-lifecycles table-id table-props]))))
