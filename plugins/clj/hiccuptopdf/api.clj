
(ns hiccuptopdf.api
    (:require [hiccuptopdf.config       :as config]
              [hiccuptopdf.side-effects :as side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; hiccuptopdf.config
(def GENERATOR-FILEPATH config/GENERATOR-FILEPATH)
(def BASE64-FILEPATH    config/BASE64-FILEPATH)

; hiccuptopdf.side-effects
(def generate-pdf!        side-effects/generate-pdf!)
(def generate-base64-pdf! side-effects/generate-base64-pdf!)
