
(ns pretty-accessories.marker.views
    (:require [fruits.random.api :as random]
              [pretty-accessories.marker.attributes :as marker.attributes]
              [pretty-accessories.marker.prototypes :as marker.prototypes]
              [pretty-presets.engine.api :as pretty-presets.engine]))

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

(defn view
  ; @description
  ; Marker accessory for elements.
  ;
  ; @links Implemented properties
  ; [Animation properties](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
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
            [marker marker-id marker-props]))))
