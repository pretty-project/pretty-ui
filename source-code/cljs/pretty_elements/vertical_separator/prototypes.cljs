
(ns pretty-elements.vertical-separator.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-props-prototype
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  [_ separator-props]
  (-> separator-props (pretty-properties/default-font-props {:font-size :micro :font-weight :medium})
                      (pretty-properties/default-flex-props {:gap :xs :orientation :vertical})
                      (pretty-properties/default-line-props {:line-color :muted :line-orientation :vertical :line-size :grow})
                      (pretty-properties/default-size-props {:height :parent :width :content :size-unit :full-block})
                      (pretty-properties/default-text-props {:text-color :muted :text-transform :uppercase :text-selectable? false})
                      (pretty-standards/standard-body-size-props)
                      (pretty-standards/standard-flex-props)
                      (pretty-standards/standard-font-props)
                      (pretty-standards/standard-line-props)
                      (pretty-standards/standard-size-props)
                      (pretty-standards/standard-text-props)
                      (pretty-rules/auto-align-scrollable-flex)
                      (pretty-rules/compose-label)))
