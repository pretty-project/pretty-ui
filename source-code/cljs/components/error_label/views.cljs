
(ns components.error-label.views
    (:require [components.error-label.prototypes :as error-label.prototypes]
              [pretty-elements.api                      :as pretty-elements]
              [random.api                        :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @info
  ; XXX#0721 (source-code/cljs/pretty_elements/label/views.cljs)
  ; The 'error-label' component is based on the 'label' element.
  ; For more information check out the documentation of the 'label' element.
  ;
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; {:color (keyword or string)(opt)
  ;   Default: :warning
  ;  :font-size (keyword)(opt)
  ;   Default: :xs}
  ;
  ; @usage
  ; [error-label {...}]
  ;
  ; @usage
  ; [error-label :my-error-label {...}]
  ([label-props]
   [component (random/generate-keyword) label-props])

  ([label-id label-props]
   (fn [_ label-props] ; XXX#0106 (README.md#parametering)
       (let [label-props (error-label.prototypes/label-props-prototype label-props)]
            [pretty-elements/label label-id label-props]))))
