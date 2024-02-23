
(ns pretty-accessories.marker.views
    (:require [fruits.random.api :as random]
              [pretty-accessories.marker.attributes :as marker.attributes]
              [pretty-accessories.marker.prototypes :as marker.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- marker
  ; @ignore
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  [marker-id marker-props]
  [:div (marker.attributes/marker-attributes marker-id marker-props)
        [:div (marker.attributes/marker-inner-attributes marker-id marker-props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  [marker-id marker-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    marker-id marker-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount marker-id marker-props))
                         :reagent-render         (fn [_ marker-props] [marker marker-id marker-props])}))

(defn view
  ; @description
  ; Marker accessory for elements.
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) marker-id
  ; @param (map) marker-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-accessories/marker.png)
  ; [marker {:border-radius {:all :s}
  ;          :fill-color    :primary
  ;          :position      :tr}]
  ([marker-props]
   [view (random/generate-keyword) marker-props])

  ([marker-id marker-props]
   ; @note (tutorials#parameterizing)
   (fn [_ marker-props]
       (let [marker-props (pretty-presets.engine/apply-preset       marker-id marker-props)
             marker-props (marker.prototypes/marker-props-prototype marker-id marker-props)]
            [view-lifecycles marker-id marker-props]))))
