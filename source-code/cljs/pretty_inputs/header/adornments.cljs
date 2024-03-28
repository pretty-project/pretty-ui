
(ns pretty-inputs.header.adornments
    (:require [component-props.api :as component-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-info-text-adornment
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:info-text (map)(opt)
  ;  ...}
  ;
  ; @return (map)
  ; {:icon (map)
  ;  :on-click-f (function)}
  [id {:keys [info-text]}]
  (let [on-click-f (fn [_] (component-props/update-props! id update :info-text-visible? not))]
       (if info-text {:icon {:icon-name :info :icon-color :muted} :on-click-f on-click-f})))
