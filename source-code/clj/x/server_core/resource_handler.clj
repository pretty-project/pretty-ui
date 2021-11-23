
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.08
; Description:
; Version: v0.1.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.resource-handler
    (:require [mid-fruits.candy                :refer [param]]
              [x.server-core.event-handler     :as event-handler]
              [x.server-core.lifecycle-handler :as lifecycle-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
; https://github.com/metosin/reitit/blob/master/doc/ring/static.md
(def DEFAULT-OPTIONS
     {:path "/" :root "/public"})



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-resource-handler-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db [::options :data-items]))

(event-handler/reg-sub :x.server-core/get-resource-handler-options get-resource-handler-options)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-resource-handler-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) resource-handler-options
  ;
  ; @return (map)
  [db [_ resource-handler-options]]
  (assoc-in db [::options :data-items]
               (param resource-handler-options)))

(event-handler/reg-event-db :x.server-core/store-resource-handler-options! store-resource-handler-options!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(lifecycle-handler/reg-lifecycles
  ::lifecycles
  {:on-app-init [:x.server-core/store-resource-handler-options! DEFAULT-OPTIONS]})
