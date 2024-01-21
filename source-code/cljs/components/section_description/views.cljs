
(ns components.section-description.views
    (:require [components.section-description.prototypes :as section-description.prototypes]
              [fruits.random.api                         :as random]
              [pretty-elements.api                       :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @note
  ; For more information, check out the documentation of the ['label'](/pretty-ui/cljs/pretty-elements/api.html#label) element.
  ;
  ; @param (keyword)(opt) description-id
  ; @param (map) description-props
  ; {:color (keyword or string)(opt)
  ;   Default: :muted
  ;  :font-size (keyword, px or string)(opt)
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
   ; @note (tutorials#parametering)
   (fn [_ description-props]
       (let [description-props (section-description.prototypes/description-props-prototype description-props)]
            [pretty-elements/label description-id description-props]))))
