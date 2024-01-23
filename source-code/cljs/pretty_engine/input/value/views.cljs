
(ns pretty-engine.input.value.views
    (:require [pretty-engine.input.value.env          :as input.value.env]
              [pretty-engine.input.value.side-effects :as input.value.side-effects]
              [state-synchronizer.api :as state-synchronizer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-synchronizer
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id input-props]
  [state-synchronizer/sensor input-id {:autoclear? true
                                       :get-monitor-value-f #(input.value.env/get-input-internal-value           input-id input-props)
                                       :get-trigger-value-f #(input.value.env/get-input-external-value           input-id input-props)
                                       :set-primary-state-f #(input.value.side-effects/set-input-internal-value! input-id input-props %)
                                       :debug? false}])
