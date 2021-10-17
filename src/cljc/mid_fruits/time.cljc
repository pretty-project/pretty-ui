
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.09.11
; Description:
; Version: v1.0.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.time
    (:import #?(:clj org.joda.time.DateTime))
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.string :as string]

              #?(:clj  [clj-time.core   :as clj-time.core])
              #?(:clj  [clj-time.format :as clj-time.format])
              #?(:cljs [cljs-time.core  :as cljs-time.core])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn elapsed
  ; @return (ms)
  []
     ; TODO ...
     ; The returned value represents the time elapsed since the application's lifetime.
  #?(:clj  (return 0)
     ; The returned value represents the time elapsed since the document's lifetime.
     :cljs (.now js/performance)))

(defn timestamp
  ; @param (string)(opt) time-zone
  ;
  ; @example (clj)
  ;  (time/timestamp)
  ;  => #<DateTime 2020-04-20T16:20:00.123Z>
  ;
  ; @example (clj)
  ;  (time/timestamp "Europe/Budapest")
  ;  => #<DateTime 2020-04-20T16:20:00.123+02:00>
  ;
  ; @return (object)
  [& [time-zone]]
  #?(:clj (let [timestamp (clj-time.core/now)]
               (if (some? time-zone)
                   (let [time-zone (clj-time.core/time-zone-for-id time-zone)]
                        (clj-time.core/to-time-zone timestamp time-zone))
                   (return timestamp)))
     :cljs (cljs-time.core/now)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn timestamp-string?
  ; @param (*) n
  ;
  ; @example
  ;  (time/timestamp-string? "2020-04-20T20:00.123+00:00")
  ;  => true
  ;
  ; @example
  ;  (time/timestamp-string? "2020-04-20T16:20:00.123Z")
  ;  => true
  ;
  ; @example
  ;  (time/timestamp-string? "2020-04-20T16:20:00.123")
  ;  => true
  ;
  ; @return (boolean)
  [n]
  (boolean (and (string? n)
                (or (re-matches #"\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.\d{3}\+\d{2}:\d{2}" n)
                    (re-matches #"\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.\d{3}Z"             n)
                    (re-matches #"\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.\d{3}"              n)))))

(defn timestamp-object?
  ; @param (*) n
  ;
  ; @return (boolean)
  [n]
  (let [type (type n)]
       #?(:clj (= type org.joda.time.DateTime))))

(defn date-string?
  ; @param (*) n
  ;
  ; @example
  ;  (time/date-string? "2020-04-20")
  ;  => true
  ;
  ; @return (boolean)
  [n]
  (and (string? n)
       (re-matches #"\d{4}[-|.]\d{2}[-|.]\d{2}" n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn timestamp-part<-leading-zero
  ; @param (integer or string) n
  ;
  ; @example
  ;  (time/timestamp-part<-leading-zero 1)
  ;  => "01"
  ;
  ; @return (string)
  [n]
  (let [n (str n)]
       (if (string/length? n 1)
           (str        "0" n)
           (return         n))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn timestamp->year
  ; @param (object or string) n
  ;
  ; @example
  ;  (time/timestamp->year "2020-04-20T16:20:00.123Z")
  ;  => "2020"
  ;
  ; @example
  ;  (time/timestamp->year #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  => 2020
  ;
  ; @return (integer or string)
  [n]
  (if (string?     n)
      (string/part n 0 4)
      #?(:clj  (clj-time.core/year  n)
         :cljs (cljs-time.core/year n))))

(defn timestamp->month
  ; @param (object or string) n
  ;
  ; @example
  ;  (time/timestamp->month "2020-04-20T16:20:00.123Z")
  ;  => "04"
  ;
  ; @example
  ;  (time/timestamp->month #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  => 4
  ;
  ; @return (integer or string)
  [n]
  (if (string?     n)
      (string/part n 5 7)
      #?(:clj  (clj-time.core/month  n)
         :cljs (cljs-time.core/month n))))

(defn timestamp->day
  ; @param (object or string) n
  ;
  ; @example
  ;  (time/timestamp->day "2020-04-20T16:20:00.123Z")
  ;  => "20"
  ;
  ; @example
  ;  (time/timestamp->day #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  => 20
  ;
  ; @return (integer or string)
  [n]
  (if (string?     n)
      (string/part n 8 10)
      #?(:clj  (clj-time.core/day  n)
         :cljs (cljs-time.core/day n))))

(defn timestamp->hours
  ; @param (object or string) n
  ;
  ; @example
  ;  (time/timestamp->hours "2020-04-20T16:20:00.123Z")
  ;  => "16"
  ;
  ; @example
  ;  (time/timestamp->hours #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  => 16
  ;
  ; @return (integer or string)
  [n]
  (if (string?     n)
      (string/part n 11 13)
      #?(:clj  (clj-time.core/hours  n)
         :cljs (cljs-time.core/hours n))))

(defn timestamp->hours-2
  ; @param (object or string) n
  ;
  ; @example
  ;  (time/timestamp->hours "2020-04-20T16:20:00.123Z")
  ;  => "16"
  ;
  ; @example
  ;  (time/timestamp->hours #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  => 16
  ;
  ; @return (integer or string)
  [n]
  (if (string?     n)
      (string/part n 11 13))
  #?(:clj  (clj-time.core/hours  n)
     :cljs (cljs-time.core/hours (cljs-time.core/now))))

(defn timestamp->minutes
  ; @param (object or string) n
  ;
  ; @example
  ;  (time/timestamp->minutes "2020-04-20T16:20:00.123Z")
  ;  => "20"
  ;
  ; @example
  ;  (time/timestamp->minutes #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  => 20
  ;
  ; @return (integer or string)
  [n]
  (if (string?     n)
      (string/part n 14 16)
      #?(:clj  (clj-time.core/minutes  n)
         :cljs (cljs-time.core/minutes n))))

(defn timestamp->seconds
  ; @param (object or string) n
  ;
  ; @example
  ;  (time/timestamp->seconds "2020-04-20T16:20:00.123Z")
  ;  => "00"
  ;
  ; @example
  ;  (time/timestamp->seconds #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  => 0
  ;
  ; @return (integer or string)
  [n]
  (if (string?     n)
      (string/part n 17 19)
      #?(:clj  (clj-time.core/seconds  n)
         :cljs (cljs-time.core/seconds n))))

(defn timestamp->milliseconds
  ; @param (object or string) n
  ;
  ; @example
  ;  (time/timestamp->milliseconds "2020-04-20T16:20:00.123Z")
  ;  => "123"
  ;
  ; @example
  ;  (time/timestamp->milliseconds #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  => 123
  ;
  ; @return (integer or string)
  [n]
  (if (string?     n)
      (string/part n 20 23)
      #?(:clj  (clj-time.core/milli  n)
         :cljs (cljs-time.core/milli n))))

(defn timestamp->date
  ; @param (string) n
  ; @param (keyword)(opt) format
  ;  :yyyymmdd, :yymmdd
  ;  Default: :yyyymmdd
  ;
  ; @example
  ;  (time/timestamp->date "2020-04-20T16:20:00.123Z" :yyyymmdd)
  ;  => "2020. 04. 20."
  ;
  ; @example
  ;  (time/timestamp->date #<DateTime 2020-04-20T16:20:00.123Z> :yyyymmdd)
  ;  => "2020. 04. 20."
  ;
  ; @return (string)
  ([n]
   (timestamp->date n :yyyymmdd))

  ([n format]
   (let [year  (timestamp->year n)
         month (timestamp-part<-leading-zero (timestamp->month n))
         day   (timestamp-part<-leading-zero (timestamp->day   n))]
        (case format :yyyymmdd (str year ". " month ". " day ".")
                     :yymmdd   (let [year (string/part year 2 2)]
                                    (str year ". " month ". " day "."))
                     (return n)))))

(defn timestamp->time
  ; @param (string) n
  ; @param (keyword)(opt) format
  ;  :hhmmss, :hhmm
  ;  Default: :hhmmss
  ;
  ; @example
  ;  (time/timestamp->time "2020-04-20T16:20:00.123Z" :hhmmss)
  ;  => "16:20:00"
  ;
  ; @example
  ;  (time/timestamp->time #<DateTime 2020-04-20T16:20:00.123Z> :hhmmss)
  ;  => "16:20:00"
  ;
  ; @return (string)
  ([n]
   (timestamp->time n :hhmmss))

  ([n format]
   (let [hours   (timestamp-part<-leading-zero (timestamp->hours   n))
         minutes (timestamp-part<-leading-zero (timestamp->minutes n))
         seconds (timestamp-part<-leading-zero (timestamp->seconds n))]
        (case format :hhmmss (str hours ":" minutes ":" seconds)
                     :hhmm   (str hours ":" minutes)
                     (return n)))))

(defn timestamp->date-and-time
  ; @param (string) n
  ; @param (keyword)(opt) date-format
  ;  :yyyymmdd, :yymmdd
  ;  Default: :yyyymmdd
  ; @param (keyword)(opt) time-format
  ;  :hhmmss, :hhmm
  ;  Default: :hhmmss
  ;
  ; @example
  ;  (time/timestamp->date-and-time "2020-04-20T16:20:00.123Z" :yyyymmdd :hhmmss)
  ;  => "2020. 04. 20. - 16:20:00"
  ;
  ; @example
  ;  (time/timestamp->date-and-time #<DateTime 2020-04-20T16:20:00.123Z> :yyyymmdd :hhmmss)
  ;  => "2020. 04. 20. - 16:20:00"
  ;
  ; @return (string)
  ([n]
   (timestamp->date-and-time n :yyyymmdd :hhmmss))

  ([n time-format]
   (timestamp->date-and-time n :yyyymmdd time-format))

  ([n date-format time-format]
   (if (string/nonempty? n)
       (let [date (timestamp->date n date-format)
             time (timestamp->time n time-format)]
            (str date " - " time)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-year
  ; @return (integer)
  []
  (let [timestamp (timestamp)]
       (timestamp->year timestamp)))

(defn get-month
  ; @return (integer)
  []
  (let [timestamp (timestamp)]
       (timestamp->month timestamp)))

(defn get-day
  ; @return (integer)
  []
  (let [timestamp (timestamp)]
       (timestamp->day timestamp)))

(defn get-hours
  ; @return (integer)
  []
  (let [timestamp (timestamp)]
       (timestamp->hours timestamp)))

(defn get-hours-2
  ; @return (integer)
  []
  #?(:cljs (cljs-time.core/hours (cljs-time.core/now))))

(defn get-hours-left-from-this-day
  ; @return (integer)
  []
  (let [hours (get-hours)]
       (- 24 hours)))

(defn get-minutes
  ; @return (integer)
  []
  (let [timestamp (timestamp)]
       (timestamp->minutes timestamp)))

(defn get-minutes-left-from-this-hour
  ; @return (integer)
  []
  (let [minutes (get-minutes)]
       (- 60 minutes)))

(defn get-seconds
  ; @return (integer)
  []
  (let [timestamp (timestamp)]
       (timestamp->seconds timestamp)))

(defn get-seconds-left-from-this-minute
  ; @return (integer)
  []
  (let [seconds (get-seconds)]
       (- 60 seconds)))

(defn get-milliseconds
  ; @return (integer)
  []
  (let [timestamp (timestamp)]
       (timestamp->milliseconds timestamp)))

(defn get-milliseconds-left-from-this-second
  ; @return (integer)
  [])
  ; TODO ...

(defn get-milliseconds-left-from-this-minute
  ; @return (integer)
  []
  (let [seconds-left (get-seconds-left-from-this-minute)]
       (* 1000 seconds-left)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-timeout!
  ; @param (integer) timeout
  ; @param (function) f
  ;
  ; @return (integer)
  [timeout f]
  #?(:clj  (f)
     :cljs (.setTimeout js/window f timeout)))

(defn set-interval!
  ; @param (integer) interval
  ; @param (function) f
  ;
  ; @return (integer)
  [interval f]
  #?(:clj  (f)
     :cljs (.setInterval js/window f interval)))

(defn clear-interval!
  ; @param (integer) interval-id
  ;
  ; @return (nil)
  [interval-id]
  #?(:clj  (return nil)
     :cljs (.clearInterval js/window interval-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reduce-interval
  ; @param (function) f
  ; @param (collection) coll
  ; @param (integer) interval
  ;
  ; @example
  ;  (time/reduce-interval my-function [:a :b :c] 500)
  ;  => (time/set-timeout!    0 #(my-function :a))
  ;     (time/set-timeout!  500 #(my-function :b))
  ;     (time/set-timeout! 1000 #(my-function :c))
  ;
  ; @return (*)
  [f coll interval]
  (reduce (fn [lap item]
              (set-timeout! (* lap interval)
                           #(f item))
              (inc lap))
          (param 0)
          (param coll)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn parse-date
  ; @param (string) n
  ;
  ; @example
  ;  (time/parse-date "2020-04-20")
  ;  => #<DateTime 2020-04-20T00:00:00.000Z>
  ;
  ; @return (object)
  [n]
  #?(:clj (let [formatter (clj-time.format/formatter "yyyy-MM-dd")]
               (clj-time.format/parse formatter n))))

(defn parse-timestamp
  ; @param (string) n
  ;
  ; @example
  ;  (time/parse-timestamp "2020-04-20T16:20:00.123")
  ;  => #<DateTime 2020-04-20T16:20:00.123Z>
  ;
  ; @return (object)
  [n]
  #?(:clj (let [formatter (clj-time.format/formatters :date-time)]
               (clj-time.format/parse formatter n))))

(defn unparse-timestamp
  ; @param (object) n
  ;
  ; @example
  ;  (time/parse-timestamp #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  => "2020-04-20T16:20:00.123"
  ;
  ; @return (string)
  [n]
  (str n))
