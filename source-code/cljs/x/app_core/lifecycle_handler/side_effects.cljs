
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.lifecycle-handler.side-effects
    (:require [mid.x.core.lifecycle-handler.side-effects :as lifecycle-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.core.lifecycle-handler.side-effects
(def reg-lifecycles!    lifecycle-handler.side-effects/reg-lifecycles!)
(def import-lifecycles! lifecycle-handler.side-effects/import-lifecycles!)
