
(ns pretty-elements.column.views
    (:require [fruits.random.api                 :as random]
              [pretty-elements.column.attributes :as column.attributes]
              [pretty-elements.column.prototypes :as column.prototypes]
              [pretty-elements.engine.api        :as pretty-elements.engine]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [reagent.api                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- column
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ; {:content (metamorphic-content)(opt)}
  [column-id {:keys [content] :as column-props}]
  [:div (column.attributes/column-attributes column-id column-props)
        [:div (column.attributes/column-body-attributes column-id column-props)
              (-> content)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  [column-id column-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    column-id column-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount column-id column-props))
                       :reagent-render         (fn [_ column-props] [column column-id column-props])}))

(defn view
  ; @description
  ; Vertical flex container element for displaying content.
  ;
  ; @links Implemented properties
  ; [Background properties color](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) column-id
  ; @param (map) column-props
  ; Check out the implemented properties below.
  ;
  ; @usage (column.png)
  ; ...
  ([column-props]
   [view (random/generate-keyword) column-props])

  ([column-id column-props]
   ; @note (tutorials#parameterizing)
   (fn [_ column-props]
       (let [column-props (pretty-presets.engine/apply-preset       column-id column-props)
             column-props (column.prototypes/column-props-prototype column-id column-props)]
            [view-lifecycles column-id column-props]))))
