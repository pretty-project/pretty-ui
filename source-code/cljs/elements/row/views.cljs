
(ns elements.row.views
    (:require [elements.row.helpers    :as row.helpers]
              [elements.row.prototypes :as row.prototypes]
              [random.api              :as random]
              [x.components.api        :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- row-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:content (metamorphic-content)(opt)}
  [row-id {:keys [content] :as row-props}]
  [:div.e-row--body (row.helpers/row-body-attributes row-id row-props)
                    [x.components/content            row-id content]])

(defn- row
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  [row-id row-props]
  [:div.e-row (row.helpers/row-attributes row-id row-props)
              [row-body                   row-id row-props]])

(defn element
  ; @param (keyword)(opt) row-id
  ; @param (map) row-props
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
  ;  :content (metamorphic-content)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :gap (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :auto
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right, :space-around, :space-between, :space-evenly
  ;   Default: :left
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :style (map)(opt)
  ;  :stretch-orientation (keyword)(opt)
  ;   :horizontal, :vertical, :both, :none
  ;   Default: :horizontal
  ;  :vertical-align (keyword)(opt)
  ;   :top, :center, :bottom
  ;   Default: :center
  ;  :wrap-items? (boolean)(opt)
  ;   Default: true}
  ;
  ; @usage
  ; [row {...}]
  ;
  ; @usage
  ; [row :my-row {...}]
  ([row-props]
   [element (random/generate-keyword) row-props])

  ([row-id row-props]
   (let [row-props (row.prototypes/row-props-prototype row-props)]
        [row row-id row-props])))
