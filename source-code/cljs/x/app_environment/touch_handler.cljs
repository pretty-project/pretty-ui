
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.3.8
; Compatibility: x4.4.6



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
  (get-in db (db/meta-item-path :environment/touch-data :touch-events-api.detected?)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/detect-touch-events-api!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (if (dom/touch-events-api-detected?)
      [:environment/->touch-events-api-detected]
      [:environment/->touch-events-api-not-detected]))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/->touch-events-api-detected
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db       (assoc-in db (db/meta-item-path :environment/touch-data :touch-events-api.detected?) true)
       :dispatch [:environment/set-element-attribute! "x-body-container" "data-touch-detected" true]}))

(a/reg-event-fx
  :environment/->touch-events-api-not-detected
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db       (assoc-in db (db/meta-item-path :environment/touch-data :touch-events-api.detected?) false)
       :dispatch [:environment/set-element-attribute! "x-body-container" "data-touch-detected" false]}))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [:environment/detect-touch-events-api!]})
