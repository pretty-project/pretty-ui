
(ns pretty-elements.vertical-spacer.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn spacer-attributes
  ; @ignore
  ;
  ; @param (keyword) spacer-id
  ; @param (map) spacer-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ spacer-props]
  (-> {:class :pe-vertical-spacer}
      (pretty-build-kit/block-size-attributes spacer-props)
      (pretty-build-kit/class-attributes      spacer-props)
      (pretty-build-kit/state-attributes      spacer-props)
      (pretty-build-kit/style-attributes      spacer-props)))
