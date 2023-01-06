
(ns components.error-label.views
    (:require [components.error-label.prototypes :as error-label.prototypes]
              [elements.api                      :as elements]
              [random.api                        :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :indent (map)(opt)
  ;  :outdent (map)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [error-label {...}]
  ;
  ; @usage
  ; [error-label :my-error-label {...}]
  ([label-props]
   [component (random/generate-keyword) label-props])

  ([label-id label-props]
   (let [label-props (error-label.prototypes/label-props-prototype label-props)]
        [elements/label label-id label-props])))
