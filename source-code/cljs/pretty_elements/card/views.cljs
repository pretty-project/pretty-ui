
(ns pretty-elements.card.views
    (:require [fruits.random.api               :as random]
              [metamorphic-content.api         :as metamorphic-content]
              [pretty-elements.card.attributes :as card.attributes]
              [pretty-elements.card.prototypes :as card.prototypes]
              [pretty-presets.api              :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ; {:content (metamorphic-content)(opt)
  ;  :content-value-f (function)
  ;  :href (string)(opt)
  ;  :on-click (function)(opt)}
  [card-id {:keys [content content-value-f href on-click] :as card-props}]
  [:div (card.attributes/card-attributes card-id card-props)
        [(cond href :a on-click :button :else :div)
         (card.attributes/card-body-attributes card-id card-props)
         [metamorphic-content/compose (content-value-f content)]]])

(defn element
  ; @info
  ; XXX#3240
  ; Some other items based on the 'card' element and their documentations link here.
  ;
  ; @param (keyword)(opt) card-id
  ; @param (map) card-props
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
  ;   Default: :opacity (if 'href' or 'on-click' is provided)
  ;  :content (metamorphic-content)(opt)
  ;  :content-value-f (function)(opt)
  ;   Default: return
  ;  :cursor (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :hover-effect (keyword)(opt)
  ;  :href (string)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :marker-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl, left, :right, bottom, :top
  ;  :max-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :max-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :on-click (function or Re-Frame metamorphic-event)(opt)
  ;  :outdent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :placeholder-value-f (function)(opt)
  ;   Default: return
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :target (keyword)(opt)
  ;   :blank, :self
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [card {...}]
  ;
  ; @usage
  ; [card :my-card {...}]
  ([card-props]
   [element (random/generate-keyword) card-props])

  ([card-id card-props]
   ; @note (tutorials#parametering)
   (fn [_ card-props]
       (let [card-props (pretty-presets/apply-preset          card-props)
             card-props (card.prototypes/card-props-prototype card-props)]
            [card card-id card-props]))))
