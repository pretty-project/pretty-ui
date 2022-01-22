
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.21
; Description:
; Version: v0.3.6
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

; @constant (string)
(def INITIAL-APP-BUILD "0.0.1")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ->app-built
  ; @usage
  ;  (core/->app-built)
  []
  (letfn [(f [{:keys [app-build]}] (if app-build {:app-build (try (format/inc-version app-build)
                                                                  (catch Exception e (println e)))}
                                                 {:app-build INITIAL-APP-BUILD}))]
         (io/swap-edn-file! APP-BUILD-FILEPATH f)))
