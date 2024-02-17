
(ns pretty-inputs.header.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]))

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
  (-> header-props (pretty-properties/default-label-props       {})
                   (pretty-properties/default-input-guide-props {})
                   (pretty-properties/default-size-props        {:height :content :width :content})
                   (pretty-standards/standard-border-props)
                   (pretty-rules/compose-input-guides)
                   (pretty-rules/compose-label)))
