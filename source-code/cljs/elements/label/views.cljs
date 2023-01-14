
(ns elements.label.views
    (:require [elements.label.helpers    :as label.helpers]
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
  (if helper [:div.e-label--helper {:data-font-size   :xs
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
                        [:div.e-label--info-text {:data-font-size   :xs
                                                  :data-line-height :text-block}
                                                 (x.components/content info-text)])))

(defn label-info-text-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:info-text (metamorphic-content)(opt)}
  [label-id {:keys [info-text] :as label-props}]
  (if info-text [:button.e-label--info-text-button (label.helpers/label-info-text-button-attributes label-id label-props)
                                                   :info_outline]))

(defn- label-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:icon (keyword)}
  [_ {:keys [icon] :as label-props}]
  [:i.e-label--icon (pretty-css/icon-attributes {} label-props)
                    icon])

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
  [:div.e-label--placeholder {:data-color         :highlight
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
  ;    ... when the user moves the pointer over the sensor, a bubble shown up with
  ;        the label 'Copy' (as a pseudo-element).
  ;    ... The bubble is implemented by the {:data-bubble-content ...} CSS preset,
  ;        which has to be applied on the sensor element.
  ; 2. The .e-label--content element has the {overflow: hidden} setting, therefore it's
  ;    not capable to be applied with the {:data-bubble-content ...} preset.
  ; 3. The .e-label--body element always fits with its environment in width, therefore
  ;    it's often too wide to be the sensor element.
  (if copyable? [:div.e-label--copyable (label.helpers/copyable-attributes label-id label-props)
                                        [:label.e-label--content (label.helpers/content-attributes label-id label-props)
                                                                 content]]
                [:<>                    [:label.e-label--content (label.helpers/content-attributes label-id label-props)
                                                                 content]]))

(defn- label-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:content (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :selectable? (boolean)}
  [label-id {:keys [content icon selectable?] :as label-props}]
  [:div.e-label--body (label.helpers/label-body-attributes label-id label-props)
                      (if (empty? content)
                          [label-placeholder label-id label-props]
                          [:<> (if icon [label-icon label-id label-props])
                               [label-content          label-id label-props]
                               [label-info-text-button label-id label-props]])])

(defn- label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  [label-id label-props]
  [:div.e-label (label.helpers/label-attributes label-id label-props)
                [label-body                     label-id label-props]
                [label-info-text                label-id label-props]
                [label-helper                   label-id label-props]])

(defn element
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; {:class (keyword or keywords in vector)(opt)
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
  ;   :material-icons-filled, :material-icons-outlined
  ;   Default: :material-icons-filled
  ;  :icon-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
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
  ;   Same as the :indent property.
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
  ;   :ellipsis, :wrap
  ;   Default: :ellipsis}
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
