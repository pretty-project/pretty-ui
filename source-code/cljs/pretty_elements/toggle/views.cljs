
(ns pretty-elements.toggle.views
    (:require [fruits.random.api                 :as random]
              [pretty-elements.engine.api        :as pretty-elements.engine]
              [pretty-elements.toggle.attributes :as toggle.attributes]
              [pretty-elements.toggle.prototypes :as toggle.prototypes]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-models.api             :as pretty-models]
              [pretty-accessories.api :as pretty-accessories]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ; {:badge (map)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :cover (map)(opt)
  ;  :marker (map)(opt)
  ;  ...}
  [toggle-id {:keys [badge content cover marker] :as toggle-props}]
  [:div (toggle.attributes/toggle-attributes toggle-id toggle-props)
        [(pretty-models/clickable-auto-tag          toggle-id toggle-props)
         (toggle.attributes/toggle-inner-attributes toggle-id toggle-props)
         (if content [:div (toggle.attributes/toggle-content-attributes toggle-id toggle-props) content])
         (if badge   [pretty-accessories/badge  toggle-id badge])
         (if marker  [pretty-accessories/marker toggle-id marker])
         (if cover   [pretty-accessories/cover  toggle-id cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  [toggle-id toggle-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    toggle-id toggle-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount toggle-id toggle-props))
                         :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   toggle-id toggle-props %))
                         :reagent-render         (fn [_ toggle-props] [toggle toggle-id toggle-props])}))

(defn view
  ; @description
  ; Clickable wrapper element with optional keypress control and timeout lock.
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
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
  ; [Clickable state properties](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Effect properties](pretty-core/cljs/pretty-properties/api.html#effect-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
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
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) toggle-id
  ; @param (map) toggle-props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/toggle.png)
  ; [toggle {:border-radius {:all :m}
  ;          :content       [:div "My toggle"]
  ;          :fill-color    :highlight
  ;          :href-uri      "/my-uri"
  ;          :indent        {:all :s}}]
  ([toggle-props]
   [view (random/generate-keyword) toggle-props])

  ([toggle-id toggle-props]
   ; @note (tutorials#parameterizing)
   (fn [_ toggle-props]
       (let [toggle-props (pretty-presets.engine/apply-preset           toggle-id toggle-props)
             toggle-props (toggle.prototypes/toggle-props-prototype     toggle-id toggle-props)
             toggle-props (pretty-elements.engine/element-timeout-props toggle-id toggle-props)]
            [view-lifecycles toggle-id toggle-props]))))
