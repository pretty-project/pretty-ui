
(ns templates.item-selector.header.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn control-bar-props-prototype
  ; @param (keyword) selector-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  ; {:order-by-options (namespaced keywords in vector)
  ;  :search-keys (keywords in vector)}
  [_ body-props]
  (merge {:order-by-options [:modified-at/ascending :modified-at/descending :name/ascending :name/descending]
          :search-keys      [:name]}
         (param body-props)))
