
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.lifecycle-handler.side-effects
    (:require [x.mid-core.lifecycle-handler.side-effects :as lifecycle-handler.side-effects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.lifecycle-handler.side-effects
(def reg-lifecycles!    lifecycle-handler.side-effects/reg-lifecycles!)
(def import-lifecycles! lifecycle-handler.side-effects/import-lifecycles!)
