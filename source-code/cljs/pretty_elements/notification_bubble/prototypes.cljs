
(ns pretty-elements.notification-bubble.prototypes
    (:require [react-references.api :as react-references]
              [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-props-prototype
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  [bubble-id bubble-props]
  (let [set-reference-f (react-references/set-reference-f bubble-id)]
       (-> bubble-props (pretty-properties/inherit-icon-props)
                        (pretty-properties/default-anchor-props       {})
                        (pretty-properties/default-border-props       {})
                        (pretty-properties/default-content-props      {})
                        (pretty-properties/default-effect-props       {})
                        (pretty-properties/default-flex-props         {:orientation :horizontal :horizontal-align :left :gap :auto})
                        (pretty-properties/default-font-props         {:font-size :s :font-weight :medium})
                        (pretty-properties/default-icon-props         {})
                        (pretty-properties/default-mouse-event-props  {})
                        (pretty-properties/default-progress-props     {})
                        (pretty-properties/default-react-props        {:set-reference-f set-reference-f})
                        (pretty-properties/default-size-props         {:size-unit :double-block})
                        (pretty-properties/default-text-props         {:text-overflow :wrap :text-selectable? true})
                        (pretty-properties/default-wrapper-size-props {}))))
