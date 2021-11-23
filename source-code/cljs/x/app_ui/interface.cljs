
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.20
; Description:
; Version: v0.4.4
; Compatibility: x4.3.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.interface
    (:require [mid-fruits.candy :refer [param]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-db.api     :as db]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-interface
  ; @usage
  ;  (r ui/get-interface db)
  ;
  ; @return (keyword)
  ;  :application-ui, :website-ui
  [db _]
  (get-in db (db/path ::primary :interface)))

(defn application-interface?
  ; @usage
  ;  (r ui/application-interface? db)
  ;
  ; @return (boolean)
  [db _]
  (= (r get-interface db)
     (param :application-ui)))

(defn website-interface?
  ; @usage
  ;  (r ui/website-interface? db)
  ;
  ; @return (boolean)
  [db _]
  (= (r get-interface db)
     (param :website-ui)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- set-interface!
  ; @param (keyword) interface
  ;  :application-ui, :website-ui
  ;
  ; @usage
  ;  (r ui/set-interface! db :application-ui)
  ;
  ; @return (map)
  [db [_ interface]]
  (assoc-in db (db/path ::primary :interface) interface))

; @usage
;  [:x.app-ui/set-interface! :application-ui]
(a/reg-event-db :x.app-ui/set-interface! set-interface!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [:x.app-ui/set-interface! :website-ui]
   :on-login    [:x.app-ui/set-interface! :application-ui]})
