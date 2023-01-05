
(ns components.surface-description.views
    (:require [components.surface-description.helpers    :as surface-description.helpers]
              [components.surface-description.prototypes :as surface-description.prototypes]
              [elements.api                              :as elements]
              [random.api                                :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-description-body
  ; @param (keyword) description-id
  ; @param (map) description-props
  ; {:content (metamorphic-content)
  ;  :font-size (keyword)
  ;  :horizontal-align (keyword)}
  [description-id {:keys [content font-size horizontal-align] :as description-props}]
  [:div.c-surface-description-body (surface-description.helpers/description-body-attributes description-id description-props)
                                   [elements/label ::surface-description
                                                   {:color            :muted
                                                    :content          content
                                                    :font-size        font-size
                                                    :line-height      :block
                                                    :horizontal-align horizontal-align}]])

(defn- surface-description
  ; @param (keyword) description-id
  ; @param (map) description-props
  [description-id description-props]
  [:div.c-surface-description (surface-description.helpers/description-attributes description-id description-props)
                              [surface-description-body                           description-id description-props]])

(defn component
  ; @param (keyword)(opt) description-id
  ; @param (map) description-props
  ; {:content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword)(opt)
  ;   Default: :xxs
  ;  :horizontal-align (keyword)(opt)
  ;   :left, :center, :right
  ;   Default: :center
  ;  :indent (map)(opt)
  ;   Default: {:horizontal :xxs}
  ;  :outdent (map)(opt)}
  ;
  ; @usage
  ; [surface-description {...}]
  ;
  ; @usage
  ; [surface-description :my-surface-description {...}]
  ([description-props]
   [component (random/generate-keyword) description-props])

  ([description-id description-props]
   (let [description-props (surface-description.prototypes/description-props-prototype description-props)]
        [surface-description description-id description-props])))
