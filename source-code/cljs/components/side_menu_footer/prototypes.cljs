
(ns components.side-menu-footer.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @param (map) footer-props
  ;
  ; @return (map)
  ; {}
  [{:keys [] :as footer-props}]
  (merge {}
         (param footer-props)))
