
(ns elements.vertical-line.views
    (:require [elements.vertical-line.attributes :as vertical-line.attributes]
              [elements.vertical-line.prototypes :as vertical-line.prototypes]
              [random.api                        :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) line-id
  ; @param (map) line-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :highlight
  ;  :height (keyword)(opt)
  ;   :auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :parent
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :style (map)(opt)
  ;  :strength (px)(opt)
  ;   Default: 1}
  ;
  ; @usage
  ; [vertical-line {...}]
  ;
  ; @usage
  ; [vertical-line :my-vertical-line {...}]
  ([line-props]
   [element (random/generate-keyword) line-props])

  ([line-id line-props]
   (let [line-props (vertical-line.prototypes/line-props-prototype line-props)]
        [:div (vertical-line.attributes/line-attributes line-id line-props)
              [:div (vertical-line.attributes/line-body-attributes line-id line-props)]])))
