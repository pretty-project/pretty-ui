
(ns elements.thumbnail.views
    (:require [css.api                       :as css]
              [elements.element.views        :as element.views]
              [elements.thumbnail.attributes :as thumbnail.attributes]
              [elements.thumbnail.prototypes :as thumbnail.prototypes]
              [random.api                    :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- thumbnail
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:background-size (keyword)
  ;  :href (string)(opt)
  ;  :on-click (Re-Frame metamorphic-event)(opt)
  ;  :uri (string)(opt)}
  [thumbnail-id {:keys [background-size href on-click uri] :as thumbnail-props}]
  [:div (thumbnail.attributes/thumbnail-attributes thumbnail-id thumbnail-props)
        [element.views/element-label thumbnail-id thumbnail-props]
        [(cond href :a on-click :button :else :div)
         (thumbnail.attributes/thumbnail-body-attributes thumbnail-id thumbnail-props)
         [:i   {:class :e-thumbnail--icon :data-icon-family :material-symbols-outlined :data-icon-size :s} :image]
         [:div {:class :e-thumbnail--image :style {:background-image (css/url uri) :background-size background-size}}]]])

(defn element
  ; @param (keyword)(opt) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:background-size (keyword)(opt)
  ;   :contain, :cover
  ;   Default: :contain
  ;  :border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :border-position (keyword)(opt)
  ;   :all, :bottom, :top, :left, :right, :horizontal, :vertical
  ;  :border-radius (map)(opt)
  ;   {:tl (keyword)(opt)
  ;    :tr (keyword)(opt)
  ;    :br (keyword)(opt)
  ;    :bl (keyword)(opt)
  ;    :all (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;   Default: {:all :m}
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :s
  ;  :helper (metamorphic-content)(opt)
  ;  :href (string)(opt)
  ;  :icon (keyword)(opt)
  ;   Default: :icon
  ;  :icon-family (keyword)(opt)
  ;   :material-symbols-filled, :material-symbols-outlined
  ;   Default: :material-symbols-outlined
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
  ;  :on-click (Re-Frame metamorphic-event)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :style (map)(opt)
  ;  :target (keyword)(opt)
  ;   :blank, :self
  ;  :uri (string)(opt)
  ;  :width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :s}
  ;
  ; @usage
  ; [thumbnail {...}]
  ;
  ; @usage
  ; [thumbnail :my-thumbnail {...}]
  ([thumbnail-props]
   [element (random/generate-keyword) thumbnail-props])

  ([thumbnail-id thumbnail-props]
   (let [thumbnail-props (thumbnail.prototypes/thumbnail-props-prototype thumbnail-props)]
        [thumbnail thumbnail-id thumbnail-props])))
