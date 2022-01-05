
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.08
; Description:
; Version: v0.2.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.resource-handler
    (:require [x.server-core.event-handler     :as event-handler]
              [x.server-core.lifecycle-handler :as lifecycle-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
;  https://github.com/metosin/reitit/blob/master/doc/ring/static.md
;  {:path (string)
;   :root (string)}
(def DEFAULT-OPTIONS {:path "/" :root "/public"})



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-resource-handler-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db [::options :data-items]))

(event-handler/reg-sub :core/get-resource-handler-options get-resource-handler-options)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-resource-handler-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) resource-handler-options
  ;
  ; @return (map)
  [db [_ resource-handler-options]]
  (assoc-in db [::options :data-items] resource-handler-options))

(event-handler/reg-event-db :core/store-resource-handler-options! store-resource-handler-options!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(lifecycle-handler/reg-lifecycles
  ::lifecycles
  {:on-server-init [:core/store-resource-handler-options! DEFAULT-OPTIONS]})
