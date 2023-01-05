
(ns elements.text-field.config)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
; Az utolsó karakter leütése után mennyi idő elteltével számít befejezettnek a gépelés
;                 ... 250) <- Nem mindenki programozó! :)
;                 ... 350) <- De ennyire? :)
(def TYPE-ENDED-AFTER 450)

; @constant (px)
; XXX#0789 (source-code/cljs/elements/text_field/helpers.cljs)
(def LINE-HEIGHT-XS 21)

; @constant (px)
; XXX#0789 (source-code/cljs/elements/text_field/helpers.cljs)
(def LINE-HEIGHT-S 24)

; @constant (px)
(def FIELD-HORIZONTAL-PADDING 4)

; @constant (px)
(def FIELD-BORDER-WIDTH 1)
