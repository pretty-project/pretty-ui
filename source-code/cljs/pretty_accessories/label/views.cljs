
(ns pretty-accessories.label.views
    (:require [fruits.random.api                 :as random]
              [pretty-accessories.label.attributes :as label.attributes]
              [pretty-accessories.label.prototypes :as label.prototypes]
              [pretty-presets.engine.api :as pretty-presets.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:content (metamorphic-content)(opt)
  ;  ...}
  [label-id {:keys [content end-adornments start-adornments] :as label-props}]
  [:div (label.attributes/label-attributes label-id label-props)
        [:div (label.attributes/label-inner-attributes label-id label-props)
              [:div (label.attributes/label-content-attributes label-id label-props) content]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @description
  ; Label accessory for elements.
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-accessories/label.png)
  ; [label {:border-color     :primary
  ;         :content          "My label"
  ;         :border-position  :bottom
  ;         :border-width     :xs
  ;         :gap              :auto
  ;         :horizontal-align :left
  ;         :outer-width      :xxl}]
  ;
  ([label-props]
   [view (random/generate-keyword) label-props])

  ([label-id label-props]
   ; @note (tutorials#parameterizing)
   (fn [_ label-props]
       (let [label-props (pretty-presets.engine/apply-preset     label-id label-props)
             label-props (label.prototypes/label-props-prototype label-id label-props)]
            [label label-id label-props]))))
