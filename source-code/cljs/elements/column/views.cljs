
(ns elements.column.views
    (:require [elements.column.attributes :as column.attributes]
              [elements.column.prototypes :as column.prototypes]
              [metamorphic-content.api    :as metamorphic-content]
              [random.api                 :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- column
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ; {:content (metamorphic-content)}
  [column-id {:keys [content] :as column-props}]
  [:div (column.attributes/column-attributes column-id column-props)
        [:div (column.attributes/column-body-attributes column-id column-props)
              [metamorphic-content/compose content]]])

(defn element
  ; @param (keyword)(opt) column-id
  ; @param (map) column-props
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
  ;  :content (metamorphic-content)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :gap (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :auto
  ;  :height (keyword)(opt)
  ;   :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :auto
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right
  ;   Default: :center
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :max-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :max-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :style (map)(opt)
  ;  :vertical-align (keyword)(opt)
  ;   :top, :center, :bottom, :space-around, :space-between, :space-evenly
  ;   Default: :top
  ;  :wrap-items? (boolean)(opt)
  ;   Default: false
  ;  :width (keyword)(opt)
  ;   :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :content}
  ;
  ; @usage
  ; [elements/column {...}]
  ;
  ; @usage
  ; [elements/column :my-column {...}]
  ([column-props]
   [element (random/generate-keyword) column-props])

  ([column-id column-props]
   (let [column-props (column.prototypes/column-props-prototype column-props)]
        [column column-id column-props])))
