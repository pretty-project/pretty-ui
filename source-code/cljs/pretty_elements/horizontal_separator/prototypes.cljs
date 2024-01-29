
(ns pretty-elements.horizontal-separator.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-props-prototype
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:color (keyword)
  ;  :width (keyword, px or string)}
  [_ separator-props]
  (merge {:color :default
          :width :auto}
         (-> separator-props)))
