
(ns pretty-tables.row.views
    (:require [fruits.hiccup.api            :as hiccup]
              [fruits.random.api            :as random]
              [pretty-elements.engine.api   :as pretty-elements.engine]
              [pretty-presets.engine.api    :as pretty-presets.engine]
              [pretty-tables.cell.views     :as cell.views]
              [pretty-tables.row.attributes :as row.attributes]
              [pretty-tables.row.prototypes :as row.prototypes]
              [reagent.core                 :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- row
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:cells (maps in vector)
  ;  ...}
  [id {:keys [cells] :as props}]
  [:div (row.attributes/outer-attributes id props)
        [:div (row.attributes/inner-attributes id props)
              (letfn [(f0 [cell] [cell.views/view cell])]
                     (hiccup/put-with [:<>] cells f0))]])

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
                         :reagent-render         (fn [_ props] [row id props])}))

(defn view
  ; @description
  ; Table row element.
  ;
  ; @links Implemented elements
  ; [Cell](pretty-ui/cljs/pretty-tables/api.html#cell)
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Grid properties](pretty-core/cljs/pretty-properties/api.html#grid-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-tables/row.png)
  ; [row {:border-color  :muted
  ;       :border-crop   :auto
  ;       :border-radius {:all :m}
  ;       :column-gap    :micro
  ;       :fill-color    :muted
  ;       :cell-default  {:outer-height :xs :fill-color :highlight}
  ;       :cells         [{:content "My cell #1"} {:content "My cell #2"}]}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset id props)
             props (row.prototypes/props-prototype     id props)]
            [view-lifecycles id props]))))
