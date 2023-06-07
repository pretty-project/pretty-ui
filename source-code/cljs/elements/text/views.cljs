
(ns elements.text.views
    (:require [elements.element.views   :as element.views]
              [elements.text.attributes :as text.attributes]
              [elements.text.prototypes :as text.prototypes]
              [hiccup.api               :as hiccup]
              [metamorphic-content.api  :as metamorphic-content]
              [random.api               :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:content (metamorphic-content)(opt)
  ;  :on-copy (Re-Frame metamorphic-event)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [text-id {:keys [content on-copy placeholder] :as text-props}]
  [:div (text.attributes/text-attributes text-id text-props)
        [element.views/element-label  text-id text-props]
        [:div (text.attributes/text-body-attributes text-id text-props)
              (if on-copy [:div (text.attributes/copyable-attributes text-id text-props)
                                [:div (text.attributes/content-attributes text-id text-props)
                                      (hiccup/parse-newlines [:<> (metamorphic-content/compose content placeholder)])]]
                          [:<>  [:div (text.attributes/content-attributes text-id text-props)
                                      (hiccup/parse-newlines [:<> (metamorphic-content/compose content placeholder)])]])]])

(defn element
  ; @param (keyword)(opt) text-id
  ; @param (map) text-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :content (metamorphic-content)(opt)
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :font-weight (keyword)(opt)
  ;   :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  ;   Default: :normal
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right
  ;   Default: :left
  ;  :horizontal-position (keyword)(opt)
  ;   :center, :left, :right
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :line-height (keyword)(opt)
  ;   :auto, :inherit, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :text-block
  ;  :max-lines (integer)(opt)
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :on-copy (Re-Frame metamorphic-event)(opt)
  ;   This event takes the text content as its last parameter.
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :placeholder (metamorphic-content)(opt)
  ;   Default: "\u00A0"
  ;  :selectable? (boolean)(opt)
  ;   Default: true
  ;  :style (map)(opt)
  ;  :text-direction (keyword)(opt)
  ;   :normal, :reversed
  ;   Default :normal
  ;  :text-overflow (keyword)(opt)
  ;   :ellipsis, :wrap
  ;   Default: :wrap
  ;  :width (keyword)(opt)
  ;   :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :content}
  ;
  ; @usage
  ; [text {...}]
  ;
  ; @usage
  ; [text :my-text {...}]
  ([text-props]
   [element (random/generate-keyword) text-props])

  ([text-id text-props]
   (let [text-props (text.prototypes/text-props-prototype text-props)]
        [text text-id text-props])))
