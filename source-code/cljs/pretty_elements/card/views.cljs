
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
  ; {:href (string)(opt)
  ;  :on-click (Re-Frame metamorphic-event)(opt)}
  [card-id {:keys [content href on-click] :as card-props}]
  [:div (card.attributes/card-attributes card-id card-props)
        [(cond href :a on-click :button :else :div)
         (card.attributes/card-body-attributes card-id card-props)
         [metamorphic-content/compose content]]])

(defn element
  ; @info
  ; XXX#3240
  ; Some other items based on the 'card' element and their documentations link here.
  ;
  ; @param (keyword)(opt) card-id
  ; @param (map) card-props
  ; {:badge-color (keyword)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :primary
  ;  :badge-content (metamorphic-content)(opt)
  ;  :badge-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl, :left, :right, :bottom, :top
  ;   Default: :tr
  ;  :border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :border-position (keyword)(opt)
  ;   :all, :bottom, :top, :left, :right, :horizontal, :vertical
  ;  :border-radius (map)(opt)
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :content (metamorphic-content)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :cursor (keyword)(opt)
  ;   :default, :disabled, :grab, :grabbing, :move, :pointer, :progress
  ;   Default: :default or :pointer
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :height (keyword)(opt)
  ;   :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :auto
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right
  ;   Default: :left
  ;  :hover-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :hover-effect (keyword)(opt)
  ;   :opacity
  ;  :href (string)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
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
  ;  :on-click (Re-Frame metamorphic-event)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :target (keyword)(opt)
  ;   :blank, :self
  ;  :width (keyword)(opt)
  ;   :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :content}
  ;
  ; @usage
  ; [card {...}]
  ;
  ; @usage
  ; [card :my-card {...}]
  ([card-props]
   [element (random/generate-keyword) card-props])

  ([card-id card-props]
   (fn [_ card-props] ; XXX#0106 (tutorials.api#parametering)
       (let [card-props (pretty-presets/apply-preset          card-props)
             card-props (card.prototypes/card-props-prototype card-props)]
            [card card-id card-props]))))
