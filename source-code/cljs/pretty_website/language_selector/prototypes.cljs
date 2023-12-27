
(ns pretty-website.language-selector.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; @ignore
  ;
  ; @param (map) selector-props
  ;
  ; @return (map)
  ; {:font-size (keyword)}
  ; {:gap (keyword)}
  [selector-props]
  (merge {:font-size :s
          :gap       :xxs}
         (-> selector-props)))
