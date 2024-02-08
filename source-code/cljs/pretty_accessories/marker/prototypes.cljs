
(ns pretty-accessories.marker.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn marker-props-prototype
  ; @ignore
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ;
  ; @return (map)
  [_ marker-props]
  (-> marker-props (pretty-properties/default-background-color-props {:fill-color :default})
                   (pretty-properties/default-border-props           {})
                   (pretty-properties/default-position-props         {:position :tr :position-method :absolute})
                   (pretty-properties/default-size-props             {:height :xxs :width :xxs})))
