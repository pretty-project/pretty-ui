
(ns pretty-inputs.core.views
    (:require [pretty-inputs.core.env          :as core.env]
              [pretty-inputs.core.side-effects :as core.side-effects]
              [state-synchronizer.api          :as state-synchronizer]
              [pretty-elements.api :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-label
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:helper (metamorphic-content)(opt)
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword or string)(opt)}
  [input-id {:keys [helper info-text label marker-color]}]
  (if label [pretty-elements/label {:content      label
                                    :helper       helper
                                    :info-text    info-text
                                    :marker-color marker-color
                                    :focus-id     input-id}]))

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
                                       :get-monitor-value-f #(core.env/get-input-internal-value           input-id input-props)
                                       :get-trigger-value-f #(core.env/get-input-external-value           input-id input-props)
                                       :set-primary-state-f #(core.side-effects/set-input-internal-value! input-id input-props %)
                                       :debug? false}])
