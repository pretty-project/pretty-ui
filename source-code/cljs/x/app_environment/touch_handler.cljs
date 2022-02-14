
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.4.8
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.touch-handler
    (:require [app-fruits.dom   :as dom]
              [mid-fruits.candy :refer [param]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-db.api     :as db]
              [x.app-environment.element-handler :as element-handler]))



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
       (element-handler/set-element-attribute! "x-body-container" "data-touch-detected"                   %)
       (a/dispatch [:db/set-item! (db/meta-item-path :environment/touch-data :touch-events-api.detected?) %])))

(a/reg-fx_ :environment/detect-touch-events-api! detect-touch-events-api!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-init {:environment/detect-touch-events-api! nil}})
