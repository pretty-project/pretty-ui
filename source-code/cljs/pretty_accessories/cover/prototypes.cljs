
(ns pretty-accessories.cover.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cover-props-prototype
  ; @ignore
  ;
  ; @param (keyword) cover-id
  ; @param (map) cover-props
  ;
  ; @return (map)
  [_ cover-props]
  (-> cover-props (pretty-properties/default-background-color-props {:fill-color :default})
                  (pretty-properties/default-flex-props             {:orientation :horizontal})
                  (pretty-properties/default-font-props             {:font-size :s :font-weight :medium})
                  (pretty-properties/default-icon-props             {})
                  (pretty-properties/default-label-props            {})
                  (pretty-properties/default-position-props         {:position :tl :position-method :absolute})
                  (pretty-properties/default-size-props             {:height :parent :width :parent})
                  (pretty-properties/default-text-props             {:text-selectable? false})
                  (pretty-properties/default-visibility-props       {:opacity :hard})))
