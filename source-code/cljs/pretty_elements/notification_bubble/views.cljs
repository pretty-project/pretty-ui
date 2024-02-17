
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
  ; {:content (metamorphic-content)(opt)
  ;  :cover (map)(opt)
  ;  :end-adornments (map)(opt)
  ;  :start-adornments (map)(opt)
  ;  ...}
  [bubble-id {:keys [content cover end-adornments start-adornments] :as bubble-props}]
  [:div (notification-bubble.attributes/bubble-attributes bubble-id bubble-props)
        [(pretty-elements.engine/clickable-auto-tag             bubble-id bubble-props)
         (notification-bubble.attributes/bubble-body-attributes bubble-id bubble-props)
         (when start-adornments [adornment-group.views/view bubble-id {:adornments start-adornments}])
         (when :always          [:div (notification-bubble.attributes/bubble-content-attributes bubble-id bubble-props) content])
         (when end-adornments   [adornment-group.views/view bubble-id {:adornments end-adornments}])
         (when cover            [pretty-accessories/cover   bubble-id cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  [bubble-id bubble-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    bubble-id bubble-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount bubble-id bubble-props))
                         :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   bubble-id bubble-props %))
                         :reagent-render         (fn [_ bubble-props] [notification-bubble bubble-id bubble-props])}))

(defn view
  ; @description
  ; Notification bubble element with optional adornments, keypress control, timeout lock, and progress display.
  ;
  ; @links Implemented accessories
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
  ;
  ; @links Implemented elements
  ; [Adornment-group](pretty-ui/cljs/pretty-elements/api.html#adornment-group)
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Clickable state properties](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
  ; [Effect properties](pretty-core/cljs/pretty-properties/api.html#effect-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Keypress properties](pretty-core/cljs/pretty-properties/api.html#keypress-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Progress properties](pretty-core/cljs/pretty-properties/api.html#progress-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) bubble-id
  ; @param (map) bubble-props
  ; Check out the implemented accessories.
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/notification-bubble.png)
  ; [notification-bubble {:border-radius         {:all :m}
  ;                       :content               "My notification bubble #1"
  ;                       :fill-color            :primary
  ;                       :indent                {:horizontal :s}
  ;                       :end-adornment-default {:fill-color :highlight :border-radius {:all :s}}
  ;                       :end-adornments        [{:icon :close}]
  ;                       :height                :xs
  ;                       :width                 :3xl}]
  ;
  ; [notification-bubble {:border-color            :highlight
  ;                       :border-radius           {:all :m}
  ;                       :content                 "My notification bubble #2"
  ;                       :fill-color              :highlight
  ;                       :indent                  {:horizontal :s}
  ;                       :start-adornment-default {:fill-color :default :border-color :highlight :border-radius {:all :s}}
  ;                       :start-adornments        [{:icon :close}]
  ;                       :height                  :xs
  ;                       :width                   :3xl}]
  ([bubble-props]
   [view (random/generate-keyword) bubble-props])

  ([bubble-id bubble-props]
   ; @note (tutorials#parameterizing)
   (fn [_ bubble-props]
       (let [bubble-props (pretty-presets.engine/apply-preset                            bubble-id bubble-props)
             bubble-props (notification-bubble.prototypes/bubble-props-prototype         bubble-id bubble-props)
             bubble-props (pretty-elements.engine/element-timeout-props                  bubble-id bubble-props)
             bubble-props (pretty-elements.engine/element-subitem-group<-subitem-default bubble-id bubble-props :start-adornments :start-adornment-default)
             bubble-props (pretty-elements.engine/element-subitem-group<-subitem-default bubble-id bubble-props :end-adornments   :end-adornment-default)
             bubble-props (pretty-elements.engine/element-subitem-group<-disabled-state  bubble-id bubble-props :start-adornments)
             bubble-props (pretty-elements.engine/element-subitem-group<-disabled-state  bubble-id bubble-props :end-adornments)]
            [view-lifecycles bubble-id bubble-props]))))
