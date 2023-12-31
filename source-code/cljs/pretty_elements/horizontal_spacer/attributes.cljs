
(ns pretty-elements.horizontal-spacer.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
  (-> {:class :pe-horizontal-spacer
       :style style}
      (pretty-build-kit/block-size-attributes spacer-props)
      (pretty-build-kit/class-attributes      spacer-props)
      (pretty-build-kit/state-attributes      spacer-props)))
