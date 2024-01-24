
(ns pretty-defaults.default-props)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
; {:element-type (keyword, px or string)}
(def DEFAULT-HEIGHT
     {:horizontal-strectched :xxx})

; @constant (map)
; {:element-type (keyword, px or string)}
(def DEFAULT-WIDTH
     {:horizontal-strectched :xxx})

; @constant (map)
; {:element-type (map)
;  {:height (keyword, px or string)
;   :width (keyword, px or string)}}
(def DEFAULT-DIMENSIONS
     {:horizontal-strectched {:height (:horizontal-strectched DEFAULT-HEIGHT)
                              :width  (:horizontal-strectched DEFAULT-HEIGHT)}})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------
