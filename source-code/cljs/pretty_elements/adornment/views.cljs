
(ns pretty-elements.adornment.views
    (:require [fruits.random.api                    :as random]
              [pretty-elements.adornment.attributes :as adornment.attributes]
              [pretty-elements.adornment.prototypes :as adornment.prototypes]
              [pretty-elements.engine.api           :as pretty-elements.engine]
              [pretty-presets.engine.api            :as pretty-presets.engine]
              [pretty-accessories.api            :as pretty-accessories]
              [reagent.core :as reagent]
              [pretty-elements.icon.views :as icon.views]
              [pretty-elements.label.views :as label.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- adornment
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ; {:cover (map)(opt)
  ;  :icon (map)(opt)
  ;  :label (map)(opt)
  ;  ...}
  [adornment-id {:keys [cover icon label] :as adornment-props}]
  [:div (adornment.attributes/adornment-attributes adornment-id adornment-props)
        [(pretty-elements.engine/clickable-auto-tag       adornment-id adornment-props)
         (adornment.attributes/adornment-inner-attributes adornment-id adornment-props)
         (cond label [label.views/view         adornment-id label]
               icon  [icon.views/view          adornment-id icon])
         (when cover [pretty-accessories/cover adornment-id cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  [adornment-id adornment-props]
  ; @note (tutorials#parameterizing)
  ;
  ; @note (#8097)
  ; The 'element-did-update' function re-registers the keypress events of the element when the property map gets changed.
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    adornment-id adornment-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount adornment-id adornment-props))
                         :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   adornment-id adornment-props %))
                         :reagent-render         (fn [_ adornment-props] [adornment adornment-id adornment-props])}))

(defn view
  ; @description
  ; Downsized button element for adornment groups.
  ;
  ; @links Implemented accessories
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented elements
  ; [Icon](pretty-ui/cljs/pretty-elements/api.html#icon)
  ; [Label](pretty-ui/cljs/pretty-elements/api.html#label)
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Clickable state properties](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Effect properties](pretty-core/cljs/pretty-properties/api.html#effect-properties)
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
  ; @param (keyword)(opt) adornment-id
  ; @param (map) adornment-props
  ; Check out the implemented accessories.
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/adornment.png)
  ; [adornment {:border-radius {:all :xs}
  ;             :fill-color    :highlight
  ;             :hover-color   :highlight
  ;             :icon          {:icon-name :settings}}]
  ([adornment-props]
   [view (random/generate-keyword) adornment-props])

  ([adornment-id adornment-props]
   ; @note (tutorials#parameterizing)
   (fn [_ adornment-props]
       (let [adornment-props (pretty-presets.engine/apply-preset             adornment-id adornment-props)
             adornment-props (adornment.prototypes/adornment-props-prototype adornment-id adornment-props)
             adornment-props (pretty-elements.engine/element-timeout-props   adornment-id adornment-props)]
            [view-lifecycles adornment-id adornment-props]))))
