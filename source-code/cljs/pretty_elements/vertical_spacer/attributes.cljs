
(ns pretty-elements.vertical-spacer.attributes
    (:require [pretty-css.api :as pretty-css]
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
      (pretty-css.layout/block-size-attributes spacer-props)
      (pretty-css/class-attributes      spacer-props)
      (pretty-css/state-attributes      spacer-props)
      (pretty-css/style-attributes      spacer-props)))
