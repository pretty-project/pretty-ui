
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
              [server-fruits.io  :as io]
              [x.mid-core.build-handler    :as build-handler]
              [x.server-core.event-handler :as event-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def APP-BUILD-FILEPATH "monoset-environment/x.app-build.edn")

; @constant (string)
(def INITIAL-APP-BUILD "0.0.1")



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.build-handler
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



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-sub :core/get-app-build get-app-build)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-db :core/store-app-build! store-app-build!)



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-app-build!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [{:keys [app-build]} (io/read-edn-file APP-BUILD-FILEPATH)]
       (event-handler/dispatch [:core/store-app-build! app-build])))

(event-handler/reg-fx :core/import-app-build! import-app-build!)
