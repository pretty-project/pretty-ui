
(ns elements.horizontal-spacer.views
    (:require [elements.horizontal-spacer.attributes :as horizontal-spacer.attributes]
              [elements.horizontal-spacer.prototypes :as horizontal-spacer.prototypes]
              [random.api                            :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) spacer-id
  ; @param (map) spacer-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :s
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [horizontal-spacer {...}]
  ;
  ; @usage
  ; [horizontal-spacer :my-horizontal-spacer {...}]
  ([spacer-props]
   [element (random/generate-keyword) spacer-props])

  ([spacer-id spacer-props]
   (let [spacer-props (horizontal-spacer.prototypes/spacer-props-prototype spacer-props)]
        [:div (horizontal-spacer.attributes/spacer-attributes spacer-id spacer-props)])))
