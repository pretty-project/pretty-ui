
(ns pretty-elements.horizontal-spacer.attributes
    (:require [pretty-css.api :as pretty-css]))

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
  [_ spacer-props]
  (-> {:class :pe-horizontal-spacer}
      (pretty-css/block-size-attributes spacer-props)
      (pretty-css/class-attributes      spacer-props)
      (pretty-css/state-attributes      spacer-props)
      (pretty-css/style-attributes      spacer-props)))
