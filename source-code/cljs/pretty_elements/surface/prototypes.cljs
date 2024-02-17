
(ns pretty-elements.surface.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

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
  (-> surface-props (pretty-properties/default-content-props      {})
                    (pretty-properties/default-size-props         {:size-unit :double-block})
                    (pretty-properties/default-state-props        {:mounted? true})
                    (pretty-standards/standard-animation-props)
                    (pretty-standards/standard-border-props)
                    (pretty-standards/standard-wrapper-size-props)
                    ;(pretty-rules/auto-disable-highlight-color)
                    ;(pretty-rules/auto-disable-hover-color)
                    (pretty-rules/apply-auto-border-crop)
                    (pretty-rules/compose-content)
                    (pretty-rules/auto-adapt-wrapper-size)))
