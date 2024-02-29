
(ns pretty-accessories.icon.views
    (:require [fruits.random.api               :as random]
              [pretty-accessories.icon.attributes :as icon.attributes]
              [pretty-accessories.icon.prototypes :as icon.prototypes]
              [pretty-presets.engine.api       :as pretty-presets.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ; {:icon-name (keyword)(opt)
  ;  ...}
  [icon-id {:keys [icon-name] :as icon-props}]
  [:div (icon.attributes/icon-attributes icon-id icon-props)
        [:div (icon.attributes/icon-inner-attributes icon-id icon-props)
              [:i (icon.attributes/icon-content-attributes icon-id icon-props) icon-name]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @description
  ; Icon accessory for elements.
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Icon properties](pretty-core/cljs/pretty-properties/api.html#icon-properties)
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
  ; @param (keyword)(opt) icon-id
  ; @param (map) icon-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-accessories/icon.png)
  ; [icon {:icon-name :settings}]
  ([icon-props]
   [view (random/generate-keyword) icon-props])

  ([icon-id icon-props]
   ; @note (tutorials#parameterizing)
   (fn [_ icon-props]
       (let [icon-props (pretty-presets.engine/apply-preset   icon-id icon-props)
             icon-props (icon.prototypes/icon-props-prototype icon-id icon-props)]
            [icon icon-id icon-props]))))
