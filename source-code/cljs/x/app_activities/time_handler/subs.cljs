
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-activities.time-handler.subs
    (:require [mid-fruits.time      :as time]
              [x.app-core.api       :as a :refer [r]]
              [x.app-dictionary.api :as dictionary]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-actual-timestamp
  ; @param (string) timestamp
  ;
  ; @example
  ;  (r activities/get-actual-timestamp db "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "Today, 16:20"
  ;
  ; @example
  ;  (r activities/get-actual-timestamp db "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "2020/04/20"
  ;
  ; @return (string)
  [db [_ timestamp]]
  (if (time/timestamp-string->today? timestamp)
      (let [today-term (r dictionary/look-up db :today)
            time       (time/timestamp-string->time timestamp :hhmm)]
           (str today-term ", " time))
      (time/timestamp-string->date timestamp :yyyymmdd)))

(defn get-actual-elapsed-time
  ; @param (string) timestamp
  ;
  ; @example
  ;  (r activities/get-actual-timestamp db "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "5m"
  ;
  ; @example
  ;  (r activities/get-actual-timestamp db "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "7d"
  ;
  ; @return (string)
  [db [_ timestamp]])
  ; TODO ...



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:activities/get-actual-timestamp "2020-04-20T16:20:00.123Z"]
(a/reg-sub :activities/get-actual-timestamp get-actual-timestamp)

; @usage
;  [:activities/get-actual-elapsed-time "2020-04-20T16:20:00.123Z"]
(a/reg-sub :activities/get-actual-elapsed-time get-actual-elapsed-time)
