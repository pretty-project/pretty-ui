
(ns elements.vertical-spacer.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn spacer-props-prototype
  ; @ignore
  ;
  ; @param (map) spacer-props
  ;
  ; @return (map)
  ; {:width (keyword)}
  [spacer-props]
  (merge {:width :s}
         (-> spacer-props)))
