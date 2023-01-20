
(ns elements.horizontal-separator.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-attributes
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [style] :as separator-props}]
  (-> {:style style}
      (pretty-css/block-size-attributes separator-props)
      (pretty-css/default-attributes    separator-props)))
