
(ns pretty-elements.blank.prototypes
    (:require [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-props-prototype
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;
  ; @return (map)
  [_ blank-props]
  (-> blank-props (pretty-rules/compose-content)
                  (pretty-standards/standard-body-size-props)
                  (pretty-standards/standard-size-props)))
