
(ns pretty-inputs.core.views
    (:require [pretty-inputs.core.env          :as core.env]
              [pretty-inputs.core.side-effects :as core.side-effects]
              [state-synchronizer.api          :as state-synchronizer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-elements.element.views/*)
(def input-label pretty-elements.element.views/element-label)

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
                                       ;:get-monitor-value-f #(core.env/get-input-internal-value           :a {})
                                       :get  (fn [])
                                       ;:get-trigger-value-f #(core.env/get-input-external-value           input-id input-props)
                                       ;:set-primary-state-f #(core.side-effects/set-input-internal-value! input-id input-props %)
                                       :debug? true}])
