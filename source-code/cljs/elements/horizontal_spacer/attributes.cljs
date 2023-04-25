
(ns elements.horizontal-spacer.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn spacer-attributes
  ; @ignore
  ;
  ; @param (keyword) spacer-id
  ; @param (map) spacer-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as spacer-props}]
  (-> {:class :e-horizontal-spacer
       :style style}
      (pretty-css/default-attributes    spacer-props)
      (pretty-css/block-size-attributes spacer-props)))
