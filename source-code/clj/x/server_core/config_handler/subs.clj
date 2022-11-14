
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.config-handler.subs
    (:require [mid.x.core.config-handler.subs :as config-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.core.config-handler.subs
(def get-app-config         config-handler.subs/get-app-config)
(def get-app-config-item    config-handler.subs/get-app-config-item)
(def get-server-config      config-handler.subs/get-server-config)
(def get-server-config-item config-handler.subs/get-server-config-item)
