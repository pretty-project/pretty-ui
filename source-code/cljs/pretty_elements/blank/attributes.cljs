
(ns pretty-elements.blank.attributes
    (:require [pretty-css.api :as pretty-css]
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
      (pretty-css.layout/indent-attributes blank-props)
      (pretty-css/style-attributes  blank-props)))

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
      (pretty-css/class-attributes   blank-props)
      (pretty-css.layout/outdent-attributes blank-props)
      (pretty-css/state-attributes   blank-props)
      (pretty-css/theme-attributes   blank-props)))
