
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.23
; Description:
; Version: v0.4.6
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.default-handler
    (:require [mid-fruits.candy  :refer [param]]
              [x.server-core.api :as a]
              [x.server-db.api   :as db]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  A szerver inicializálásakor szükséges beállítani a :method-not-allowed, :not-acceptable
;  és :not-found alapértelmezett kezelőket.
;
; (a/reg-lifecycles
;   ::lifecycles
;   {:on-app-init {:dispatch-n [[:x.server-router/set-default-route! :method-not-allowed {...}]
;                               [:x.server-router/set-default-route! :not-acceptable     {...}]
;                               [:x.server-router/set-default-route! :not-found          {...}]]}})



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-default-route
  ; @param (keyword) route-id
  ;
  ; @return (map)
  [db [_ route-id]]
  (get-in db (db/path ::default-routes route-id)))

(a/reg-sub :x.server-router/get-default-route get-default-route)

(defn get-default-routes
  ; @param (keyword) route-id
  ;
  ; @return (map)
  [db [_ route-id]]
  (get-in db (db/path ::default-routes)))

(a/reg-sub :x.server-router/get-default-routes get-default-routes)



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
  ; @return (map)
  [db [_ route-id route-props]]
  (assoc-in db (db/path ::default-routes route-id) route-props))

(a/reg-event-db :x.server-router/set-default-route! set-default-route!)
