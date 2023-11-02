
(ns pretty-presets.api
    (:require [pretty-presets.default-presets.presets]
              [pretty-presets.preset-pool.env          :as preset-pool.env]
              [pretty-presets.preset-pool.side-effects :as preset-pool.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; pretty-presets.preset-pool.env
(def apply-preset preset-pool.env/apply-preset)

; pretty-presets.preset-pool.side-effects
(def reg-preset! preset-pool.side-effects/reg-preset!)
