
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
  [chip-id {:keys [content href icon icon-family on-click-f placeholder primary-button] :as chip-props}]
  [:div (chip.attributes/chip-attributes chip-id chip-props)
        [(cond href :a on-click-f :button :else :div)
         (chip.attributes/chip-body-attributes chip-id chip-props)
         (if-let [{:keys [icon icon-family]} primary-button]
                 [:button (chip.attributes/primary-button-attributes chip-id chip-props)
                          [:i {:class :pe-chip--primary-button-icon :data-icon-family icon-family} icon]])
         (if primary-button [:div {:class :pe-chip--primary-button-spacer}])
         (if icon           [:i   {:class :pe-chip--icon :data-icon-family icon-family :data-icon-size :xs} icon]
                            [:div {:class :pe-chip--icon-placeholder}])
         (cond content      [:div (chip.attributes/chip-content-attributes chip-id chip-props) [metamorphic-content/compose content]]
               placeholder  [:div (chip.attributes/chip-content-attributes chip-id chip-props) [metamorphic-content/compose placeholder]])]])

(defn element
  ; @param (keyword)(opt) chip-id
  ; @param (map) chip-props
  ; {:border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;   Default: :opacity (if 'on-click-f' is provided)
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
  ;   Default: :material-symbols-outlined
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-click-f (function)(opt)
  ;   TODO Makes the chip clickable
  ;  :on-mouse-over-f (function)(opt)
  ;  :on-right-click-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :primary-button (map)(opt)
  ;   {:click-effect (keyword)(opt)
  ;     Default: :opacity
  ;    :hover-effect (keyword)(opt)
  ;    :icon (keyword)
  ;    :icon-family-f (keyword)(opt)
  ;     Default: :material-symbols-outlined
  ;    :on-click-f (function)}
  ;  :style (map)(opt)
  ;  :target (keyword)(opt)
  ;   Makes the chip clickable
  ;   :blank, :self
  ;   TODO
  ;  :text-color (keyword or string)(opt)
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
