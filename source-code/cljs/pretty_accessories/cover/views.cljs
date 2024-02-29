
(ns pretty-accessories.cover.views
    (:require [fruits.random.api :as random]
              [pretty-accessories.cover.attributes :as cover.attributes]
              [pretty-accessories.cover.prototypes :as cover.prototypes]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [pretty-accessories.icon.views :as icon.views]
              [pretty-accessories.label.views :as label.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cover
  ; @ignore
  ;
  ; @param (keyword) cover-id
  ; @param (map) cover-props
  ; {:icon (map)(opt)
  ;  :label (map)(opt)
  ;  ...}
  [cover-id {:keys [icon label] :as cover-props}]
  [:div (cover.attributes/cover-attributes cover-id cover-props)
        [:div (cover.attributes/cover-inner-attributes cover-id cover-props)
              (cond label [label.views/view cover-id label]
                    icon  [icon.views/view  cover-id icon])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @description
  ; Cover accessory for elements.
  ;
  ; @links Implemented accessories
  ; [Icon](pretty-core/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-core/cljs/pretty-accessories/api.html#label)
  ;
  ; @links Implemented properties
  ; [Animation properties](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Overlay properties](pretty-core/cljs/pretty-properties/api.html#overlay-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) cover-id
  ; @param (map) cover-props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-accessories/cover.png)
  ; [cover {:fill-color :highlight
  ;         :label      {:content "My cover"}
  ;         :opacity    :medium}]
  ([cover-props]
   [view (random/generate-keyword) cover-props])

  ([cover-id cover-props]
   ; @note (tutorials#parameterizing)
   (fn [_ cover-props]
       (let [cover-props (pretty-presets.engine/apply-preset     cover-id cover-props)
             cover-props (cover.prototypes/cover-props-prototype cover-id cover-props)]
            [cover cover-id cover-props]))))
