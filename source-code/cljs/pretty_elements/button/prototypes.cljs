
(ns pretty-elements.button.prototypes
    (:require [pretty-elements.core.props :as core.props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  [button-id button-props]
  (-> button-props (core.props/badge-props       {})
                   (core.props/border-props      {})
                   (core.props/clickable-props   {})
                   (core.props/effect-props      {})
                   (core.props/focus-props       {:focus-id button-id})
                   (core.props/font-props        {})
                   (core.props/label-icon-props  {})
                   (core.props/label-props       {})
                   (core.props/marker-props      {})
                   (core.props/mouse-event-props {})
                   (core.props/progress-props    {})
                   (core.props/row-props         {})
                   (core.props/text-props        {})
                   (core.props/tooltip-props     {})))
