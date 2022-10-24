
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.default-handler.subs
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-default-routes
  ; @param (keyword) route-id
  ;
  ; @usage
  ;  (r get-default-routes db)
  ;
  ; @return (map)
  [db [_ route-id]]
  (get-in db [:router :default-handler/data-items]))

(defn get-default-route
  ; @param (keyword) route-id
  ;
  ; @usage
  ;  (r get-default-route db :my-default-route)
  ;
  ; @return (map)
  [db [_ route-id]]
  (get-in db [:router :default-handler/data-items route-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:router/get-default-routes]
(r/reg-sub :router/get-default-routes get-default-routes)

; @usage
;  [:router/get-default-route :my-default-route]
(r/reg-sub :router/get-default-route get-default-route)
