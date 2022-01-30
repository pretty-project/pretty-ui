
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.4.4
; Compatibility: x4.5.8



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
  ; @usage
  ;  (r environment/touch-events-api-detected? db)
  ;
  ; @return (boolean)
  [db _]
  (get-in db (db/meta-item-path :environment/touch-data :touch-events-api.detected?)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/detect-touch-events-api!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if (dom/touch-events-api-detected?)
          {:db (assoc-in db (db/meta-item-path :environment/touch-data :touch-events-api.detected?) true)
           :environment/set-element-attribute! ["x-body-container" "data-touch-detected" true]}
          {:db (assoc-in db (db/meta-item-path :environment/touch-data :touch-events-api.detected?) false)
           :environment/set-element-attribute! ["x-body-container" "data-touch-detected" false]})))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [:environment/detect-touch-events-api!]})
