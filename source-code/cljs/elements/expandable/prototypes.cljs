
(ns elements.expandable.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-props-prototype
  ; @ignore
  ;
  ; @param (map) expandable-props
  ; {:icon (keyword)}
  ;
  ; @return (map)
  ; {:expanded? (boolean)
  ;  :icon-family (keyword)}
  [{:keys [icon] :as expandable-props}]
  (merge {:expanded? false}
         (if icon {:icon-family :material-symbols-outlined})
         (param expandable-props)))
