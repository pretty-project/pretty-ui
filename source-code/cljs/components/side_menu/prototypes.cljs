
(ns components.side-menu.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-props-prototype
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {}
  [menu-props]
  (merge {:position :left}
         (param menu-props)))
