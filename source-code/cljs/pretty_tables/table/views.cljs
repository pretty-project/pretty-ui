
(ns pretty-tables.table.views
    (:require [fruits.hiccup.api              :as hiccup]
              [fruits.random.api              :as random]
              [pretty-elements.engine.api     :as pretty-elements.engine]
              [pretty-presets.engine.api      :as pretty-presets.engine]
              [pretty-tables.row.views        :as row.views]
              [pretty-tables.table.attributes :as table.attributes]
              [pretty-tables.table.prototypes :as table.prototypes]
              [reagent.core                   :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- table
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:rows (maps in vector)(opt)
  ;  ...}
  [id {:keys [rows] :as props}]
  [:div (table.attributes/outer-attributes id props)
        [:div (table.attributes/inner-attributes id props)
              (letfn [(f0 [row] [row.views/view row])]
                     (hiccup/put-with [:<>] rows f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount id props))
                         :reagent-render         (fn [_ props] [table id props])}))

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
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-tables/table.png)
  ; [table {:border-crop   :auto
  ;         :border-color  :muted
  ;         :border-radius {:all :m}
  ;         :fill-color    :muted
  ;         :gap           :micro
  ;         :row-default   {:column-gap :micro :cell-default {:fill-color :highlight :outer-height :xs}}
  ;         :rows          [{:cells [{:content "My cell #1.1"} {:content "My cell #1.2"}]}
  ;                         {:cells [{:content "My cell #2.1"} {:content "My cell #2.2"}]}
  ;                         {:cells [{:content "My cell #3.1"} {:content "My cell #3.2"}]}]}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset id props)
             props (table.prototypes/props-prototype   id props)]
            [view-lifecycles id props]))))
