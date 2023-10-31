
(ns components.list-item-button.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [button-props]
  (merge {:fill-color  :highlight
          :font-size   :xs
          :hover-color :highlight}
         (-> button-props)))
