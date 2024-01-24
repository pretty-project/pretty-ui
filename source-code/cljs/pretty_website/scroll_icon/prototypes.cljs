
(ns pretty-website.scroll-icon.prototypes
    (:require [pretty-css.api :as pretty-css]))

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
