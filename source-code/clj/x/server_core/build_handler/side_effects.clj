
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.build-handler.side-effects
    (:require [mid-fruits.format                     :as format]
              [server-fruits.io                      :as io]
              [x.mid-core.build-handler.side-effects :as build-handler.side-effects]
              [x.server-core.build-handler.engine    :as build-handler.engine]
              [x.server-core.event-handler           :as event-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.build-handler.side-effects
(def app-build build-handler.side-effects/app-build)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ->app-built
  ; @usage
  ;  (core/->app-built)
  []
  (letfn [(f [{:keys [app-build]}] (if app-build {:app-build (format/inc-version app-build)}
                                                 {:app-build build-handler.engine/INITIAL-APP-BUILD}))]
         (io/swap-edn-file! build-handler.engine/APP-BUILD-FILEPATH f)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-app-build!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (if-let [{:keys [app-build]} (io/read-edn-file build-handler.engine/APP-BUILD-FILEPATH)]
          (event-handler/dispatch [:core/store-app-build! app-build])
          ; Development környezetben nem szükséges az (a/->app-built) függvényt meghívni,
          ; ezért nem minden esetben biztosított az app-build értékének létezése!
          (event-handler/dispatch [:core/store-app-build! build-handler.engine/INITIAL-APP-BUILD])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-fx :core/import-app-build! import-app-build!)
