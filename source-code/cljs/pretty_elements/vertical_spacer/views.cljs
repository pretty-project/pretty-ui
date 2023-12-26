
(ns pretty-elements.vertical-spacer.views
    (:require [fruits.random.api                          :as random]
              [pretty-elements.vertical-spacer.attributes :as vertical-spacer.attributes]
              [pretty-elements.vertical-spacer.prototypes :as vertical-spacer.prototypes]
              [pretty-presets.api                         :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) spacer-id
  ; @param (map) spacer-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :height (keyword, px or string)(opt)
  ;   Default: :parent
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :width (keyword, px or string)(opt)
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
   ; @note (tutorials#parametering)
   (fn [_ spacer-props]
       (let [spacer-props (pretty-presets/apply-preset                       spacer-props)
             spacer-props (vertical-spacer.prototypes/spacer-props-prototype spacer-props)]
            [:div (vertical-spacer.attributes/spacer-attributes spacer-id spacer-props)]))))
