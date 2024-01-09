
(ns pretty-inputs.core.views
    (:require [state-synchronizer.api :as state-synchronizer]
              [pretty-inputs.core.env :as core.env]
              [pretty-inputs.core.side-effects :as core.side-effects]))

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
                                       :get-monitor-value-f    #(core.env/get-input-internal-value           input-id input-props)
                                       :get-trigger-value-f    #(core.env/get-input-external-value           input-id input-props)
                                       :update-primary-state-f #(core.side-effects/set-input-internal-value! input-id input-props %)}])
