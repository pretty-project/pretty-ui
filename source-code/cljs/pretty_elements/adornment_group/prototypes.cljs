
(ns pretty-elements.adornment-group.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]
              [metamorphic-content.api :as metamorphic-content]
              [countdown-timer.api :as countdown-timer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-props-prototype
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ; {:icon (keyword)(opt)
  ;  :on-click-f (function)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:click-effect (keyword)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword)
  ;  :on-click-f (function)
  ;  :text-color (keyword or string)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)}
  [adornment-id {:keys [icon on-click-f timeout tooltip-content] :as adornment-props}]
  (merge {:text-color :default}
         (if icon            {:icon-family    :material-symbols-outlined
                              :icon-size      :s})
         (if on-click-f      {:click-effect   :opacity})
         (if tooltip-content {:tooltip-position :left})
         (-> adornment-props)
         (if tooltip-content {:tooltip-content (metamorphic-content/compose tooltip-content)})
         (if timeout {:on-click-f #(countdown-timer/start-countdown! adornment-id {:step 1000 :timeout timeout :on-start-f on-click-f})})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (map) group-props
  ;
  ; @return (map)
  [_])
