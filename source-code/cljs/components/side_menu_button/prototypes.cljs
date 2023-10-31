
(ns components.side-menu-button.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [button-props]
  (merge {:font-size        :xs
          :gap              :xs
          :horizontal-align :left
          :hover-color      :highlight
          :icon-size        :m
          :indent           {:horizontal :xs :left :s :right :xl}}
         (-> button-props)))
