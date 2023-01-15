
(ns components.section-description.views
    (:require [components.section-description.prototypes :as section-description.prototypes]
              [elements.api                              :as elements]
              [random.api                                :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; XXX#0439 (source-code/cljs/elements/label/views.cljs)
  ; The section-description component is based on the label element.
  ; For more information check out the documentation of the label element.
  ;
  ; @param (keyword)(opt) description-id
  ; @param (map) description-props
  ; {:color (keyword or string)(opt)
  ;   Default: :muted
  ;  :font-size (keyword)(opt)
  ;   Default: :xxs
  ;  :horizontal-align (keyword)(opt)
  ;   Default: :center
  ;  :indent (map)(opt)
  ;   Default: {:horizontal :xs :vertical :xs}}
  ;
  ; @usage
  ; [section-description {...}]
  ;
  ; @usage
  ; [section-description :my-section-description {...}]
  ([description-props]
   [component (random/generate-keyword) description-props])

  ([description-id description-props]
   (let [description-props (section-description.prototypes/description-props-prototype description-props)]
        [elements/label description-id description-props])))
