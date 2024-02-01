
(ns components.input-block.views
    (:require [components.input-block.helpers    :as input-block.helpers]
              [components.input-block.prototypes :as input-block.prototypes]
              [fruits.random.api                 :as random]
              [metamorphic-content.api           :as metamorphic-content]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- input-block
  ; @param (keyword) block-id
  ; @param (map) block-props
  ; {:input (metamorphic-content)
  ;  :label (metamorphic-content)}
  [block-id {:keys [input label] :as block-props}]
  [:<> [:label.c-input-block--label (input-block.helpers/input-label-attributes block-id block-props)
                                    [metamorphic-content/compose label]]
       [:div.c-input-block--input   [metamorphic-content/compose input]]])

(defn view
  ; @description
  ; When displaying a 'text-field' element in an 'input-block' by using the
  ; same ID for both the field and the block, the label of the block can targets the field.
  ;
  ; @param (keyword)(opt) block-id
  ; @param (map) block-props
  ; {:input (metamorphic-content)
  ;  :label (metamorphic-content)}
  ;
  ; @usage
  ; [input-block {...}]
  ;
  ; @usage
  ; [input-block :my-input-block {...}]
  ([block-props]
   [view (random/generate-keyword) block-props])

  ([block-id block-props]
   ; @note (tutorials#parameterizing)
   (fn [_ block-props]
       (let [] ; block-props (input-block.prototypes/block-props-prototype block-props)
            [input-block block-id block-props]))))
