
(ns pretty-elements.ghost.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-props-prototype
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ;
  ; @return (map)
  [_ ghost-props]
  (-> ghost-props (pretty-properties/default-animation-props        {:animation-duration 2000 :animation-mode :repeat :animation-name :pulse})
                  (pretty-properties/default-background-color-props {:fill-color :highlight})
                  (pretty-properties/default-border-props           {})
                  (pretty-properties/default-size-props             {:height :s :width :s})))
