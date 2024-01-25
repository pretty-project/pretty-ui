
(ns pretty-elements.adornment-group.prototypes
    (:require [pretty-elements.core.props :as core.props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  [_ group-props]
  (-> group-props (core.props/size-props {:height :content :width :content})
                  (core.props/flex-props {:orientation :horizontal :overflow :scroll})))
