
(ns pretty-elements.horizontal-separator.views
    (:require [fruits.random.api                               :as random]
              [metamorphic-content.api                         :as metamorphic-content]
              [pretty-elements.horizontal-separator.attributes :as horizontal-separator.attributes]
              [pretty-elements.horizontal-separator.prototypes :as horizontal-separator.prototypes]
              [pretty-presets.api                              :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-separator
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:color (keyword or string)(opt)
  ;  :label (metamorphic-content)(opt)}
  [separator-id {:keys [color label] :as separator-props}]
  [:div (horizontal-separator.attributes/separator-attributes separator-id separator-props)
        [:div (horizontal-separator.attributes/separator-body-attributes separator-id separator-props)
              [:hr {:class :pe-horizontal-separator--line :data-border-color color}]
              (if label [:span {:class :pe-horizontal-separator--label}
                               (metamorphic-content/compose label)])
              [:hr {:class :pe-horizontal-separator--line :data-border-color color}]]])

(defn element
  ; @param (keyword)(opt) separator-id
  ; @param (map) separator-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :width (keyword)(opt)
  ;   :auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :auto}
  ;
  ; @usage
  ; [horizontal-separator {...}]
  ;
  ; @usage
  ; [horizontal-separator :my-horizontal-separator {...}]
  ([separator-props]
   [element (random/generate-keyword) separator-props])

  ([separator-id separator-props]
   (fn [_ separator-props] ; XXX#0106 (tutorials.api#parametering)
       (let [separator-props (pretty-presets/apply-preset                               separator-props)
             separator-props (horizontal-separator.prototypes/separator-props-prototype separator-props)]
            [horizontal-separator separator-id separator-props]))))
