
(ns pretty-presets.api
    (:require [pretty-presets.default-presets.presets]
              [pretty-presets.engine.api :as preset-presets.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (preset-presets.engine.api/*)
(def reg-preset! preset-presets.engine/reg-preset!)
