
(ns elements.thumbnail.views
    (:require [css.api                       :as css]
              [elements.element.views        :as element.views]
              [elements.thumbnail.helpers    :as thumbnail.helpers]
              [elements.thumbnail.prototypes :as thumbnail.prototypes]
              [random.api                    :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:background-size (keyword)
  ;  :uri (string)(opt)}
  [thumbnail-id {:keys [background-size uri] :as thumbnail-props}]
  [:button.e-thumbnail--body (thumbnail.helpers/toggle-thumbnail-body-attributes thumbnail-id thumbnail-props)
                             [:div.e-thumbnail--icon  {:data-icon-family :material-icons-filled :data-icon-size :s} :image]
                             [:div.e-thumbnail--image {:style {:background-image (css/url uri)
                                                               :background-size background-size}}]])

(defn- static-thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:background-size (keyword)
  ;  :icon (keyword)
  ;  :icon-family (keyword)
  ;  :uri (string)(opt)}
  [thumbnail-id {:keys [background-size icon icon-family uri] :as thumbnail-props}]
  [:div.e-thumbnail--body (thumbnail.helpers/static-thumbnail-body-attributes thumbnail-id thumbnail-props)
                          [:div.e-thumbnail--icon  {:data-icon-family icon-family :data-icon-size :s} icon]
                          [:div.e-thumbnail--image {:style {:background-image (css/url uri)
                                                            :background-size background-size}}]])

(defn- thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:on-click (metamorphic-event)(opt)}
  [thumbnail-id {:keys [on-click] :as thumbnail-props}]
  [:div.e-thumbnail (thumbnail.helpers/thumbnail-attributes thumbnail-id thumbnail-props)
                    [element.views/element-label thumbnail-id thumbnail-props]
                    (cond (some? on-click) [toggle-thumbnail thumbnail-id thumbnail-props]
                          (nil?  on-click) [static-thumbnail thumbnail-id thumbnail-props])])

(defn element
  ; @param (keyword)(opt) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:background-size (keyword)(opt)
  ;   :contain, :cover
  ;   Default: :contain
  ;  :border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :border-radius (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :m
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :s
  ;  :helper (metamorphic-content)(opt)
  ;  :icon (keyword)(opt)
  ;   Default: :icon
  ;  :icon-family (keyword)(opt)
  ;   Default: :material-icons-filled
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
  ;  :label (metamorphic-content)(opt)
  ;  :on-click (metamorphic-event)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :style (map)(opt)
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
