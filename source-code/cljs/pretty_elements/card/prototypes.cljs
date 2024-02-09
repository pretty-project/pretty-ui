
(ns pretty-elements.card.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [react-references.api :as react-references]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-props-prototype
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  [card-id card-props]
  (let [set-reference-f (react-references/set-reference-f card-id)]
       (-> card-props (pretty-properties/default-anchor-props      {})
                      (pretty-properties/default-border-props      {})
                      (pretty-properties/default-mouse-event-props {})
                      (pretty-properties/default-content-props     {})
                      (pretty-properties/default-effect-props      {})
                      (pretty-properties/default-flex-props        {:orientation :vertical :vertical-align :top})
                      (pretty-properties/default-react-props       {:set-reference-f set-reference-f}))))
