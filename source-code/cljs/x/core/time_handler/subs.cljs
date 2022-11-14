
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.time-handler.subs
    (:require [time.api     :as time]
              [re-frame.api :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-server-time
  ; @usage
  ;  (r get-server-time db)
  [db _]
  (get-in db [:x.core :time-handler/meta-items :server-time]))

(defn get-server-year
  ; @usage
  ;  (r get-server-year db)
  [db _]
  (let [server-time (r get-server-time db)]
       (time/timestamp-string->year server-time)))

(defn get-server-month
  ; @usage
  ;  (r get-server-month db)
  [db _])

(defn get-server-week
  ; @usage
  ;  (r get-server-week db)
  [db _])

(defn get-server-day
  ; @usage
  ;  (r get-server-day db)
  [db _])

(defn get-server-hour
  ; @usage
  ;  (r get-server-hour db)
  [db _])

(defn get-server-seconds
  ; @usage
  ;  (r get-server-seconds db)
  [db _])

(defn get-server-milliseconds
  ; @usage
  ;  (r get-server-milliseconds db)
  [db _])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.core/get-server-time]
(r/reg-sub :x.core/get-server-time get-server-time)

; @usage
;  [:x.core/get-server-year]
(r/reg-sub :x.core/get-server-year get-server-year)

; @usage
;  [:x.core/get-server-month]
(r/reg-sub :x.core/get-server-month get-server-month)

; @usage
;  [:x.core/get-server-week]
(r/reg-sub :x.core/get-server-week get-server-week)

; @usage
;  [:x.core/get-server-day]
(r/reg-sub :x.core/get-server-day get-server-day)

; @usage
;  [:x.core/get-server-hour]
(r/reg-sub :x.core/get-server-hour get-server-hour)

; @usage
;  [:x.core/get-server-seconds]
(r/reg-sub :x.core/get-server-seconds get-server-seconds)

; @usage
;  [:x.core/get-server-milliseconds]
(r/reg-sub :x.core/get-server-milliseconds get-server-milliseconds)
