
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.hiccuptopdf.api
    (:require [tools.hiccuptopdf.config       :as config]
              [tools.hiccuptopdf.side-effects :as side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; tools.hiccuptopdf.config
(def GENERATOR-FILEPATH config/GENERATOR-FILEPATH)
(def BASE64-FILEPATH    config/BASE64-FILEPATH)

; tools.hiccuptopdf.side-effects
(def generate-pdf!        side-effects/generate-pdf!)
(def generate-base64-pdf! side-effects/generate-base64-pdf!)
