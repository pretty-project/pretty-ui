
(ns pretty-elements.vertical-spacer.attributes
    (:require [pretty-css.basic.api  :as pretty-css.basic]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css.basic/class-attributes      spacer-props)
      (pretty-css.basic/state-attributes      spacer-props)
      (pretty-css.basic/style-attributes      spacer-props)
      (pretty-css.layout/quarter-block-size-attributes spacer-props)))
