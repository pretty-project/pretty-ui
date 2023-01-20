
(ns elements.label.views
    (:require [elements.label.attributes :as label.attributes]
              [elements.label.helpers    :as label.helpers]
              [elements.label.prototypes :as label.prototypes]
              [pretty-css.api            :as pretty-css]
              [random.api                :as random]
              [x.components.api          :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:helper (metamorphic-content)}
  [_ {:keys [helper]}]
  (if helper [:div {:class            :e-label--helper
                    :data-font-size   :xs
                    :data-line-height :text-block}
                   (x.components/content helper)]))

(defn label-info-text
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:info-text (metamorphic-content)(opt)}
  [label-id {:keys [info-text]}]
  (if info-text (if-let [info-text-visible? (label.helpers/info-text-visible? label-id)]
                        [:div {:class            :e-label--info-text
                               :data-font-size   :xs
                               :data-line-height :text-block}
                              (x.components/content info-text)])))

(defn label-info-text-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:info-text (metamorphic-content)(opt)}
  [label-id {:keys [info-text] :as label-props}]
  (if info-text [:button (label.attributes/label-info-text-button-attributes label-id label-props) :info]))

(defn- label-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:icon (keyword)}
  [_ {:keys [icon] :as label-props}]
  (if icon [:i (pretty-css/icon-attributes {:class :e-label--icon} label-props) icon]))

(defn- label-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:placeholder (metamorphic-content)(opt)}
  [_ {:keys [placeholder]}]
  ; BUG#9811
  ; In some cases the content is an empty string for a short while before it
  ; gets its value (e.g. from a subscription or a HTTP request, etc.),
  ; therefore the placeholder has to be the same height even if it's empty!
  ;
  ; Otherwise an empty placeholder and a delayed content would cause a short
  ; flickering by the inconsistent element height!
  ;
  ; Solution:
  ; In the case of the placeholder is an empty string too, the "\u00A0" white
  ; character provides the consistent height for the element until the content
  ; gets its value.
  [:div {:class              :e-label--placeholder
         :data-color         :highlight
         :data-selectable    false
         :data-text-overflow :ellipsis}
        (if placeholder (x.components/content placeholder)
                        "\u00A0")])

(defn- label-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:content (string)
  ;  :copyable? (boolean)(opt)
  ;  :target-id (keyword)(opt)}
  [label-id {:keys [content copyable? target-id] :as label-props}]
  ; https://css-tricks.com/html-inputs-and-labels-a-love-story/
  ; ... it is always the best idea to use an explicit label instead of an implicit label.
  ;
  ; XXX#7009 (source-code/cljs/elements/label/prototypes.cljs)
  ;
  ; XXX#7030 Why the {:copyable? true} setting needs the .e-label--copyable element?
  ; 1. By using the 'label' element with {:copyable? true} setting, ...
  ;    ... the content will be a clickable sensor and by clicking on it, it copies
  ;        the content to the clipboard.
  ;    ... when the user moves the pointer over the sensor, a tooltip shown up with
  ;        the label 'Copy' (as a pseudo-element).
  ;    ... The tooltip is implemented by the {:data-tooltip-content ...} CSS preset,
  ;        which has to be applied on the sensor element.
  ; 2. The .e-label--content element has the {overflow: hidden} setting, therefore it's
  ;    not capable to be applied with the {:data-tooltip-content ...} preset.
  ; 3. The .e-label--body element always fits with its environment in width, therefore
  ;    it's often too wide to be the sensor element.
  (if copyable? [:div (label.attributes/copyable-attributes label-id label-props)
                      [:label (label.attributes/content-attributes label-id label-props) content]]
                [:<>  [:label (label.attributes/content-attributes label-id label-props) content]]))

(defn- label-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:content (string)(opt)
  ;  :icon-position (keyword)(opt)}
  [label-id {:keys [content icon-position] :as label-props}]
  [:div (label.attributes/label-body-attributes label-id label-props)
        (if (empty? content)
            [label-placeholder label-id label-props]
            (case icon-position :left  [:<> [label-icon             label-id label-props]
                                            [label-content          label-id label-props]
                                            [label-info-text-button label-id label-props]]
                                :right [:<> [label-info-text-button label-id label-props]
                                            [label-content          label-id label-props]
                                            [label-icon             label-id label-props]]
                                       [:<> [label-content          label-id label-props]
                                            [label-info-text-button label-id label-props]]))])

(defn- label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  [label-id label-props]
  [:div (label.attributes/label-attributes label-id label-props)
        [label-body                        label-id label-props]
        [label-info-text                   label-id label-props]
        [label-helper                      label-id label-props]])

(defn element
  ; XXX#0721
  ; Some other items based on the label element and their documentations are linked to here.
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
  ;  :copyable? (boolean)(opt)
  ;   Default: false
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :font-weight (keyword)(opt)
  ;   :inherit, :extra-light, :light, :normal, :medium, :bold, :extra-bold
  ;   Default :medium
  ;  :gap (keyword)(opt)
  ;   Distance between the icon, info-text button and label
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
  ;   :inherit, :native, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :text-block
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :marker-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :placeholder (metamorphic-content)(opt)
  ;  :selectable? (boolean)(opt)
  ;   Default: false
  ;  :style (map)(opt)
  ;  :target-id (keyword)(opt)
  ;   The input element's id, that you want to connect with the label.
  ;  :vertical-position (keyword)(opt)
  ;   :bottom, :center, :top
  ;  :text-direction (keyword)(opt)
  ;   :normal, :reversed
  ;   Default :normal
  ;  :text-overflow (keyword)(opt)
  ;   :ellipsis, :no-wrap, :wrap
  ;   Default: :ellipsis
  ;  :text-transform (keyword)(opt)
  ;   :capitalize, :lowercase, :uppercase}
  ;
  ; @usage
  ; [label {...}]
  ;
  ; @usage
  ; [label :my-label {...}]
  ([label-props]
   [element (random/generate-keyword) label-props])

  ([label-id label-props]
   (let [label-props (label.prototypes/label-props-prototype label-props)]
        [label label-id label-props])))
