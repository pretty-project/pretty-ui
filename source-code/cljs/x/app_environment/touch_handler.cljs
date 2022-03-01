
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.5.4
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.touch-handler
    (:require [app-fruits.dom   :as dom]
              [mid-fruits.candy :refer [param]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-db.api     :as db]
              [x.app-environment.element-handler.side-effects :as element-handler.side-effects]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn touch-events-api-detected?
  ; @usage
  ;  (r environment/touch-events-api-detected? db)
  ;
  ; @return (boolean)
  [db _]
  (get-in db (db/meta-item-path :environment/touch-data :touch-events-api.detected?)))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- detect-touch-events-api!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [% (dom/touch-events-api-detected?)]
       (element-handler.side-effects/set-element-attribute! "x-body-container" "data-touch-detected"                   %)
       (a/dispatch [:db/set-item! (db/meta-item-path :environment/touch-data :touch-events-api.detected?) %])))

(a/reg-fx :environment/detect-touch-events-api! detect-touch-events-api!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-init {:fx [:environment/detect-touch-events-api!]}})
