
(ns pretty-website.language-selector.prototypes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; @ignore
  ;
  ; @param (map) selector-props
  ;
  ; @return (map)
  ; {:font-size (keyword, px or string)}
  ; {:gap (keyword, px or string)}
  [selector-props]
  (merge {:font-size :s
          :gap       :xxs}
         (-> selector-props)))
