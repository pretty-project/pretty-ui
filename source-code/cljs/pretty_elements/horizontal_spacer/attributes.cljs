
(ns pretty-elements.horizontal-spacer.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn spacer-body-attributes
  ; @ignore
  ;
  ; @param (keyword) spacer-id
  ; @param (map) spacer-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ spacer-props]
  (-> {:class :pe-horizontal-spacer--body}
      (pretty-attributes/body-size-attributes spacer-props)
      (pretty-attributes/indent-attributes    spacer-props)
      (pretty-attributes/style-attributes     spacer-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn spacer-attributes
  ; @ignore
  ;
  ; @param (keyword) spacer-id
  ; @param (map) spacer-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ spacer-props]
  (-> {:class :pe-horizontal-spacer}
      (pretty-attributes/class-attributes   spacer-props)
      (pretty-attributes/outdent-attributes spacer-props)
      (pretty-attributes/size-attributes    spacer-props)
      (pretty-attributes/state-attributes   spacer-props)
      (pretty-attributes/theme-attributes   spacer-props)))
