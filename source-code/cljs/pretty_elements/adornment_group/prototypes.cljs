
(ns pretty-elements.adornment-group.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-props-prototype
  ; @ignore
  ;
  ; @param (integer) adornment-dex
  ; @param (map) adornment-props
  ;
  ; @return (map)
  [_ adornment-props]
  (-> adornment-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  [_ group-props]
  (-> group-props (pretty-properties/default-flex-props {:orientation :horizontal :overflow :scroll})
                  (pretty-properties/default-size-props {:size-unit :full-block})
                  (pretty-standards/standard-flex-props)
                  (pretty-standards/standard-wrapper-size-props)
                  (pretty-rules/auto-adapt-wrapper-size)
                  (pretty-rules/auto-align-scrollable-flex)))
