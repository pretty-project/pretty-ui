
(ns elements.horizontal-separator.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-attributes
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as separator-props}]
  (-> {:class :e-horizontal-separator
       :style style}
      (pretty-css/default-attributes    separator-props)
      (pretty-css/block-size-attributes separator-props)))
