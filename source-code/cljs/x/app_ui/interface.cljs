
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.20
; Description:
; Version: v0.5.4
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.interface
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-db.api   :as db]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-interface
  ; @usage
  ;  (r ui/get-interface db)
  ;
  ; @return (keyword)
  ;  :application-ui, :website-ui
  [db _]
  (get-in db (db/path :ui/interface :interface)))

(defn application-interface?
  ; @usage
  ;  (r ui/application-interface? db)
  ;
  ; @return (boolean)
  [db _]
  (= :application-ui (r get-interface db)))

(defn website-interface?
  ; @usage
  ;  (r ui/website-interface? db)
  ;
  ; @return (boolean)
  [db _]
  (= :website-ui (r get-interface db)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- set-interface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) interface
  ;
  ; @return (map)
  [db [_ interface]]
  (assoc-in db (db/path :ui/interface :interface) interface))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/set-interface!
  ; @param (keyword) interface
  ;  :application-ui, :website-ui
  ;
  ; @usage
  ;  [:ui/set-interface! :application-ui]
  (fn [{:keys [db]} [_ interface]]
      {:db (r set-interface! db interface)
       :fx [:environment/set-element-attribute! "x-body-container" "data-interface" (name interface)]}))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-init [:ui/set-interface! :website-ui]
   :on-login    [:ui/set-interface! :application-ui]})
