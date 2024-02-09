
(ns pretty-elements.vertical-separator.prototypes
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
  (-> separator-props (pretty-properties/default-line-props         {:line-orientation :vertical})
                      (pretty-properties/default-font-props         {:font-size :micro :font-weight :medium})
                      (pretty-properties/default-flex-props         {:gap :xs :orientation :vertical})
                      (pretty-properties/default-label-props        {})
                      (pretty-properties/default-size-props         {:height :parent :width :content :size-unit :full-block})
                      (pretty-properties/default-text-props         {:text-transform :uppercase :text-selectable? false})
                      (pretty-properties/default-wrapper-size-props {})))
