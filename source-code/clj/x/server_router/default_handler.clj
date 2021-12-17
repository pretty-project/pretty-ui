
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.23
; Description:
; Version: v0.7.0
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.default-handler
    (:require [mid-fruits.candy  :refer [param]]
              [x.server-core.api :as a]
              [x.server-db.api   :as db]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-default-routes
  ; @param (keyword) route-id
  ;
  ; @usage
  ;  (r router/get-default-routes db)
  ;
  ; @return (map)
  [db [_ route-id]]
  (get-in db (db/path ::default-routes)))

; @usage
;  [:router/get-default-routes]
(a/reg-sub :router/get-default-routes get-default-routes)

(defn get-default-route
  ; @param (keyword) route-id
  ;
  ; @usage
  ;  (r router/get-default-route db :my-default-route)
  ;
  ; @return (map)
  [db [_ route-id]]
  (get-in db (db/path ::default-routes route-id)))

; @usage
;  [:router/get-default-route :my-default-route]
(a/reg-sub :router/get-default-route get-default-route)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-default-route!
  ; https://github.com/metosin/reitit/blob/master/doc/ring/default_handler.md
  ;
  ; @param (keyword) route-id
  ;  :method-not-allowed, :not-acceptable, :not-found
  ; @param (map) route-props
  ;  {:body (*)
  ;   :mime-type (string)
  ;   :status (integer)}
  ;
  ; @usage
  ;  (r router/set-default-route! db :my-default-route {...})
  ;
  ; @return (map)
  [db [_ route-id route-props]]
  (assoc-in db (db/path ::default-routes route-id) route-props))

; @usage
;  [:router/set-default-route! :my-default-route {...}]
(a/reg-event-db :router/set-default-route! set-default-route!)
