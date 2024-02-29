
(ns pretty-accessories.badge.views
    (:require [fruits.random.api :as random]
              [pretty-accessories.badge.attributes :as badge.attributes]
              [pretty-accessories.badge.prototypes :as badge.prototypes]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [pretty-accessories.icon.views :as icon.views]
              [pretty-accessories.label.views :as label.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- badge
  ; @ignore
  ;
  ; @param (keyword) badge-id
  ; @param (map) badge-props
  ; {:icon (map)(opt)
  ;  :label (map)(opt)
  ;  ...}
  [badge-id {:keys [icon label] :as badge-props}]
  [:div (badge.attributes/badge-attributes badge-id badge-props)
        [:div (badge.attributes/badge-inner-attributes badge-id badge-props)
              (cond label [label.views/view badge-id label]
                    icon  [icon.views/view  badge-id icon])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @description
  ; Badge accessory for elements.
  ;
  ; @links Implemented accessories
  ; [Icon](pretty-core/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-core/cljs/pretty-accessories/api.html#label)
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
  ; @param (keyword)(opt) badge-id
  ; @param (map) badge-props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-accessories/badge.png)
  ; [badge {:border-radius {:all :s}
  ;         :label         {:content "My badge"}
  ;         :fill-color    :highlight
  ;         :indent        {:all :xxs}
  ;         :position      :br}]
  ([badge-props]
   [view (random/generate-keyword) badge-props])

  ([badge-id badge-props]
   ; @note (tutorials#parameterizing)
   (fn [_ badge-props]
       (let [badge-props (pretty-presets.engine/apply-preset     badge-id badge-props)
             badge-props (badge.prototypes/badge-props-prototype badge-id badge-props)]
            [badge badge-id badge-props]))))
