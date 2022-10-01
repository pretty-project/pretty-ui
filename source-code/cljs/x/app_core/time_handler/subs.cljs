
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.time-handler.subs
    (:require [mid-fruits.time          :as time]
              [x.app-core.event-handler :as event-handler :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-server-time
  ; @usage
  ;  (r a/get-server-time db)
  [db _]
  (get-in db [:core :time-handler/meta-items :server-time]))

(defn get-server-year
  ; @usage
  ;  (r a/get-server-year db)
  [db _]
  (let [server-time (r get-server-time db)]
       (time/timestamp-string->year server-time)))

(defn get-server-month
  ; @usage
  ;  (r a/get-server-month db)
  [db _])

(defn get-server-week
  ; @usage
  ;  (r a/get-server-week db)
  [db _])

(defn get-server-day
  ; @usage
  ;  (r a/get-server-day db)
  [db _])

(defn get-server-hour
  ; @usage
  ;  (r a/get-server-hour db)
  [db _])

(defn get-server-seconds
  ; @usage
  ;  (r a/get-server-seconds db)
  [db _])

(defn get-server-milliseconds
  ; @usage
  ;  (r a/get-server-milliseconds db)
  [db _])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:core/get-server-time]
(event-handler/reg-sub :core/get-server-time get-server-time)

; @usage
;  [:core/get-server-year]
(event-handler/reg-sub :core/get-server-year get-server-year)

; @usage
;  [:core/get-server-month]
(event-handler/reg-sub :core/get-server-month get-server-month)

; @usage
;  [:core/get-server-week]
(event-handler/reg-sub :core/get-server-week get-server-week)

; @usage
;  [:core/get-server-day]
(event-handler/reg-sub :core/get-server-day get-server-day)

; @usage
;  [:core/get-server-hour]
(event-handler/reg-sub :core/get-server-hour get-server-hour)

; @usage
;  [:core/get-server-seconds]
(event-handler/reg-sub :core/get-server-seconds get-server-seconds)

; @usage
;  [:core/get-server-milliseconds]
(event-handler/reg-sub :core/get-server-milliseconds get-server-milliseconds)
