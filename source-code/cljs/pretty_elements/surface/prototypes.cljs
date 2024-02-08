
(ns pretty-elements.surface.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-props-prototype
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  [_ surface-props]
  (-> surface-props (pretty-properties/default-animation-props {})
                    (pretty-properties/default-border-props    {})
                    (pretty-properties/default-content-props   {})
                    (pretty-properties/default-state-props     {:mounted? true})))
