
(ns pretty-tables.data-table.views
    (:require [fruits.hiccup.api                   :as hiccup]
              [fruits.random.api                   :as random]
              [pretty-elements.engine.api          :as pretty-elements.engine]
              [pretty-presets.engine.api           :as pretty-presets.engine]
              [pretty-tables.data-column.views     :as data-column.views]
              [pretty-tables.data-row.views        :as data-row.views]
              [pretty-tables.data-table.attributes :as data-table.attributes]
              [pretty-tables.data-table.prototypes :as data-table.prototypes]
              [reagent.api                         :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-table-column
  ; @ignore
  ;
  ; @param (integer) row-dex
  ; @param (map) row-props
  [row-dex row-props]
  (let [row-props (data-table.prototypes/row-props-prototype row-dex row-props)]
       [data-column.views/view row-props]))

(defn- data-table-row
  ; @ignore
  ;
  ; @param (integer) row-dex
  ; @param (map) row-props
  [row-dex row-props]
  (let [row-props (data-table.prototypes/row-props-prototype row-dex row-props)]
       [data-row.views/view row-props]))

(defn- data-table
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:columns (maps in vector)(opt)
  ;  :rows (maps in vector)(opt)}
  [table-id {:keys [columns rows] :as table-props}]
  [:div (data-table.attributes/table-attributes table-id table-props)
        [:div (data-table.attributes/table-body-attributes table-id table-props)
              (letfn [(f0 [column-dex column-props] [data-table-column column-dex column-props])
                      (f1 [row-dex    row-props]    [data-table-row    row-dex    row-props])]
                     (cond columns (hiccup/put-with-indexed [:<>] columns f0)
                           rows    (hiccup/put-with-indexed [:<>] rows    f1)))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  [table-id table-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    table-id table-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount table-id table-props))
                       :reagent-render         (fn [_ table-props] [data-table table-id table-props])}))

(defn view
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :column-default (map)(opt)
  ;  :columns (maps in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :row-default (map)(opt)
  ;  :rows (maps in vector)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [data-table {...}]
  ;
  ; @usage
  ; [data-table :my-data-table {...}]
  ([table-props]
   [view (random/generate-keyword) table-props])

  ([table-id table-props]
   ; @note (tutorials#parameterizing)
   (fn [_ table-props]
       (let [table-props (pretty-presets.engine/apply-preset                            table-id table-props)
             table-props (data-table.prototypes/table-props-prototype                   table-id table-props)
             table-props (pretty-elements.engine/element-subitem-group<-subitem-default table-id table-props :columns :column-default)
             table-props (pretty-elements.engine/element-subitem-group<-disabled-state  table-id table-props :rows    :row-default)
             table-props (pretty-elements.engine/element-subitem-group<-subitem-default table-id table-props :columns :column-default)
             table-props (pretty-elements.engine/element-subitem-group<-disabled-state  table-id table-props :rows    :row-default)
             table-props (pretty-elements.engine/dissoc-element-disabled-state          table-id table-props)]
            [view-lifecycles table-id table-props]))))
