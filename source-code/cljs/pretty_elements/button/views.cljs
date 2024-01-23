
(ns pretty-elements.button.views
    (:require [fruits.random.api                 :as random]
              [metamorphic-content.api           :as metamorphic-content]
              [pretty-elements.button.attributes :as button.attributes]
              [pretty-elements.button.prototypes :as button.prototypes]
              [pretty-presets.api :as pretty-presets]
              [pretty-engine.api :as pretty-engine]
              [reagent.api                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:href-uri (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-position (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :on-click-f (function)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [button-id {:keys [href-uri icon icon-position label on-click-f placeholder] :as button-props}]
  [:div (button.attributes/button-attributes button-id button-props)
        [(cond href-uri :a on-click-f :button :else :div)
         (button.attributes/button-body-attributes button-id button-props)
         (case icon-position :left  [:<> (if   icon        [:i   (button.attributes/button-icon-attributes  button-id button-props) icon])
                                         (cond label       [:div (button.attributes/button-label-attributes button-id button-props) [metamorphic-content/compose label]]
                                               placeholder [:div (button.attributes/button-label-attributes button-id button-props) [metamorphic-content/compose placeholder]])]
                             :right [:<> (cond label       [:div (button.attributes/button-label-attributes button-id button-props) [metamorphic-content/compose label]]
                                               placeholder [:div (button.attributes/button-label-attributes button-id button-props) [metamorphic-content/compose placeholder]])
                                         (if   icon        [:i   (button.attributes/button-icon-attributes  button-id button-props) icon])]
                                    [:<> (cond label       [:div (button.attributes/button-label-attributes button-id button-props) [metamorphic-content/compose label]]
                                               placeholder [:div (button.attributes/button-label-attributes button-id button-props) [metamorphic-content/compose placeholder]])])]])

(defn button-lifecycles
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-engine/element-did-mount    button-id button-props))
                       :component-will-unmount (fn [_ _] (pretty-engine/element-will-unmount button-id button-props))
                       :component-did-update   (fn [%]   (pretty-engine/element-did-update   button-id button-props %))
                       :reagent-render         (fn [_ button-props] [button button-id button-props])}))

(defn element
  ; @bug (#9912)
  ; If the keypress key-code is 13 (ENTER) the on-click-f function fires multiple times during the key is pressed!
  ; This phenomenon caused by:
  ; 1. The keydown event focuses the button via the 'button.side-effects/key-pressed' function.
  ; 2. One of the default actions of the 13 (ENTER) key is to fire the element's on-click
  ;    function on a focused button element. Therefore, the 'on-click-f' function
  ;    fires repeatedly during the 13 (ENTER) key is pressed.
  ; In case of using any other key than the 13 (ENTER) the 'on-click-f' function fires only by
  ; the 'button.side-effects/key-released' function.
  ;
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:badge-color (keyword or string)(opt)
  ;  :badge-content (metamorphic-content)(opt)
  ;  :badge-position (keyword)(opt)
  ;  :border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;  :cursor (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;  :font-weight (keyword or integer)(opt)
  ;  :gap (keyword, px or string)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :href-target (keyword)(opt)
  ;  :href-uri (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-color (keyword or string)(opt)
  ;  :icon-family (keyword)(opt)
  ;  :icon-position (keyword)(opt)
  ;  :icon-size (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :keypress (map)(opt)
  ;   {:exclusive? (boolean)(opt)
  ;     Exclusive keypress events temporarly disable every other previously registered keypress events.
  ;     Default: false
  ;    :key-code (integer)
  ;    :in-type-mode? (boolean)(opt)
  ;     Only in-type-mode keypress events remain active while a text-field is in focused state.
  ;     Default: false}
  ;  :label (metamorphic-content)(opt)
  ;  :letter-spacing (keyword, px or string)(opt)
  ;  :line-height (keyword, px or string)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :marker-position (keyword)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-click-timeout (ms)(opt)
  ;  :on-mouse-over-f (function)(opt)
  ;  :on-right-click-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :progress (percent)(opt)
  ;  :progress-color (keyword or string)(opt)
  ;  :progress-direction (keyword)(opt)
  ;  :progress-duration (ms)(opt)
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :text-color (keyword or string)(opt)
  ;  :text-overflow (keyword)(opt)
  ;  :text-transform (keyword)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)
  ;  :tooltip-position (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [button {...}]
  ;
  ; @usage
  ; [button :my-button {...}]
  ;
  ; @usage
  ; [button {:keypress {:key-code 13} :on-click-f (fn [_] (println "ENTER pressed"))}]
  ([button-props]
   [element (random/generate-keyword) button-props])

  ([button-id button-props]
   ; @note (tutorials#parametering)
   (fn [_ button-props]
       (let [button-props (pretty-presets/apply-preset              button-id button-props)
             button-props (button.prototypes/button-props-prototype button-id button-props)
             button-props (pretty-engine/element-timeout-props      button-id button-props)]
            [button-lifecycles button-id button-props]))))
