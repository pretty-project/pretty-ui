
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.21
; Description:
; Version: v0.2.0
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.build-handler
    (:require [mid-fruits.format :as format]
              [server-fruits.io  :as io]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def APP-BUILD-FILEPATH "monoset-environment/x.app-build.edn")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ->app-built
  ; @usage
  ;  (core/->app-built)
  []
  (io/swap-edn-file! APP-BUILD-FILEPATH #(update % :app-build format/inc-version)))
