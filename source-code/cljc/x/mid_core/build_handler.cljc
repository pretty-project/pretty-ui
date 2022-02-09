
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.22
; Description:
; Version: v0.3.0
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.build-handler
    (:require [x.mid-core.event-handler :as event-handler]))



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

(defn app-build
  ; @example
  ;  (a/app-build)
  ;  =>
  ;  "0.4.2"
  ;
  ; @return (string)
  []
 @(event-handler/subscribe [:core/get-app-build]))



;; -- Subscriptions ----------------------------------------------------------
;; ---------------------------------------------------------------------------

(defn get-app-build
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (get-in db [:core/build-handler :meta-items :app-build]))

(event-handler/reg-sub :core/get-app-build get-app-build)



;; -- DB events --------------------------------------------------------------
;; ---------------------------------------------------------------------------

(defn store-app-build!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) app-build
  ;
  ; @return (string)
  [db [_ app-build]]
  (assoc-in db [:core/build-handler :meta-items :app-build] app-build))

(event-handler/reg-event-db :core/store-app-build! store-app-build!)
