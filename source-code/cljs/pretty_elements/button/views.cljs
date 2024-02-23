
(ns pretty-elements.button.views
    (:require [fruits.random.api                 :as random]
              [pretty-elements.button.attributes :as button.attributes]
              [pretty-elements.button.prototypes :as button.prototypes]
              [pretty-elements.engine.api        :as pretty-elements.engine]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-accessories.api :as pretty-accessories]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:badge (map)(opt)
  ;  :cover (map)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-position (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marker (map)(opt)
  ;  ...}
  [button-id {:keys [badge cover icon icon-position label marker] :as button-props}]
  [:div (button.attributes/button-attributes button-id button-props)
        [(pretty-elements.engine/clickable-auto-tag button-id button-props)
         (button.attributes/button-body-attributes  button-id button-props)
         (case icon-position :right [:<> (if label [:div (button.attributes/button-label-attributes button-id button-props) label])
                                         (if icon  [:i   (button.attributes/button-icon-attributes  button-id button-props) icon])]
                                    [:<> (if icon  [:i   (button.attributes/button-icon-attributes  button-id button-props) icon])
                                         (if label [:div (button.attributes/button-label-attributes button-id button-props) label])])
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
                         :reagent-render         (fn [_ button-props] [button button-id button-props])}))

(defn view
  ; @description
  ; Button element with optional keypress control, timeout lock, and progress display.
  ;
  ; @links Implemented accessories
  ; [Badge](pretty-ui/cljs/pretty-accessories/api.html#badge)
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
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
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Icon properties](pretty-core/cljs/pretty-properties/api.html#icon-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Keypress properties](pretty-core/cljs/pretty-properties/api.html#keypress-properties)
  ; [Label properties](pretty-core/cljs/pretty-properties/api.html#label-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Progress properties](pretty-core/cljs/pretty-properties/api.html#progress-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/button.png)
  ; [button {:border-radius {:all :l}
  ;          :fill-color    :primary
  ;          :gap           :auto
  ;          :icon          :settings
  ;          :icon-position :right
  ;          :indent        {:horizontal :s :vertical :xxs}
  ;          :label         "My button #1"
  ;          :outer-width   :5xl}]
  ;
  ; [button {:border-radius {:all :l}
  ;          :border-color  :highlight
  ;          :fill-color    :highlight
  ;          :gap           :auto
  ;          :icon          :people
  ;          :icon-position :left
  ;          :indent        {:horizontal :s :vertical :xxs}
  ;          :label         "My button #2"
  ;          :outer-width   :5xl}]
  ([button-props]
   [view (random/generate-keyword) button-props])

  ([button-id button-props]
   ; @note (tutorials#parameterizing)
   (fn [_ button-props]
       (let [button-props (pretty-presets.engine/apply-preset           button-id button-props)
             button-props (button.prototypes/button-props-prototype     button-id button-props)
             button-props (pretty-elements.engine/element-timeout-props button-id button-props)]
            [view-lifecycles button-id button-props]))))
