
(ns pretty-inputs.multi-combo-box.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.layout.api     :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-body-attributes
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ box-props]
  (-> {:class :pi-multi-combo-box--body}
      (pretty-css.layout/indent-attributes box-props)
      (pretty-css.basic/style-attributes  box-props)))

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
      (pretty-css.basic/class-attributes   box-props)
      (pretty-css.layout/outdent-attributes box-props)
      (pretty-css.basic/state-attributes   box-props)
      (pretty-css.appearance/theme-attributes   box-props)))
