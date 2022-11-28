
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.text-field.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
;  Az utolsó karakter leütése után mennyi idő elteltével számít befejezettnek a gépelés
(def TYPE-ENDED-AFTER 450)
;                 ... 350) <- De ennyire? :)
;                 ... 250) <- Nem mindenki programozó! :)

; @constant (px)
(def LINE-HEIGHT-XS 21)

; @constant (px)
(def LINE-HEIGHT-S 24)

; @constant (px)
(def FIELD-HORIZONTAL-PADDING 4)

; @constant (px)
(def FIELD-BORDER-WIDTH 1)
