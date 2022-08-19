
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.resource-handler.lifecycles
    (:require [x.server-core.lifecycle-handler.side-effects :as lifecycle-handler.side-effects]
              [x.server-core.resource-handler.config        :as resource-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(lifecycle-handler.side-effects/reg-lifecycles!
  ::lifecycles
  {:on-server-init [:core/store-resource-handler-options! resource-handler.config/DEFAULT-OPTIONS]})
