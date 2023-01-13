
(ns elements.vertical-separator.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:style (map)(opt)
  ;  :width (keyword)}
  ;
  ; @return (map)
  ; {:data-block-width (keyword)
  ;  :style (map)}
  [_ {:keys [style width] :as separator-props}]
  (merge (pretty-css/default-attributes separator-props)
         {:data-block-width width
          :style            style}))
