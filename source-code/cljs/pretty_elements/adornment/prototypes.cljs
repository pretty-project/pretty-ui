
(ns pretty-elements.adornment.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [react-references.api :as react-references]))

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
  (let [set-reference-f (react-references/set-reference-f adornment-id)]
       (-> adornment-props (pretty-properties/clickable-text-auto-props)
                           (pretty-properties/default-anchor-props       {})
                           (pretty-properties/default-border-props       {})
                           (pretty-properties/default-effect-props       {})
                           (pretty-properties/default-flex-props         {:orientation :horizontal})
                           (pretty-properties/default-font-props         {:font-size :xxs :font-weight :medium})
                           (pretty-properties/default-icon-props         {})
                           (pretty-properties/default-label-props        {})
                           (pretty-properties/default-mouse-event-props  {})
                           (pretty-properties/default-progress-props     {})
                           (pretty-properties/default-react-props        {:set-reference-f set-reference-f})
                           (pretty-properties/default-size-props         {:min-width :xs :size-unit :half-block})
                           (pretty-properties/default-text-props         {:text-selectable? false})
                           (pretty-properties/default-wrapper-size-props {}))))
