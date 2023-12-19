
(ns pretty-diagrams.api
    (:require [pretty-diagrams.circle-diagram.views :as circle-diagram.views]
              [pretty-diagrams.line-diagram.views   :as line-diagram.views]
              [pretty-diagrams.point-diagram.views  :as point-diagram.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @tutorial First steps
; @redirect (tutorials/first-steps)

; @tutorial Introduction
; @redirect (tutorials/introduction)

; @tutorial Parametering
; @redirect (tutorials/parametering)

; @tutorial Presets
; @redirect (tutorials/presets)

; @tutorial Event handlers
; @redirect (tutorials/event-handlers)

; @tutorial Content types
; @redirect (tutorials/content-types)

; @tutorial Value paths of inputs
; @redirect (tutorials/value-paths-of-inputs)

; @tutorial Option paths of optionable inputs
; @redirect (tutorials/option-paths-of-optionable-inputs)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/diagram)
(def line-diagram   line-diagram.views/diagram)
(def point-diagram  point-diagram.views/diagram)
(def circle-diagram circle-diagram.views/diagram)
