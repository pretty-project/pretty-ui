
(ns pretty-elements.adornment.prototypes
    (:require [pretty-elements.core.props :as core.props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-props-prototype
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  [adornment-id adornment-props]
  (-> adornment-props (core.props/clickable-props   {})
                      (core.props/effect-props      {})
                      (core.props/focus-props       {:focus-id adornment-id})
                      (core.props/font-props        {:font-size :xxs})
                      (core.props/icon-props        {})
                      (core.props/label-props       {})
                      (core.props/mouse-event-props {})
                      (core.props/progress-props    {})
                      (core.props/size-props        {:min-width :m})
                      (core.props/tooltip-props     {})))
