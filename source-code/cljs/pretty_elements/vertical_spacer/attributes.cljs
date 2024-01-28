
(ns pretty-elements.vertical-spacer.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.layout.api :as pretty-css.layout]
              [pretty-css.basic.api :as pretty-css.basic]))

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
      (pretty-css.layout/block-size-attributes spacer-props)
      (pretty-css.basic/class-attributes      spacer-props)
      (pretty-css.basic/state-attributes      spacer-props)
      (pretty-css.basic/style-attributes      spacer-props)))
