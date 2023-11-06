
(ns pretty-elements.label.views
    (:require [pretty-elements.label.attributes :as label.attributes]
              [pretty-elements.label.env        :as label.env]
              [pretty-elements.label.prototypes :as label.prototypes]
              [metamorphic-content.api   :as metamorphic-content]
              [pretty-css.api            :as pretty-css]
              [pretty-presets.api        :as pretty-presets]
              [random.api                :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-helper
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:helper (metamorphic-content)}
  [_ {:keys [helper]}]
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
  (if icon [:i (pretty-css/icon-attributes {:class :pe-label--icon} label-props) icon]))

(defn- label-content
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:content (metamorphic-content)(opt)
  ;  :on-copy (boolean)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;  :target-id (keyword)(opt)}
  [label-id {:keys [content on-copy placeholder target-id] :as label-props}]
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
  ; 2. The '.pe-label--content' element has the '{overflow: hidden}' setting, therefore it's
  ;    not capable to be applied with the '{:data-tooltip-content ...}' preset.
  ; 3. The '.pe-label--body' element always fits with its environment in width, therefore
  ;    it's often too wide to be the sensor element.
  ;
  ; XXX#7039 Why the label element without a 'target-id' property shown in a DIV tag?
  ; - A label element without a 'target-id' value doesn't use 'for' HTML attribute and
  ;   it would violate the HTML rules:
  ;   "A <label> isn't associated with a form field."
  ;   "To fix this issue, nest the <input> in the <label> or provide a for attribute on the <label> that matches a form field id."
  ; - Therefore if no 'target-id' value provided (=> no 'for' attribute on the HTML element)
  ;   it's better to use a DIV tag instead of using a LABEL tag.
  (if on-copy [:div (label.attributes/copyable-attributes label-id label-props)
                    [(if target-id :label :div)
                     (label.attributes/content-attributes label-id label-props) (metamorphic-content/compose content placeholder)]]
              [:<>  [(if target-id :label :div)
                     (label.attributes/content-attributes label-id label-props) (metamorphic-content/compose content placeholder)]]))

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

(defn element
  ; @info
  ; XXX#0721
  ; Some other items based on the 'label' element and their documentations link here.
  ;
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; {:border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :border-position (keyword)(opt)
  ;   :all, :bottom, :top, :left, :right, :horizontal, :vertical
  ;  :border-radius (map)(opt)
  ;   {:tl (keyword)(opt)
  ;    :tr (keyword)(opt)
  ;    :br (keyword)(opt)
  ;    :bl (keyword)(opt)
  ;    :all (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :inherit
  ;  :content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :font-weight (keyword)(opt)
  ;   :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  ;   Default :medium
  ;  :gap (keyword)(opt)
  ;   Distance between the icon, the info-text button and the label
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :auto
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right
  ;   Default: :left
  ;  :horizontal-position (keyword)(opt)
  ;   :center, :left, :right
  ;  :icon (keyword)(opt)
  ;  :icon-color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :inherit
  ;  :icon-family (keyword)(opt)
  ;   :material-symbols-filled, :material-symbols-outlined
  ;   Default: :material-symbols-outlined
  ;  :icon-position (keyword)(opt)
  ;   :left, :right
  ;   Default: :left
  ;  :icon-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :info-text (metamorphic-content)(opt)
  ;  :line-height (keyword)(opt)
  ;   :auto, :inherit, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :text-block
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :marker-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl, left, :right, bottom, :top
  ;  :min-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :on-copy (Re-Frame metamorphic-event)(opt)
  ;   This event takes the label content as its last parameter
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :placeholder (metamorphic-content)(opt)
  ;   Default: "\u00A0"
  ;  :preset (keyword)(opt)
  ;  :selectable? (boolean)(opt)
  ;   Default: false
  ;  :style (map)(opt)
  ;  :target-id (keyword)(opt)
  ;   The input element's ID, that you want to connect to the label with using the 'for' HTML attribute.
  ;  :text-direction (keyword)(opt)
  ;   :normal, :reversed
  ;   Default :normal
  ;  :text-overflow (keyword)(opt)
  ;   :ellipsis, :hidden, :wrap
  ;  :text-transform (keyword)(opt)
  ;   :capitalize, :lowercase, :uppercase
  ;  :tooltip-content (metamorphic-content)(opt)
  ;  :tooltip-position (keyword)(opt)
  ;   :left, :right
  ;  :vertical-position (keyword)(opt)
  ;   :bottom, :center, :top
  ;  :width (keyword)(opt)
  ;   :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :content}
  ;
  ; @usage
  ; [label {...}]
  ;
  ; @usage
  ; [label :my-label {...}]
  ([label-props]
   [element (random/generate-keyword) label-props])

  ([label-id label-props]
   (fn [_ label-props] ; XXX#0106 (README.md#parametering)
       (let [label-props (pretty-presets/apply-preset            label-props)
             label-props (label.prototypes/label-props-prototype label-props)]
            [label label-id label-props]))))
