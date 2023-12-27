
(ns pretty-elements.vertical-polarity.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-props-prototype
  ; @ignore
  ;
  ; @param (map) polarity-props
  ;
  ; @return (map)
  ; {:height (keyword, px or string)
  ;  :horizontal-align (keyword)
  ;  :width (keyword, px or string)}
  [polarity-props]
  (merge {:height           :parent
          :horizontal-align :center
          :width :auto}
         (-> polarity-props)))
