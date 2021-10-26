
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.26
; Description:
; Version: v0.2.2
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.process-bar
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.api   :as elements]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db (db/meta-item-path ::primary)))

(a/reg-sub ::get-view-props get-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- listen-to-request!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (map)
  [db [_ request-id]]
  (assoc-in db (db/meta-item-path ::primary :request-id) request-id))

(a/reg-event-db :x.app-ui/listen-to-request! listen-to-request!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- process-bar
  [component-id view-props]
  [:div#x-app-process-bar])


(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [components/subscriber ::view {:component  #'process-bar
                                 :subscriber [::get-view-props]}])
