
(ns pretty-inputs.color-selector.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; @ignore
  ;
  ; @param (map) selector-props
  ; {:popup (map)(opt)}
  ;
  ; @return (map)
  ; {:popup (map)
  ;   {:cover-color (keyword or string)
  ;    :fill-color (keyword or string)}}
  [{:keys [popup] :as selector-props}]
  (merge {}
         (-> selector-props)
         {:popup (merge {:cover-color :black :fill-color :default} popup)}))
