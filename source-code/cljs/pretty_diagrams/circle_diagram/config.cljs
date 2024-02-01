
(ns pretty-diagrams.circle-diagram.config)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @description
; Applied angle correction to make the first datum starts at the top center position of the circle.
;
; @constant (°)
(def ANGLE-CORRECTION -90)

; @ignore
;
; @description
; Virtual size of the circle within the SVG object.
; Scaled up/down to match the actual size of the diagram.
;
; @constant (px)
(def CIRCLE-DIAMETER 200)
