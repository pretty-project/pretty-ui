
(ns pretty-elements.card.views
    (:require [fruits.random.api               :as random]
              [pretty-elements.card.attributes :as card.attributes]
              [pretty-elements.card.prototypes :as card.prototypes]
              [pretty-elements.engine.api      :as pretty-elements.engine]
              [pretty-presets.engine.api       :as pretty-presets.engine]
              [reagent.api                     :as reagent]
              [pretty-accessories.api :as pretty-accessories]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ; {:badge (map)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :cover (map)(opt)
  ;  :marker (map)(opt)}
  [card-id {:keys [badge content cover marker] :as card-props}]
  [:div (card.attributes/card-attributes card-id card-props)
        [(pretty-elements.engine/clickable-auto-tag card-id card-props)
         (card.attributes/card-body-attributes      card-id card-props)
         (if content [:div (card.attributes/card-content-attributes card-id card-props) content])
         (if badge   [:<>  [pretty-accessories/badge                card-id badge]])
         (if marker  [:<>  [pretty-accessories/marker               card-id marker]])
         (if cover   [:<>  [pretty-accessories/cover                card-id cover]])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  [card-id card-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    card-id card-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount card-id card-props))
                       :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   card-id card-props %))
                       :reagent-render         (fn [_ card-props] [card card-id card-props])}))

(defn view
  ; @description
  ; Optionally clickable card style element for displaying content.
  ;
  ; @links Implemented accessories
  ; [Badge](pretty-ui/cljs/pretty-accessories/api.html#badge)
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
  ; [Marker](pretty-ui/cljs/pretty-accessories/api.html#marker)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Background properties color](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Clickable state properties](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Effect properties](pretty-core/cljs/pretty-properties/api.html#effect-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Keypress properties](pretty-core/cljs/pretty-properties/api.html#keypress-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) card-id
  ; @param (map) card-props
  ; Check out the implemented accessories below.
  ; Check out the implemented properties below.
  ;
  ; @usage (card.png)
  ; ...
  ([card-props]
   [view (random/generate-keyword) card-props])

  ([card-id card-props]
   ; @note (tutorials#parameterizing)
   (fn [_ card-props]
       (let [card-props (pretty-presets.engine/apply-preset           card-id card-props)
             card-props (card.prototypes/card-props-prototype         card-id card-props)
             card-props (pretty-elements.engine/element-timeout-props card-id card-props)]
            [view-lifecycles card-id card-props]))))
