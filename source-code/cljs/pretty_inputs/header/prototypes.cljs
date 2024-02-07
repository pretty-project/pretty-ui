
(ns pretty-inputs.header.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

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
  (-> header-props (pretty-properties/default-border-props      {})
                   (pretty-properties/default-label-props       {})
                   (pretty-properties/default-input-guide-props {})
                   (pretty-properties/default-size-props        {:height :content :width :content})))
