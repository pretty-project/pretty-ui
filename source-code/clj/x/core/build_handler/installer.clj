
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.build-handler.installer
    (:require [plugins.git.api                     :as git]
              [x.core.build-handler.config         :as build-handler.config]
              [x.core.install-handler.side-effects :as install-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- installer
  []
  (git/ignore! build-handler.config/BUILD-VERSION-FILEPATH "x.core"))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(install-handler.side-effects/reg-installer! ::installer installer)
