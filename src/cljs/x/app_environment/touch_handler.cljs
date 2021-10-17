
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.2.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.touch-handler
    (:require [app-fruits.dom   :as dom]
              [mid-fruits.candy :refer [param]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-db.api     :as db]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn touch-events-api-detected?
  ; @return (boolean)
  [db _]
  (get-in db (db/meta-item-path ::primary :touch-events-api.detected?)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::detect-touch-events-api!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (if (dom/touch-events-api-detected?)
      [::->touch-events-api-detected]
      [::->touch-events-api-not-detected]))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::->touch-events-api-detected
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n
   [[:x.app-environment.element-handler/set-attribute!
     "x-body-container" "data-touch-detected" true]
    [:x.app-db/set-item! (db/meta-item-path ::primary :touch-events-api.detected?)
                         (param true)]]})

(a/reg-event-fx
  ::->touch-events-api-not-detected
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n
   [[:x.app-environment.element-handler/set-attribute!
     "x-body-container" "data-touch-detected" false]
    [:x.app-db/set-item! (db/meta-item-path ::primary :touch-events-api.detected?)
                         (param false)]]})



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [::detect-touch-events-api!]})
