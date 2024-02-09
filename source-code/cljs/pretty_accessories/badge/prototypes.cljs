
(ns pretty-accessories.badge.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn badge-props-prototype
  ; @ignore
  ;
  ; @param (keyword) badge-id
  ; @param (map) badge-props
  ;
  ; @return (map)
  [_ badge-props]
  (-> badge-props (pretty-properties/default-background-color-props {:fill-color :default})
                  (pretty-properties/default-border-props           {})
                  (pretty-properties/default-font-props             {:font-size :micro :font-weight :medium})
                  (pretty-properties/default-icon-props             {})
                  (pretty-properties/default-label-props            {})
                  (pretty-properties/default-position-props         {:position :br :position-method :absolute})
                  (pretty-properties/default-size-props             {:size-unit :quarter-block})
                  (pretty-properties/default-text-props             {:text-selectable? false})
                  (pretty-properties/default-wrapper-size-props     {})))
