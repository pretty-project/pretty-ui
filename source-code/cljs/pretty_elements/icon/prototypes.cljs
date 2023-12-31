
(ns pretty-elements.icon.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-props-prototype
  ; @ignore
  ;
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {:icon-color (keyword or string)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword, px or string)}
  [icon-props]
  (merge {:icon-color  :default
          :icon-family :material-symbols-outlined
          :icon-size   :m}
         (-> icon-props)))
