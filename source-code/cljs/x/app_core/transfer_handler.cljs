
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.22
; Description:
; Version: v0.3.0
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.transfer-handler
    (:require [mid-fruits.map           :as map]
              [x.app-core.event-handler :as event-handler :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-transfer-data
  ; @param (keyword) transfer-id
  ;
  ; @usage
  ;  (r a/get-transfer-data db :my-transfer)
  ;
  ; @return (*)
  [db [_ transfer-id]]
  (get-in db [:core/transfer-handler :data-items transfer-id]))

;(event-handler/reg-sub :core/get-transfer-data get-transfer-data)

(defn get-transfer-item
  ; @param (keyword) transfer-id
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r a/get-transfer-item db :my-transfer :my-item)
  ;
  ; @return (*)
  [db [_ transfer-id item-key]]
  (get-in db [:core/transfer-handler :data-items transfer-id item-key]))

;(event-handler/reg-sub :core/get-transfer-item get-transfer-item)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-transfer-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ server-response]]
  (letfn [(f [db transfer-id {:keys [data target-path]}]
             (cond-> db target-path (assoc-in target-path data)
                        :store-data (assoc-in [:core/transfer-handler :data-items transfer-id] data)))]
         (reduce-kv f db server-response)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/synchronize-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:core/synchronize-app! #'app]
  (fn [cofx [_ app]]
      [:sync/send-request! :core/synchronize-app!
                           {:method     :get
                            :on-failure [:core/error-catched {:cofx cofx :error "Failed to synchronize app!"}]
                            :on-success [:core/app-synchronized app]
                            :uri        "/synchronize-app"}]))

(event-handler/reg-event-fx
  :core/app-synchronized
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (component) app
  ;
  ; @usage
  ;  [:core/app-synchronized #'app {...}]
  (fn [{:keys [db] :as cofx} [_ app server-response]]
      {:db (r store-transfer-data! db server-response)
       :dispatch-if [(-> server-response map/nonempty? not)
                     [:core/error-catched {:cofx cofx :error "Failed to synchronize app!"}]
                     [:boot-loader/app-synchronized app]]}))
