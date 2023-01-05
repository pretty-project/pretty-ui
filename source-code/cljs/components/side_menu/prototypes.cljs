
(ns components.side-menu.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-props-prototype
  ; @param (map) menu-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [] :as menu-props}]
  (merge {}
         (param menu-props)))
