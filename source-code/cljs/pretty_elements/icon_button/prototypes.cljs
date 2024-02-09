
(ns pretty-elements.icon-button.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [react-references.api :as react-references]))

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
  (let [set-reference-f (react-references/set-reference-f button-id)]
       (-> button-props (pretty-properties/clickable-text-auto-props)
                        (pretty-properties/inherit-icon-props)
                        (pretty-properties/default-anchor-props       {})
                        (pretty-properties/default-border-props       {})
                        (pretty-properties/default-effect-props       {})
                        (pretty-properties/default-flex-props         {:orientation :vertical})
                        (pretty-properties/default-font-props         {:font-size :micro :font-weight :medium})
                        (pretty-properties/default-icon-props         {:icon-size :m})
                        (pretty-properties/default-label-props        {})
                        (pretty-properties/default-mouse-event-props  {})
                        (pretty-properties/default-progress-props     {})
                        (pretty-properties/default-react-props        {:set-reference-f set-reference-f})
                        (pretty-properties/default-size-props         {:size-unit :full-block})
                        (pretty-properties/default-text-props         {:text-selectable? false})
                        (pretty-properties/default-wrapper-size-props {}))))
