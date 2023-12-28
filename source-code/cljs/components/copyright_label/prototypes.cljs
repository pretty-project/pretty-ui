
(ns components.copyright-label.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:font-size (keyword, px or string)}
  [label-props]
  (merge {:font-size :xxs}
         (-> label-props)))
