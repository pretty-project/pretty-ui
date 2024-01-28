
(ns components.menu-table.helpers
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.layout.api :as pretty-css.layout]
              [pretty-css.basic.api :as pretty-css.basic]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-body-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  [_ table-props]
  (-> {}
      (pretty-css.layout/indent-attributes table-props)
      (pretty-css.basic/style-attributes  table-props)))

(defn table-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  [_ table-props]
  (-> {} (pretty-css.basic/class-attributes   table-props)
         (pretty-css.layout/outdent-attributes table-props)
         (pretty-css.basic/state-attributes   table-props)))
