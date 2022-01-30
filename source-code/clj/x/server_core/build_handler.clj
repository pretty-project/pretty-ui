
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
              [x.mid-core.build-handler        :as build-handler]
              [x.server-core.event-handler     :as event-handler]
              [x.server-core.lifecycle-handler :as lifecycle-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def APP-BUILD-FILEPATH "monoset-environment/x.app-build.edn")

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
  (let [{:keys [app-build]} (io/read-edn-file APP-BUILD-FILEPATH)]
       (event-handler/dispatch [:core/store-app-build! app-build])))

(event-handler/reg-fx :core/import-app-build! import-app-build!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- transfer-app-build
  ; @param (map) request
  ;
  ; @return (string)
  [_]
  (event-handler/subscribed [:core/get-app-build]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(lifecycle-handler/reg-lifecycles!
  ::lifecycles
  {:on-server-init [:core/reg-transfer! :core/transfer-app-build!
                                        {:data-f      transfer-app-build
                                         :target-path [:core/build-handler :meta-items :app-build]}]})
