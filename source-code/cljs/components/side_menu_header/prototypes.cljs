
(ns components.side-menu-header.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn refer-props-prototype
  ; @param (map) refer-props
  ;
  ; @return (map)
  ; {}
  [{:keys [] :as refer-props}]
  (merge {}
         (param refer-props)))
