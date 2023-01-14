
(ns elements.horizontal-separator.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:height (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-block-height (keyword)
  ;  :style (map)}
  [_ {:keys [height style] :as separator-props}]
  (-> {:data-block-height height
       :style             style}
      (pretty-css/default-attributes separator-props)))
