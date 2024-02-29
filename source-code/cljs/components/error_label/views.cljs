
(ns components.error-label.views
    (:require [components.error-label.prototypes :as error-label.prototypes]
              [fruits.random.api                 :as random]
              [pretty-elements.api               :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @note
  ; For more information, check out the documentation of the ['label'](/pretty-ui/cljs/pretty-elements/api.html#label) element.
  ;
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; {:color (keyword or string)(opt)
  ;   Default: :warning
  ;  :font-size (keyword, px or string)(opt)
  ;   Default: :xs}
  ;
  ; @usage
  ; [error-label {...}]
  ;
  ; @usage
  ; [error-label :my-error-label {...}]
  ([label-props]
   [view (random/generate-keyword) label-props])

  ([label-id label-props]
   ; @note (tutorials#parameterizing)
   (fn [_ label-props]
       (let [label-props (error-label.prototypes/label-props-prototype label-props)]))))
            ;[pretty-elements/label label-id label-props]))))
