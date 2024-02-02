
(ns pretty-inputs.header.prototypes
    (:require [pretty-inputs.properties.api :as pretty-inputs.properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-prototype
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [_ header-props]
  (-> header-props (pretty-inputs.properties/default-border-props {})
                   (pretty-inputs.properties/default-label-props  {})
                   (pretty-inputs.properties/default-guides-props {})
                   (pretty-inputs.properties/default-size-props   {:height :content :width :content})))
