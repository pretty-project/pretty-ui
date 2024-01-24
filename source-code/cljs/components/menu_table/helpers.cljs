
(ns components.menu-table.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-body-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  [_ table-props]
  (-> {}
      (pretty-css/indent-attributes table-props)
      (pretty-css/style-attributes  table-props)))

(defn table-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  [_ table-props]
  (-> {} (pretty-css/class-attributes   table-props)
         (pretty-css/outdent-attributes table-props)
         (pretty-css/state-attributes   table-props)))
