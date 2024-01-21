
(ns components.menu-table.helpers
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-body-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  [_ table-props]
  (-> {}
      (pretty-build-kit/indent-attributes table-props)
      (pretty-build-kit/style-attributes  table-props)))

(defn table-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  [_ table-props]
  (-> {} (pretty-build-kit/class-attributes   table-props)
         (pretty-build-kit/outdent-attributes table-props)
         (pretty-build-kit/state-attributes   table-props)))
