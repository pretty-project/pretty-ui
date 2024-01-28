
(ns pretty-elements.blank.attributes
    (:require [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.layout.api :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-body-attributes
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ blank-props]
  (-> {:class :pe-blank--body}
      (pretty-css.basic/style-attributes   blank-props)
      (pretty-css.layout/indent-attributes blank-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-attributes
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ blank-props]
  (-> {:class :pe-blank}
      (pretty-css.appearance/theme-attributes blank-props)
      (pretty-css.basic/class-attributes      blank-props)
      (pretty-css.basic/state-attributes      blank-props)
      (pretty-css.layout/outdent-attributes   blank-props)))
