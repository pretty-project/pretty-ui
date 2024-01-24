
(ns pretty-elements.ghost.prototypes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-props-prototype
  ; @ignore
  ;
  ; @param (map) ghost-props
  ;
  ; @return (map)
  ; {:height (keyword, px or string)
  ;  :width (keyword, px or string)}
  [ghost-props]
  (merge {:height :s
          :width  :s}
         (-> ghost-props)))
