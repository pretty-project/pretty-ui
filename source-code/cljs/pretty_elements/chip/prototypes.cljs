
(ns pretty-elements.chip.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [react-references.api :as react-references]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-props-prototype
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  [chip-id chip-props]
  (let [set-reference-f (react-references/set-reference-f chip-id)]
       (-> chip-props ;(pretty-properties/clickable-text-auto-props) 
                      (pretty-properties/default-anchor-props           {})
                      (pretty-properties/default-background-color-props {:fill-color :primary})
                      (pretty-properties/default-border-props           {})
                      (pretty-properties/default-effect-props           {})
                      (pretty-properties/default-flex-props             {:orientation :horizontal})
                      (pretty-properties/default-font-props             {:font-size :xxs :font-weight :medium})
                      (pretty-properties/default-label-props            {})
                      (pretty-properties/default-mouse-event-props      {})
                      (pretty-properties/default-react-props            {:set-reference-f set-reference-f})
                      (pretty-properties/default-size-props             {:size-unit :full-block})
                      (pretty-properties/default-text-props             {:text-overflow :ellipsis :text-selectable? false})
                      (pretty-properties/default-wrapper-size-props     {}))))
