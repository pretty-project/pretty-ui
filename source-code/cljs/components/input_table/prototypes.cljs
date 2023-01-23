
(ns components.input-table.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-props-prototype
  ; @param (map) table-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [border-color] :as table-props}]
  (merge (if border-color {:border-position :all
                           :border-width    :xxs})
         (param table-props)))
