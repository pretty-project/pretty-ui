
(ns components.menu-table.helpers
    (:require [pretty-attributes.api  :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-inner-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  [_ table-props]
  (-> {}
      (pretty-attributes/indent-attributes table-props)
      (pretty-attributes/style-attributes  table-props)))

(defn table-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  [_ table-props]
  (-> {} (pretty-attributes/class-attributes  table-props)
         (pretty-attributes/outdent-attributes table-props)
         (pretty-attributes/state-attributes  table-props)))
