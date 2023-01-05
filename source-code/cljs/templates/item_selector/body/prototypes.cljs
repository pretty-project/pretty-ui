
(ns templates.item-selector.body.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; @param (map) selector-props
  ;
  ; @return (map)
  ; {:export-item-f (function)
  ;  :import-count-f (function)
  ;  :import-id-f (function)}
  [selector-props]
  (merge {:export-item-f  (fn [item-id item item-count] item-id)
          :import-id-f    (fn [{:keys [id]}] id)
          :import-count-f (fn [{:keys [count]}] count)}
         (param selector-props)))

(defn body-props-prototype
  ; @param (keyword) selector-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ; {:default-order-by (namespaced keyword)}
  [_ body-props]
  (merge {:default-order-by :modified-at/descending}
         (param body-props)))
