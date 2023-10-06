
(ns elements.vertical-spacer.views
    (:require [elements.vertical-spacer.attributes :as vertical-spacer.attributes]
              [elements.vertical-spacer.prototypes :as vertical-spacer.prototypes]
              [random.api                          :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) spacer-id
  ; @param (map) spacer-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :style (map)(opt)
  ;  :width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :s}
  ;
  ; @usage
  ; [vertical-spacer {...}]
  ;
  ; @usage
  ; [vertical-spacer :my-vertical-spacer {...}]
  ([spacer-props]
   [element (random/generate-keyword) spacer-props])

  ([spacer-id spacer-props]
   (let [spacer-props (vertical-spacer.prototypes/spacer-props-prototype spacer-props)]
        [:div (vertical-spacer.attributes/spacer-attributes spacer-id spacer-props)])))