
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.20
; Description:
; Version: v0.2.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.engine
    (:require [mid-fruits.map :refer [dissoc-in]]
              [x.app-core.api :as a :refer [r]]
              [x.app-db.api   :as db]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-component-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (keyword) prop-id
  ;
  ; @return (*)
  [db [_ component-id prop-id]]
  (get-in db (db/path ::components component-id prop-id)))

(a/reg-sub :components/get-component-prop get-component-prop)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-component-prop!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (keyword) prop-id
  ; @param (*) prop-value
  ;
  ; @return (map)
  [db [_ component-id prop-id prop-value]]
  (assoc-in db (db/path ::components component-id prop-id) prop-value))

(defn remove-component-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (map)
  [db [_ component-id]]
  (dissoc-in db (db/path ::components component-id)))