
(ns pretty-elements.chip.views
    (:require [fruits.random.api               :as random]
              [metamorphic-content.api         :as metamorphic-content]
              [pretty-elements.chip.attributes :as chip.attributes]
              [pretty-elements.chip.prototypes :as chip.prototypes]
              [pretty-presets.api              :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {}
  [chip-id {:keys [content content-value-f href icon icon-family on-click primary-button] :as chip-props}]
  [:div (chip.attributes/chip-attributes chip-id chip-props)
        [(cond href :a on-click :button :else :div)
         (chip.attributes/chip-body-attributes chip-id chip-props)
         (if-let [{:keys [icon icon-family]} primary-button]
                 [:button (chip.attributes/primary-button-attributes chip-id chip-props)
                          [:i {:class :pe-chip--primary-button-icon :data-icon-family icon-family} icon]])
         (if primary-button [:div {:class :pe-chip--primary-button-spacer}])
         (if icon           [:i   {:class :pe-chip--icon :data-icon-family icon-family :data-icon-size :xs} icon]
                            [:div {:class :pe-chip--icon-placeholder}])
         (if content        [:div (chip.attributes/chip-content-attributes chip-id chip-props)
                                  [metamorphic-content/compose (content-value-f content)]])]])

(defn element
  ; @param (keyword)(opt) chip-id
  ; @param (map) chip-props
  ; {:border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;   Default: :opacity
  ;  :content (metamorphic-content)(opt)
  ;  :content-value-f (function)(opt)
  ;   Default: return
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :height (keyword, px or string)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :href (string)(opt)
  ;   TODO Makes the chip clickable
  ;  :icon (keyword)(opt)
  ;  :icon-family (keyword)(opt)
  ;   :material-symbols-filled, :material-symbols-outlined
  ;   Default: :material-symbols-outlined
  ;  :indent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :on-click (function or Re-Frame metamorphic-event)(opt)
  ;   TODO Makes the chip clickable
  ;  :outdent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :placeholder-value-f (function)(opt)
  ;   Default: return
  ;  :preset (keyword)(opt)
  ;  :primary-button (map)(opt)
  ;   {:click-effect (keyword)(opt)
  ;     Default: :opacity
  ;    :hover-effect (keyword)(opt)
  ;    :icon (keyword)
  ;    :icon-family (keyword)(opt)
  ;     :material-symbols-filled, :material-symbols-outlined
  ;     Default: :material-symbols-outlined
  ;    :on-click (function or Re-Frame metamorphic-event)}
  ;  :style (map)(opt)
  ;  :target (keyword)(opt)
  ;   Makes the chip clickable
  ;   :blank, :self
  ;   TODO
  ;  :text-color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [chip {...}]
  ;
  ; @usage
  ; [chip :my-chip {...}]
  ([chip-props]
   [element (random/generate-keyword) chip-props])

  ([chip-id chip-props]
   ; @note (tutorials#parametering)
   (fn [_ chip-props]
       (let [chip-props (pretty-presets/apply-preset          chip-props)
             chip-props (chip.prototypes/chip-props-prototype chip-props)]
            [chip chip-id chip-props]))))
