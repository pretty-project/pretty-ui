
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.21
; Description:
; Version: v0.6.0
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.build-handler
    (:require [mid-fruits.format :as format]
              [server-fruits.io  :as io]
              [x.mid-core.build-handler       :as build-handler]
              [x.server-core.event-handler    :as event-handler]
              [x.server-core.transfer-handler :as transfer-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
;  A "resources" mappában elhelyezett "x.app-build.edn" fájl minden esetben
;  a lefordított JAR fájl része!
(def APP-BUILD-FILEPATH "resources/x.app-build.edn")

; @constant (string)
(def INITIAL-APP-BUILD "0.0.1")



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.build-handler
(def app-build        build-handler/app-build)
(def get-app-build    build-handler/get-app-build)
(def store-app-build! build-handler/store-app-build!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ->app-built
  ; @usage
  ;  (core/->app-built)
  []
  (letfn [(f [{:keys [app-build]}] (if app-build {:app-build (format/inc-version app-build)}
                                                 {:app-build INITIAL-APP-BUILD}))]
         (io/swap-edn-file! APP-BUILD-FILEPATH f)))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-app-build!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (if-let [{:keys [app-build]} (io/read-edn-file APP-BUILD-FILEPATH)]
          (event-handler/dispatch [:core/store-app-build! app-build])
          ; Development környezetben nem szükséges az (a/->app-built) függvényt meghívni,
          ; ezért nem minden esetben biztosított az app-build értékének létezése!
          (event-handler/dispatch [:core/store-app-build! INITIAL-APP-BUILD])))

(event-handler/reg-fx_ :core/import-app-build! import-app-build!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(transfer-handler/reg-transfer! :core/transfer-app-build!
                                {:data-f     #(event-handler/subscribed [:core/get-app-build])
                                 :target-path [:core/build-handler :meta-items :app-build]})
