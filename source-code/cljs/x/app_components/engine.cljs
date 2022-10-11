
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.20
; Description:
; Version: v0.2.8
; Compatibility: x4.5.6



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.engine
    (:require [mid-fruits.map :refer [dissoc-in]]
              [re-frame.api   :as r]
              [x.app-db.api   :as db]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-component-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ component-id prop-key]]
  (get-in db (db/path :components/primary component-id prop-key)))

(r/reg-sub :components/get-component-prop get-component-prop)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-component-prop!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (keyword) prop-key
  ; @param (*) prop-value
  ;
  ; @return (map)
  [db [_ component-id prop-key prop-value]]
  (assoc-in db (db/path :components/primary component-id prop-key) prop-value))

(defn remove-component-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (map)
  [db [_ component-id]]
  (dissoc-in db (db/path :components/primary component-id)))
