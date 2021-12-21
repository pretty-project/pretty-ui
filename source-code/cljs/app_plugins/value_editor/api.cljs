
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.17
; Description:
; Version: v0.3.0
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.api
    (:require [app-plugins.value-editor.engine]
              [app-plugins.value-editor.events]
              [app-plugins.value-editor.subs :as subs]
              [app-plugins.value-editor.views]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name {:edit-original? true}
;  A {:value-path ...} tulajdonságként átadott Re-Frame adatbázis útvonalon tárolt
;  érték szerkesztése közben, az aktuális érték az útvonalra íródik.
;
; @name {:on-save ...}
;  A mentés gomb megnyomásakor megtörténő esemény, amely használatával
;  egyedi mentési eljárás is megvalósítható.



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.value-editor.subs
(def get-editor-value subs/get-editor-value)
