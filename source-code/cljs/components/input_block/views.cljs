
(ns components.input-block.views
    (:require [components.input-block.helpers    :as input-block.helpers]
              [components.input-block.prototypes :as input-block.prototypes]
              [random.api                        :as random]
              [x.components.api                  :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- input-block
  ; @param (keyword) block-id
  ; @param (map) block-props
  ; {:input (metamorphic-content)
  ;  :label (metamorphic-content)}
  [block-id {:keys [input label] :as block-props}]
  [:<> [:label.c-input-block--label (input-block.helpers/input-label-attributes block-id block-props)
                                    [x.components/content block-id label]]
       [:div.c-input-block--input   [x.components/content block-id input]]])

(defn component
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
   [component (random/generate-keyword) block-props])

  ([block-id block-props]
   (let [] ; block-props (input-block.prototypes/block-props-prototype block-props)
        [input-block block-id block-props])))
