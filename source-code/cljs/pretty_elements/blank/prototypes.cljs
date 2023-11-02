
(ns pretty-elements.blank.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-props-prototype
  ; @ignore
  ;
  ; @param (map) blank-props
  ;
  ; @return (map)
  ; {:width (keyword)}
  [blank-props]
  (merge {:width :content}
         (-> blank-props)))
