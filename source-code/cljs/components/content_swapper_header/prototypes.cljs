
(ns components.content-swapper-header.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-prototype
  ; @ignore
  ;
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {}
  [header-props]
  (merge {:border-color     :highlight
          :border-position  :bottom
          :font-size        :xs
          :gap              :xs
          :horizontal-align :left
          :hover-color      :highlight
          :icon             :chevron_left
          :icon-position    :left
          :icon-size        :xl
          :indent           {:horizontal :xs}
          :width            :auto}
         (-> header-props)))
