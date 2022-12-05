
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.infinite-loader.events
    (:require [re-frame.api :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hide-observer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (map)
  [db [_ loader-id]]
  (assoc-in db [:tools :infinite-loader/data-items loader-id :observer-visible?] false))

(defn show-observer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (map)
  [db [_ loader-id]]
  (assoc-in db [:tools :infinite-loader/data-items loader-id :observer-visible?] true))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn pause-loader!
  ; @param (keyword) loader-id
  ;
  ; @usage
  ; (r pause-loader! db :my-loader)
  ;
  ; @return (map)
  [db [_ loader-id]]
  (r hide-observer! db loader-id))

(defn restart-loader!
  ; @param (keyword) loader-id
  ;
  ; @usage
  ; (r restart-loader! db :my-loader)
  ;
  ; @return (map)
  [db [_ loader-id]]
  (r show-observer! db loader-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :infinite-loader/hide-observer! hide-observer!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :infinite-loader/show-observer! show-observer!)

; @usage
; [:infinite-loader/pause-loader! :my-loader]
(r/reg-event-db :infinite-loader/pause-loader! pause-loader!)

; @usage
; [:infinite-loader/restart-loader! :my-loader]
(r/reg-event-db :infinite-loader/restart-loader! restart-loader!)
