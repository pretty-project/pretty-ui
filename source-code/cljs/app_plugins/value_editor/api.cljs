
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.17
; Description:
; Version: v0.2.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.api
    (:require [app-plugins.value-editor.engine :as engine]
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



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  (ns my-namespace (:require [app-plugins.value-editor.api :as value-editor]))
;
;  (defn my-subscription
;   [db _]
;   {:my-value (r value-editor/get-editor-value db :my-editor)})
;  (a/reg-sub ::my-subscription my-subscription)
;
; @usage
;  (a/dispatch [:value-editor/load! ...])



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.value-editor.api
(def get-editor-value engine/get-editor-value)
