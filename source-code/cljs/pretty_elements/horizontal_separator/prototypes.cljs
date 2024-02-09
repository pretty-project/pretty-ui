
(ns pretty-elements.horizontal-separator.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

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
  (-> separator-props (pretty-properties/default-line-props         {:line-orientation :horizontal})
                      (pretty-properties/default-font-props         {:font-size :micro :font-weight :medium})
                      (pretty-properties/default-flex-props         {:gap :xs :orientation :horizontal})
                      (pretty-properties/default-label-props        {})
                      (pretty-properties/default-size-props         {:height :content :width :auto :size-unit :full-block})
                      (pretty-properties/default-text-props         {:text-transform :uppercase :text-selectable? false})
                      (pretty-properties/default-wrapper-size-props {})))
