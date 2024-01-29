
(ns pretty-elements.ghost.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-props-prototype
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ;
  ; @return (map)
  ; {:fill-color (keyword or string)
  ;  :height (keyword, px or string)
  ;  :width (keyword, px or string)}
  [_ ghost-props]
  (merge {

          :animation-duration 2000
          :animation-name     :opacity-1-05-1



          :fill-color :highlight
          :height     :s
          :width      :s}
         (-> ghost-props)))
