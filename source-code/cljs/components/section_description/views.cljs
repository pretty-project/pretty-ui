
(ns components.section-description.views
    (:require [components.section-description.prototypes :as section-description.prototypes]
              [elements.api                              :as elements]
              [random.api                                :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; XXX#0439 (source-code/cljs/elements/label/views.cljs)
  ; The section-description component is based on the label element.
  ; Check out the documentation of the label element for more information.
  ;
  ; @param (keyword)(opt) description-id
  ; @param (map) description-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;   :left, :center, :right
  ;   Default: :center
  ;  :indent (map)(opt)
  ;   Default: {:horizontal :xs :vertical :xs}
  ;  :outdent (map)(opt)
  ;  :style (map)(opt)}
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
