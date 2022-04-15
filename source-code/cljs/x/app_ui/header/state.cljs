
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.header.state
    (:require [reagent.api :refer [ratom]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (metamorphic-content)
;
; A HEADER-TITLE értéke a Re-Frame adatbázis helyett atom típusban van tárolva,
; így az értékének változtatása kevesebb erőforrást igényel.
(defonce HEADER-TITLE (ratom nil))
