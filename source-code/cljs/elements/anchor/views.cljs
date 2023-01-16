
(ns elements.anchor.views
    (:require [elements.anchor.attributes :as anchor.attributes]
              [elements.anchor.prototypes :as anchor.prototypes]
              [random.api                 :as random]
              [x.components.api           :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- anchor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ; {:content (metamorphic-content)}
  [anchor-id {:keys [content] :as anchor-props}]
  [:div (anchor.attributes/anchor-attributes anchor-id anchor-props)
        [:a (anchor.attributes/anchor-body-attributes anchor-id anchor-props)
            [x.components/content anchor-id content]]])

(defn element
  ; @param (keyword)(opt) anchor-id
  ; @param (map) anchor-props
  ; {:color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :inherit
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :font-weight (keyword)(opt)
  ;   :inherit, :extra-light, :light, :normal, :medium, :bold, :extra-bold
  ;   Default: :medium
  ;  :href (string)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :line-height (keyword)(opt)
  ;   :inherit, :native, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :text-block
  ;  :on-click (metamorphic-event)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [anchor {...}]
  ;
  ; @usage
  ; [anchor :my-anchor {...}]
  ([anchor-props]
   [element (random/generate-keyword) anchor-props])

  ([anchor-id anchor-props]
   (let [anchor-props (anchor.prototypes/anchor-props-prototype anchor-props)]
        [anchor anchor-id anchor-props])))
