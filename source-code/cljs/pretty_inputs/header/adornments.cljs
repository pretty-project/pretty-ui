
(ns pretty-inputs.header.adornments
    (:require [pretty-inputs.engine.api :as pretty-inputs.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-info-text-adornment
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  (let [on-click-f #(pretty-inputs.engine/toggle-input-info-text-visibility! header-id header-props)]
       (if (-> header-props :header :info-text)
           {:icon {:icon-name :info :icon-color :muted}
            :on-click-f on-click-f})))
