
(ns pretty-tables.cell.views
    (:require [fruits.random.api                  :as random]
              [pretty-elements.engine.api         :as pretty-elements.engine]
              [pretty-presets.engine.api          :as pretty-presets.engine]
              [pretty-tables.cell.attributes :as cell.attributes]
              [pretty-tables.cell.prototypes :as cell.prototypes]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cell
  ; @ignore
  ;
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ; {:content (metamorphic-content)(opt)
  ;  ...}
  [cell-id {:keys [content] :as cell-props}]
  [:div (cell.attributes/cell-attributes cell-id cell-props)
        [:div (cell.attributes/cell-body-attributes cell-id cell-props)
              (-> content)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  [cell-id cell-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    cell-id cell-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount cell-id cell-props))
                         :reagent-render         (fn [_ cell-props] [cell cell-id cell-props])}))

(defn view
  ; @description
  ; Table cell element.
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) cell-id
  ; @param (map) cell-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-tables/cell.png)
  ; [cell {:border-color  :muted
  ;        :border-radius {:all :m}
  ;        :content       "My cell"
  ;        :fill-color    :highlight
  ;        :outer-height  :xs}]
  ([cell-props]
   [view (random/generate-keyword) cell-props])

  ([cell-id cell-props]
   ; @note (tutorials#parameterizing)
   (fn [_ cell-props]
       (let [cell-props (pretty-presets.engine/apply-preset   cell-id cell-props)
             cell-props (cell.prototypes/cell-props-prototype cell-id cell-props)]
            [view-lifecycles cell-id cell-props]))))
