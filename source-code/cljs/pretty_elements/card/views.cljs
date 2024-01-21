
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
  ;  :href (string)(opt)
  ;  :on-click-f (function)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [card-id {:keys [content href on-click-f placeholder] :as card-props}]
  [:div (card.attributes/card-attributes card-id card-props)
        [(cond href :a on-click-f :button :else :div)
         (card.attributes/card-body-attributes card-id card-props)
         [metamorphic-content/compose content placeholder]]])

(defn element
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
  ;   Default: :opacity (if 'href' or 'on-click-f' is provided)
  ;  :content (metamorphic-content)(opt)
  ;  :cursor (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :href (string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :marker-color (keyword or string)(opt)
  ;  :marker-position (keyword)(opt)
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-mouse-over-f (function)(opt)
  ;  :on-right-click-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :target (keyword)(opt)
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
