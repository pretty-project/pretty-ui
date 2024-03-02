
(ns pretty-elements.column.views
    (:require [fruits.random.api                 :as random]
              [pretty-elements.column.attributes :as column.attributes]
              [pretty-elements.column.prototypes :as column.prototypes]
              [pretty-elements.engine.api        :as pretty-elements.engine]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- column
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [column-id {:keys [content] :as column-props}]
  [:div (column.attributes/column-attributes column-id column-props)
        [:div (column.attributes/column-inner-attributes column-id column-props)
              [:div (column.attributes/column-content-attributes column-id column-props) content]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  [column-id column-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    column-id column-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount column-id column-props))
                         :reagent-render         (fn [_ column-props] [column column-id column-props])}))

(defn view
  ; @description
  ; Vertical flex container element.
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
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
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) column-id
  ; @param (map) column-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/column.png)
  ; [column {:border-color     :primary
  ;          :border-radius    {:all :m}
  ;          :border-width     :xs
  ;          :content          [:<> [:div "My row #1"]
  ;                                 [:div "My row #2"]
  ;                                 [:div "My row #3"]]
  ;          :fill-color       :highlight
  ;          :gap              :xs
  ;          :horizontal-align :center
  ;          :vertical-align   :center
  ;          :outer-height     :s
  ;          :outer-width      :5xl}]
  ([column-props]
   [view (random/generate-keyword) column-props])

  ([column-id column-props]
   ; @note (tutorials#parameterizing)
   (fn [_ column-props]
       (let [column-props (pretty-presets.engine/apply-preset       column-id column-props)
             column-props (column.prototypes/column-props-prototype column-id column-props)]
            [view-lifecycles column-id column-props]))))
