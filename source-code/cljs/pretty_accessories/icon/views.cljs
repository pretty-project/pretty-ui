
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
  ; @param (keyword) id
  ; @param (map) props
  ; {:icon-name (keyword)(opt)
  ;  ...}
  [id {:keys [icon-name] :as props}]
  [:div (icon.attributes/outer-attributes id props)
        [:div (icon.attributes/inner-attributes id props)
              [:i (icon.attributes/content-attributes id props) icon-name]]])

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
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-accessories/icon.png)
  ; [icon {:icon-name :settings}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset id props)
             props (icon.prototypes/props-prototype    id props)]
            [icon id props]))))
