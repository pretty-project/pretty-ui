
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.load-handler.lifecycles
    (:require [x.core.lifecycle-handler.side-effects :as lifecycle-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(lifecycle-handler.side-effects/reg-lifecycles! ::lifecycles
  {:on-app-init [:x.core/initialize-load-handler!]})
