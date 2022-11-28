
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.activities.time-handler.subs
    (:require [re-frame.api     :as r :refer [r]]
              [time.api         :as time]
              [x.dictionary.api :as x.dictionary]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-actual-timestamp
  ; @param (string) timestamp
  ;
  ; @example
  ;  (r get-actual-timestamp db "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "Today, 16:20"
  ;
  ; @example
  ;  (r get-actual-timestamp db "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "2020/04/20"
  ;
  ; @return (string)
  [db [_ timestamp]]
  (if (time/timestamp-string->today? timestamp)
      (let [today-term (r x.dictionary/look-up db :today)
            time       (time/timestamp-string->time timestamp :hhmm)]
           (str today-term ", " time))
      (time/timestamp-string->date timestamp :yyyymmdd)))

(defn get-actual-elapsed-time
  ; @param (string) timestamp
  ;
  ; @example
  ;  (r get-actual-timestamp db "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "5m"
  ;
  ; @example
  ;  (r get-actual-timestamp db "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "7d"
  ;
  ; @return (string)
  [db [_ timestamp]])
  ; TODO



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.activities/get-actual-timestamp "2020-04-20T16:20:00.123Z"]
(r/reg-sub :x.activities/get-actual-timestamp get-actual-timestamp)

; @usage
;  [:x.activities/get-actual-elapsed-time "2020-04-20T16:20:00.123Z"]
(r/reg-sub :x.activities/get-actual-elapsed-time get-actual-elapsed-time)
