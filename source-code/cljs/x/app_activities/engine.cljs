
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.06
; Description:
; Version: v0.4.0
; Compatibility: x4.4.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-activities.engine
    (:require [mid-fruits.time      :as time]
              [x.app-core.api       :as a :refer [r]]
              [x.app-dictionary.api :as dictionary]))



;; -- Subscriptions -----------------------------------------------------------
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
