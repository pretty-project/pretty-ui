
(ns pretty-elements.icon-button.views
    (:require [fruits.random.api                      :as random]
              [pretty-elements.engine.api             :as pretty-elements.engine]
              [pretty-elements.icon-button.attributes :as icon-button.attributes]
              [pretty-elements.icon-button.prototypes :as icon-button.prototypes]
              [pretty-presets.engine.api              :as pretty-presets.engine]
              [pretty-models.api             :as pretty-models]
              [reagent.core :as reagent]
              [pretty-accessories.api :as pretty-accessories]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-button
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:badge (map)(opt)
  ;  :cover (map)(opt)
  ;  :icon (map)(opt)
  ;  :label (map)(opt)
  ;  :marker (map)(opt)
  ;  ...}
  [button-id {:keys [badge cover icon label marker] :as button-props}]
  [:div (icon-button.attributes/button-attributes button-id button-props)
        [(pretty-models/clickable-auto-tag               button-id button-props)
         (icon-button.attributes/button-inner-attributes button-id button-props)
         (if icon   [pretty-accessories/icon   button-id icon])
         (if label  [pretty-accessories/label  button-id label])
         (if badge  [pretty-accessories/badge  button-id badge])
         (if marker [pretty-accessories/marker button-id marker])
         (if cover  [pretty-accessories/cover  button-id cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    button-id button-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount button-id button-props))
                         :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   button-id button-props %))
                         :reagent-render         (fn [_ button-props] [icon-button button-id button-props])}))

(defn view
  ; @description
  ; Icon button element with optional keypress control, timeout lock, and progress display.
  ;
  ; @links Implemented accessories
  ; [Badge](pretty-ui/cljs/pretty-accessories/api.html#badge)
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
  ; [Icon](pretty-ui/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ; [Marker](pretty-ui/cljs/pretty-accessories/api.html#marker)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Clickable state properties](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Effect properties](pretty-core/cljs/pretty-properties/api.html#effect-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Keypress properties](pretty-core/cljs/pretty-properties/api.html#keypress-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Progress properties](pretty-core/cljs/pretty-properties/api.html#progress-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/icon-button.png)
  ; [icon-button {:border-radius {:all :s}
  ;               :fill-color    :highlight
  ;               :icon          {:icon-name :settings}
  ;               :gap           :xxs
  ;               :indent        {:all :xxs}
  ;               :label         {:content "My icon button"}}]
  ([button-props]
   [view (random/generate-keyword) button-props])

  ([button-id button-props]
   ; @note (tutorials#parameterizing)
   (fn [_ button-props]
       (let [button-props (pretty-presets.engine/apply-preset            button-id button-props)
             button-props (icon-button.prototypes/button-props-prototype button-id button-props)
             button-props (pretty-elements.engine/element-timeout-props  button-id button-props)]
            [view-lifecycles button-id button-props]))))
