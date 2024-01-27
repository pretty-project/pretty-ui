
(ns pretty-inputs.multi-combo-box.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.layout.api :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-body-attributes
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ box-props]
  (-> {:class :pi-multi-combo-box--body}
      (pretty-css.layout/indent-attributes box-props)
      (pretty-css/style-attributes  box-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-attributes
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {}
  [_ box-props]
  (-> {:class :pi-multi-combo-box}
      (pretty-css/class-attributes   box-props)
      (pretty-css.layout/outdent-attributes box-props)
      (pretty-css/state-attributes   box-props)
      (pretty-css/theme-attributes   box-props)))
