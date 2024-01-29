
(ns pretty-elements.label.views
    (:require [fruits.random.api                :as random]
              [metamorphic-content.api          :as metamorphic-content]
              [pretty-css.content.api           :as pretty-css.content]
              [pretty-elements.label.attributes :as label.attributes]
              [pretty-elements.label.env        :as label.env]
              [pretty-elements.label.prototypes :as label.prototypes]
              [pretty-elements.engine.api                :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                      :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-helper
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:helper (metamorphic-content)(opt)}
  [_ {:keys [helper]}]
  ; The 'label' element implements the 'helper' property for other elements that use the 'label' element as their actual label.
  (if helper [:div {:class               :pe-label--helper
                    :data-font-size      :xs
                    :data-letter-spacing :auto
                    :data-line-height    :text-block}
                   (metamorphic-content/compose helper)]))

(defn label-info-text
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:info-text (metamorphic-content)(opt)}
  [label-id {:keys [info-text]}]
  (if info-text (if-let [info-text-visible? (label.env/info-text-visible? label-id)]
                        [:div {:class               :pe-label--info-text
                               :data-font-size      :xs
                               :data-letter-spacing :auto
                               :data-line-height    :text-block}
                              (metamorphic-content/compose info-text)])))

(defn label-info-text-button
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:info-text (metamorphic-content)(opt)}
  [label-id {:keys [info-text] :as label-props}]
  (if info-text [:button (label.attributes/label-info-text-button-attributes label-id label-props) :info]))

(defn- label-icon
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:icon (keyword)}
  [_ {:keys [icon] :as label-props}]
  (if icon [:i (pretty-css.content/icon-attributes {:class :pe-label--icon} label-props) icon]))

(defn- label-content
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:content (metamorphic-content)(opt)
  ;  :on-copy (boolean)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [label-id {:keys [content on-copy placeholder] :as label-props}]
  ; https://css-tricks.com/html-inputs-and-labels-a-love-story/
  ; ... it is always the best idea to use an explicit label instead of an implicit label.
  ;
  ; XXX#7030 Why the '{:on-copy ...}' setting needs the '.pe-label--copyable' element?
  ; 1. By using the 'label' element with '{:on-copy ...}' setting, ...
  ;    ... the content will be a clickable sensor and by clicking on it, it copies
  ;        the content to the clipboard by using the given event.
  ;    ... when the user moves the pointer over the sensor, a tooltip shown up with
  ;        the label 'Copy' (as a pseudo-element).
  ;    ... The tooltip is implemented by the '{:data-tooltip-content ...}' CSS preset,
  ;        which has to be applied on the sensor element.
  ; 2. The '.pe-label--content' element has the '{overflow: hidden}' setting.
  ;    Therefore, it isn't capable to be applied with the '{:data-tooltip-content ...}' preset.
  ; 3. The '.pe-label--body' element always fits with its environment in width.
  ;    Therefore it's often too wide to be the sensor element.
  (if on-copy [:div (label.attributes/copyable-attributes label-id label-props)
                    [:div (label.attributes/content-attributes label-id label-props)
                          (metamorphic-content/compose content placeholder)]]
              [:<>  [:div (label.attributes/content-attributes label-id label-props)
                          (metamorphic-content/compose content placeholder)]]))

(defn- label-body
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:content (string)(opt)
  ;  :icon-position (keyword)(opt)}
  [label-id {:keys [content icon-position] :as label-props}]
  [:div (label.attributes/label-body-attributes label-id label-props)
        (case icon-position :left  [:<> [label-icon             label-id label-props]
                                        [label-content          label-id label-props]
                                        [label-info-text-button label-id label-props]]
                            :right [:<> [label-info-text-button label-id label-props]
                                        [label-content          label-id label-props]
                                        [label-icon             label-id label-props]]
                                   [:<> [label-content          label-id label-props]
                                        [label-info-text-button label-id label-props]])])

(defn- label
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  [label-id label-props]
  [:div (label.attributes/label-attributes label-id label-props)
        [label-body                        label-id label-props]
        [label-info-text                   label-id label-props]
        [label-helper                      label-id label-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  [label-id label-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    label-id label-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount label-id label-props))
                       :reagent-render         (fn [_ label-props] [label label-id label-props])}))

(defn element
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :focus-id (keyword)(opt)
  ;   ID of a Pretty input that should be focused when the user clicks on the label.
  ;  :font-size (keyword, px or string)(opt)
  ;   Default: :s
  ;  :font-weight (keyword or integer)(opt)
  ;   Default :medium
  ;  :gap (keyword, px or string)(opt)
  ;   Distance between the icon, the info-text button and the content.
  ;  :height (keyword, px or string)(opt)
  ;  :helper (metamorphic-content)(opt)
  ;  :horizontal-align (keyword)(opt)
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
  ;  :info-text (metamorphic-content)(opt)
  ;  :line-height (keyword, px or string)(opt)
  ;   Default: :text-block
  ;  :marker-color (keyword or string)(opt)
  ;  :marker-position (keyword)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-copy-f (function)(opt)
  ;   Takes the label content as parameter.
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;   Default: "\u00A0"
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :text-color (keyword or string)(opt)
  ;   Default: :inherit
  ;  :text-direction (keyword)(opt)
  ;   :normal, :reversed
  ;   Default :normal
  ;  :text-overflow (keyword)(opt)
  ;  :text-selectable? (boolean)(opt)
  ;  :text-transform (keyword)(opt)
  ;  :theme (keyword)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)
  ;  :tooltip-position (keyword)(opt)
  ;  :vertical-align (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [label {...}]
  ;
  ; @usage
  ; [label :my-label {...}]
  ([label-props]
   [element (random/generate-keyword) label-props])

  ([label-id label-props]
   ; @note (tutorials#parametering)
   (fn [_ label-props]
       (let [label-props (pretty-presets.engine/apply-preset     label-id label-props)
             label-props (label.prototypes/label-props-prototype label-id label-props)]
            [element-lifecycles label-id label-props]))))
