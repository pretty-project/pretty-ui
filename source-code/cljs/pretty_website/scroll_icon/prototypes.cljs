
(ns pretty-website.scroll-icon.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-props-prototype
  ; @ignore
  ;
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {:color (keyword)}
  [icon-props]
  (merge {:color "#FFFFFF"}
         (-> icon-props)))
