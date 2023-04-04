
(ns elements.vertical-separator.views
    (:require [elements.vertical-separator.attributes :as vertical-separator.attributes]
              [elements.vertical-separator.prototypes :as vertical-separator.prototypes]
              [random.api                             :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) separator-id
  ; @param (map) separator-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :style (map)(opt)
  ;  :width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :s}
  ;
  ; @usage
  ; [vertical-separator {...}]
  ;
  ; @usage
  ; [vertical-separator :my-vertical-separator {...}]
  ([separator-props]
   [element (random/generate-keyword) separator-props])

  ([separator-id separator-props]
   (let [separator-props (vertical-separator.prototypes/separator-props-prototype separator-props)]
        [:div (vertical-separator.attributes/separator-attributes separator-id separator-props)])))
