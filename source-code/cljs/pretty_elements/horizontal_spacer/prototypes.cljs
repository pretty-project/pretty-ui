
(ns pretty-elements.horizontal-spacer.prototypes
    (:require [pretty-css.api :as pretty-css]))

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
  (merge {:height :s
          :width  :auto}
         (-> spacer-props)))
