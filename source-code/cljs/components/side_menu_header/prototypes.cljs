
(ns components.side-menu-header.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-prototype
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {}
  [{:keys [] :as header-props}]
  (merge {}
         (param header-props)))
