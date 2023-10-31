
(ns elements.horizontal-spacer.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn spacer-props-prototype
  ; @ignore
  ;
  ; @param (map) spacer-props
  ;
  ; @return (map)
  ; {:height (keyword)}
  [spacer-props]
  (merge {:height :s}
         (-> spacer-props)))
