
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.pdf-generator.api
    (:require [tools.pdf-generator.config       :as config]
              [tools.pdf-generator.side-effects :as side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; tools.pdf-generator.config
(def GENERATOR-FILEPATH config/GENERATOR-FILEPATH)
(def BASE64-FILEPATH    config/BASE64-FILEPATH)

; tools.pdf-generator.side-effects
(def generate-pdf!       side-effects/generate-pdf!)
(def generate-base64-pdf! side-effects/generate-base64-pdf!)
