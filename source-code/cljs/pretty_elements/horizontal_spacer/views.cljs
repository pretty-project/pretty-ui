
(ns pretty-elements.horizontal-spacer.views
    (:require [fruits.random.api                            :as random]
              [pretty-elements.horizontal-spacer.attributes :as horizontal-spacer.attributes]
              [pretty-elements.horizontal-spacer.prototypes :as horizontal-spacer.prototypes]
              [pretty-presets.api                           :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) spacer-id
  ; @param (map) spacer-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :s
  ;  :preset (keyword)(opt)
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
   (fn [_ spacer-props] ; XXX#0106 (tutorials.api#parametering)
       (let [spacer-props (pretty-presets/apply-preset                         spacer-props)
             spacer-props (horizontal-spacer.prototypes/spacer-props-prototype spacer-props)]
            [:div (horizontal-spacer.attributes/spacer-attributes spacer-id spacer-props)]))))
