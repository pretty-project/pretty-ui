
(ns pretty-elements.button.views
    (:require [fruits.random.api                 :as random]
              [metamorphic-content.api           :as metamorphic-content]
              [pretty-elements.button.attributes :as button.attributes]
              [pretty-elements.button.prototypes :as button.prototypes]
              [pretty-presets.api                :as pretty-presets]
              [re-frame.api                      :as r]
              [reagent.api                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:content (metamorphic-content)(opt)
  ;  :href (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-position (keyword)(opt)
  ;  :on-click-f (function)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [button-id {:keys [content href icon icon-position on-click-f placeholder] :as button-props}]
  [:div (button.attributes/button-attributes button-id button-props)
        [(cond href :a on-click-f :button :else :button)
         (button.attributes/button-body-attributes button-id button-props)
         (case icon-position :left  [:<> (if   icon        [:i   (button.attributes/button-icon-attributes    button-id button-props) icon])
                                         (cond content     [:div (button.attributes/button-content-attributes button-id button-props) [metamorphic-content/compose content]]
                                               placeholder [:div (button.attributes/button-content-attributes button-id button-props) [metamorphic-content/compose placeholder]])]
                             :right [:<> (cond content     [:div (button.attributes/button-content-attributes button-id button-props) [metamorphic-content/compose content]]
                                               placeholder [:div (button.attributes/button-content-attributes button-id button-props) [metamorphic-content/compose placeholder]])
                                         (if   icon        [:i   (button.attributes/button-icon-attributes    button-id button-props) icon])]
                                    [:<> (cond content     [:div (button.attributes/button-content-attributes button-id button-props) [metamorphic-content/compose content]]
                                               placeholder [:div (button.attributes/button-content-attributes button-id button-props) [metamorphic-content/compose placeholder]])])]])

(defn button-lifecycles
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch [:pretty-elements.button/button-did-mount    button-id button-props]))
                       :component-will-unmount (fn [_ _] (r/dispatch [:pretty-elements.button/button-will-unmount button-id button-props]))
                       :component-did-update   (fn [%]   (r/dispatch [:pretty-elements.button/button-did-update   button-id %]))
                       :reagent-render         (fn [_ button-props] [button button-id button-props])}))

(defn element
  ; @info
  ; XXX#0714
  ; Some other items based on the 'button' element and their documentations link here.
  ;
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
  ;   Default: :primary
  ;  :badge-content (metamorphic-content)(opt)
  ;  :badge-position (keyword)(opt)
  ;   Default: :tr
  ;  :border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;   Default: :opacity (if 'href' of 'on-click-f' is provided)
  ;  :content (metamorphic-content)(opt)
  ;  :cursor (keyword or string)(opt)
  ;   Default: :pointer
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :font-size (keyword, px or string)(opt)
  ;   Default: :s
  ;  :font-weight (keyword or integer)(opt)
  ;   Default: :medium
  ;  :gap (keyword, px or string)(opt)
  ;   Distance between the icon and the content.
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;   Default: :center
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :href (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-color (keyword or string)(opt)
  ;   Default: :inherit
  ;  :icon-family (keyword)(opt)
  ;   Default: :material-symbols-outlined
  ;  :icon-position (keyword)(opt)
  ;   Default: :left
  ;  :icon-size (keyword, px or string)(opt)
  ;   Default: provided :font-size (if any) or :s
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :keypress (map)(opt)
  ;   {:exclusive? (boolean)(opt)
  ;     Exclusive keypress events temporarly disable every other previously registered keypress events.
  ;     Default: false
  ;    :key-code (integer)
  ;    :required? (boolean)(opt)
  ;     Only required keypress events remain active during a text-field is in focused state.
  ;     Default: false}
  ;  :line-height (keyword, px or string)(opt)
  ;   Default: :text-block
  ;  :marker-color (keyword or string)(opt)
  ;  :marker-position (keyword)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-mouse-over-f (function)(opt)
  ;  :on-right-click-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :progress (percent)(opt)
  ;  :progress-color (keyword or string)(opt)
  ;   Default: :muted
  ;  :progress-direction (keyword)(opt)
  ;   Default: :ltr
  ;  :progress-duration (ms)(opt)
  ;   Default: 250
  ;  :text-color (keyword or string)(opt)
  ;   Default: :inherit
  ;  :text-overflow (keyword)(opt)
  ;   Default: :hidden
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
       (let [button-props (pretty-presets/apply-preset                        button-props)
             button-props (button.prototypes/button-props-prototype button-id button-props)]
            [button-lifecycles button-id button-props]))))
