
(ns pretty-elements.vertical-spacer.views
    (:require [pretty-elements.vertical-spacer.attributes :as vertical-spacer.attributes]
              [pretty-elements.vertical-spacer.prototypes :as vertical-spacer.prototypes]
              [pretty-presets.api                  :as pretty-presets]
              [random.api                          :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) spacer-id
  ; @param (map) spacer-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :preset (keyword)(opt)
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
   (fn [_ spacer-props] ; XXX#0106 (README.md#parametering)
       (let [spacer-props (pretty-presets/apply-preset                       spacer-props)
             spacer-props (vertical-spacer.prototypes/spacer-props-prototype spacer-props)]
            [:div (vertical-spacer.attributes/spacer-attributes spacer-id spacer-props)]))))
