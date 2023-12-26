
(ns pretty-elements.vertical-spacer.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn spacer-props-prototype
  ; @ignore
  ;
  ; @param (map) spacer-props
  ;
  ; @return (map)
  ; {:height (keyword, px or string)
  ;  :width (keyword, px or string)}
  [spacer-props]
  (merge {:height :parent
          :width  :s}
         (-> spacer-props)))
