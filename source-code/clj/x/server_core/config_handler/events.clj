
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.config-handler.events
    (:require [x.mid-core.config-handler.events :as config-handler.events]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.config-handler.events
(def store-app-config!     config-handler.events/store-app-config!)
(def store-server-config!  config-handler.events/store-server-config!)
(def store-website-config! config-handler.events/store-website-config!)
