
(ns templates.item-lister.body.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ; {:default-order-by (namespaced keyword)}
  [_ body-props]
  (merge {:default-order-by :modified-at/descending}
         (param body-props)))
