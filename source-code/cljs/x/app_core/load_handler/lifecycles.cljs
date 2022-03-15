
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.load-handler.lifecycles
    (:require [x.app-core.lifecycle-handler.side-effects :as lifecycle-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(lifecycle-handler.side-effects/reg-lifecycles!
  ::lifecycles
  {:on-app-init [:core/initialize-load-handler!]})
