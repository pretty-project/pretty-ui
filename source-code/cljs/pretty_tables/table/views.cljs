
(ns pretty-tables.table.views
    (:require [fruits.hiccup.api                   :as hiccup]
              [fruits.random.api                   :as random]
              [pretty-elements.engine.api          :as pretty-elements.engine]
              [pretty-presets.engine.api           :as pretty-presets.engine]
              [pretty-tables.row.views        :as row.views]
              [pretty-tables.table.attributes :as table.attributes]
              [pretty-tables.table.prototypes :as table.prototypes]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- table-row
  ; @ignore
  ;
  ; @param (integer) row-dex
  ; @param (map) row-props
  [row-dex row-props]
  (let [row-props (table.prototypes/row-props-prototype row-dex row-props)]
       [row.views/view row-props]))

(defn- table
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:rows (maps in vector)(opt)
  ;  ...}
  [table-id {:keys [rows] :as table-props}]
  [:div (table.attributes/table-attributes table-id table-props)
        [:div (table.attributes/table-body-attributes table-id table-props)
              (letfn [(f0 [row-dex row-props] [table-row row-dex row-props])]
                     (hiccup/put-with-indexed [:<>] rows f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  [table-id table-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    table-id table-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount table-id table-props))
                         :reagent-render         (fn [_ table-props] [table table-id table-props])}))

(defn view
  ; @description
  ; Table element.
  ;
  ; @links Implemented elements
  ; [Row](pretty-ui/cljs/pretty-tables/api.html#row)
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-tables/table.png)
  ; [table {:border-crop   :auto
  ;         :border-color  :muted
  ;         :border-radius {:all :m}
  ;         :fill-color    :muted
  ;         :gap           :micro
  ;         :row-default   {:column-gap :micro :cell-default {:fill-color :highlight :height :xs}}
  ;         :rows          [{:cells [{:content "My cell #1.1"} {:content "My cell #1.2"}]}
  ;                         {:cells [{:content "My cell #2.1"} {:content "My cell #2.2"}]}
  ;                         {:cells [{:content "My cell #3.1"} {:content "My cell #3.2"}]}]}]
  ([table-props]
   [view (random/generate-keyword) table-props])

  ([table-id table-props]
   ; @note (tutorials#parameterizing)
   (fn [_ table-props]
       (let [table-props (pretty-presets.engine/apply-preset                            table-id table-props)
             table-props (table.prototypes/table-props-prototype                        table-id table-props)
             table-props (pretty-elements.engine/element-subitem-group<-subitem-default table-id table-props :rows :row-default)
             table-props (pretty-elements.engine/element-subitem-group<-disabled-state  table-id table-props :rows)
             table-props (pretty-elements.engine/leave-element-disabled-state           table-id table-props :rows)]
            [view-lifecycles table-id table-props]))))
