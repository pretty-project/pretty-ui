
(ns pretty-elements.notification-bubble.views
    (:require [fruits.random.api                              :as random]
              [pretty-elements.engine.api                     :as pretty-elements.engine]
              [pretty-elements.adornment-group.views :as adornment-group.views]
              [pretty-elements.notification-bubble.attributes :as notification-bubble.attributes]
              [pretty-elements.notification-bubble.prototypes :as notification-bubble.prototypes]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-accessories.api :as pretty-accessories]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- notification-bubble
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:content (multitype-content)(opt)
  ;  :cover (map)(opt)
  ;  :end-adornment-group (map)(opt)
  ;  :start-adornment-group (map)(opt)
  ;  ...}
  [bubble-id {:keys [content cover end-adornment-group start-adornment-group] :as bubble-props}]
  [:div (notification-bubble.attributes/bubble-attributes bubble-id bubble-props)
        [:div (notification-bubble.attributes/bubble-inner-attributes bubble-id bubble-props)
              (if start-adornment-group [adornment-group.views/view bubble-id start-adornment-group])
              (if content               [:div (notification-bubble.attributes/bubble-content-attributes bubble-id bubble-props) content])
              (if end-adornment-group   [adornment-group.views/view bubble-id end-adornment-group])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  [bubble-id bubble-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    bubble-id bubble-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount bubble-id bubble-props))
                         :reagent-render         (fn [_ bubble-props] [notification-bubble bubble-id bubble-props])}))

(defn view
  ; @description
  ; Notification bubble element with optional adornments and progress display.
  ;
  ; @links Implemented elements
  ; [Adornment-group](pretty-ui/cljs/pretty-elements/api.html#adornment-group)
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Progress properties](pretty-core/cljs/pretty-properties/api.html#progress-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) bubble-id
  ; @param (map) bubble-props
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/notification-bubble.png)
  ; [notification-bubble {:border-radius       {:all :m}
  ;                       :content             "My notification bubble #1"
  ;                       :fill-color          :primary
  ;                       :indent              {:horizontal :s}
  ;                       :outer-height        :xs
  ;                       :outer-width         :3xl
  ;                       :end-adornment-group {:adornment-default {:fill-color :highlight :border-radius {:all :s}}
  ;                                             :adornments        [{:icon {:icon-name :close}}]}}]
  ;
  ; [notification-bubble {:border-color          :highlight
  ;                       :border-radius         {:all :m}
  ;                       :content               "My notification bubble #2"
  ;                       :fill-color            :highlight
  ;                       :indent                {:horizontal :s}
  ;                       :outer-height          :xs
  ;                       :outer-width           :3xl
  ;                       :start-adornment-group {:adornment-default {:fill-color :default :border-color :highlight :border-radius {:all :s}}
  ;                                               :adornments        [{:icon {:icon-name :close}}]}}]
  ([bubble-props]
   [view (random/generate-keyword) bubble-props])

  ([bubble-id bubble-props]
   ; @note (tutorials#parameterizing)
   (fn [_ bubble-props]
       (let [bubble-props (pretty-presets.engine/apply-preset                    bubble-id bubble-props)
             bubble-props (notification-bubble.prototypes/bubble-props-prototype bubble-id bubble-props)]
            [view-lifecycles bubble-id bubble-props]))))
