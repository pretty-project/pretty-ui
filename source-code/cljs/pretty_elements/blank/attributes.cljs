
(ns pretty-elements.blank.attributes
    (:require [pretty-css.api :as pretty-css]))

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
      (pretty-css/indent-attributes blank-props)
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
      (pretty-css/outdent-attributes blank-props)
      (pretty-css/state-attributes   blank-props)))
