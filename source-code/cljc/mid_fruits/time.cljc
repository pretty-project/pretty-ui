
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.09.11
; Description:
; Version: v1.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.time
    (:import #?(:clj org.joda.time.DateTime)
             #?(:clj org.bson.types.BSONTimestamp))

    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.format :as format]
              [mid-fruits.map    :as map]
              [mid-fruits.math   :as math]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]

              #?(:clj  [clj-time.coerce  :as clj-time.coerce])
              #?(:clj  [clj-time.core    :as clj-time.core])
              #?(:clj  [clj-time.format  :as clj-time.format])
              #?(:cljs [cljs-time.core   :as cljs-time.core])
              #?(:cljs [cljs-time.format :as cljs-time.format])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ms->s [n] (/ n 1000))
(defn ms->m [n] (/ n 60000))
(defn ms->h [n] (/ n 3600000))
(defn ms->D [n] (/ n 86400000))
(defn ms->W [n] (/ n 604800000))
(defn s->ms [n] (* n 1000))
(defn s->m  [n] (/ n 60))
(defn s->h  [n] (/ n 3600))
(defn s->D  [n] (/ n 86400))
(defn s->W  [n] (/ n 604800))
(defn m->ms [n] (* n 60000))
(defn m->s  [n] (* n 60))
(defn m->h  [n] (/ n 60))
(defn m->D  [n] (/ n 1440))
(defn m->W  [n] (/ n 10800))
(defn h->ms [n] (* n 3600000))
(defn h->s  [n] (* n 3600))
(defn h->m  [n] (* n 60))
(defn h->D  [n] (/ n 24))
(defn h->W  [n] (/ n 168))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn epoch-ms
  ; @return (ms)
  []
  #?(:clj (-> (clj-time.core/now)
              (clj-time.coerce/to-long))))

(defn epoch-s
  ; @return (s)
  []
  #?(:clj (-> (clj-time.core/now)
              (clj-time.coerce/to-long)
              (quot 1000))))

(defn epoch-ms->timestamp-string
  ; @param (ms) n
  ;
  ; @example
  ;  (time/epoch-ms->timestamp-string 1640800860000)
  ;  =>
  ;  "2021-12-29T18:01:00.000Z"
  ;
  ; @return (string)
  [n]
  #?(:clj (if n (-> n clj-time.coerce/from-long str))))

(defn epoch-s->timestamp-string
  ; @param (s) n
  ;
  ; @example
  ;  (time/epoch-s->timestamp-string 1640800860)
  ;  =>
  ;  "2021-12-29T18:01:00.000Z"
  ;
  ; @return (string)
  [n]
  #?(:clj (if n (-> n s->ms clj-time.coerce/from-long str))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn elapsed
  ; @return (ms)
  [] ; TODO ...
     ; The returned value represents the time elapsed since the application's lifetime.
  #?(:clj  (return 0)
     ; The returned value represents the time elapsed since the document's lifetime.
     :cljs (.now js/performance)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn timestamp-object
  ; @param (string)(opt) time-zone
  ;
  ; @example (clj)
  ;  (time/timestamp-object)
  ;  =>
  ;  #<DateTime 2020-04-20T16:20:00.123Z>
  ;
  ; @example (clj)
  ;  (time/timestamp-object "Europe/Budapest")
  ;  =>
  ;  #<DateTime 2020-04-20T16:20:00.123+02:00>
  ;
  ; @return (object)
  ([]
   #?(:clj (clj-time.core/now)))

  ([time-zone]
   #?(:clj (let [timestamp (clj-time.core/now)
                 time-zone (clj-time.core/time-zone-for-id time-zone)]
                (clj-time.core/to-time-zone timestamp time-zone)))))

(defn timestamp-string
  ; @return (string)
  []
  ; Java időbélyegző-objektum string típussá alakításának formátuma: "2020-04-20T16:20:00.123Z"
  #?(:cljs (let [formatter (cljs-time.format/formatter "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                 timestamp (cljs-time.core/now)]
                (cljs-time.format/unparse formatter timestamp))
     :clj  (let [timestamp-object (timestamp-object)]
                (str timestamp-object))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn timestamp-string?
  ; @param (*) n
  ;
  ; @example
  ;  (time/timestamp-string? "2020-04-20T20:00.123+00:00")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (time/timestamp-string? "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (time/timestamp-string? "2020-04-20T16:20:00.123")
  ;  =>
  ;  true
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
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (boolean (and (string? n)
                (re-matches #"\d{4}[-|.]\d{2}[-|.]\d{2}" n))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn timestamp-string->year
  ; @param (string) n
  ;
  ; @example
  ;  (time/timestamp-string->year "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "2020"
  ;
  ; @return (string)
  [n]
  (string/part n 0 4))

(defn timestamp-object->year
  ; @param (object) n
  ;
  ; @example
  ;  (time/timestamp->year #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  =>
  ;  2020
  ;
  ; @return (integer)
  [n]
  #?(:clj  (clj-time.core/year  n)
     :cljs (cljs-time.core/year n)))

(defn timestamp-string->month
  ; @param (string) n
  ;
  ; @example
  ;  (time/timestamp-string->month "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "04"
  ;
  ; @return (string)
  [n]
  (string/part n 5 7))

(defn timestamp-object->month
  ; @param (object) n
  ;
  ; @example
  ;  (time/timestamp->month #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  =>
  ;  4
  ;
  ; @return (integer)
  [n]
  #?(:clj  (clj-time.core/month  n)
     :cljs (cljs-time.core/month n)))

(defn timestamp-string->day
  ; @param (string) n
  ;
  ; @example
  ;  (time/timestamp-string->day "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "20"
  ;
  ; @return (string)
  [n]
  (string/part n 8 10))

(defn timestamp-object->day
  ; @param (object) n
  ;
  ; @example
  ;  (time/timestamp->day #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  =>
  ;  20
  ;
  ; @return (integer)
  [n]
  #?(:clj  (clj-time.core/day  n)
     :cljs (cljs-time.core/day n)))

(defn timestamp-string->hours
  ; @param (string) n
  ;
  ; @example
  ;  (time/timestamp-string->hours "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "16"
  ;
  ; @return (string)
  [n]
  (string/part n 11 13))

(defn timestamp-object->hours
  ; @param (object) n
  ;
  ; @example
  ;  (time/timestamp->hours #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  =>
  ;  16
  ;
  ; @return (integer)
  [n]
  #?(:clj  (clj-time.core/hours  n)
     :cljs (cljs-time.core/hours n)))

(defn timestamp-string->minutes
  ; @param (string) n
  ;
  ; @example
  ;  (time/timestamp-string->minutes "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "20"
  ;
  ; @return (string)
  [n]
  (string/part n 14 16))

(defn timestamp-object->minutes
  ; @param (object) n
  ;
  ; @example
  ;  (time/timestamp->minutes #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  =>
  ;  20
  ;
  ; @return (integer)
  [n]
  #?(:clj  (clj-time.core/minutes  n)
     :cljs (cljs-time.core/minutes n)))

(defn timestamp-string->seconds
  ; @param (string) n
  ;
  ; @example
  ;  (time/timestamp->seconds "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "00"
  ;
  ; @return (string)
  [n]
  (string/part n 17 19))

(defn timestamp-object->seconds
  ; @param (object) n
  ;
  ; @example
  ;  (time/timestamp->seconds #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  =>
  ;  0
  ;
  ; @return (integer)
  [n]
  #?(:clj  (clj-time.core/seconds  n)
     :cljs (cljs-time.core/seconds n)))

(defn timestamp-string->milliseconds
  ; @param (string) n
  ;
  ; @example
  ;  (time/timestamp->milliseconds "2020-04-20T16:20:00.123Z")
  ;  =>
  ;  "123"
  ;
  ; @return (string)
  [n]
  (string/part n 20 23))

(defn timestamp-object->milliseconds
  ; @param (object) n
  ;
  ; @example
  ;  (time/timestamp->milliseconds #<DateTime 2020-04-20T16:20:00.123Z>)
  ;  =>
  ;  123
  ;
  ; @return (integer)
  [n]
  #?(:clj  (clj-time.core/milli  n)
     :cljs (cljs-time.core/milli n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn timestamp-string->date
  ; @param (string) n
  ; @param (keyword)(opt) format
  ;  :yyyymmdd, :yymmdd
  ;  Default: :yyyymmdd
  ;
  ; @example
  ;  (time/timestamp-string->date "2020-04-20T16:20:00.123Z" :yyyymmdd)
  ;  =>
  ;  "2020/04/20"
  ;
  ; @return (string)
  ([n]
   (timestamp-string->date n :yyyymmdd))

  ([n format]
   (if (string/nonempty? n)
       (let [year  (timestamp-string->year n)
             month (format/leading-zeros (timestamp-string->month n) 2)
             day   (format/leading-zeros (timestamp-string->day   n) 2)]
            (case format :yyyymmdd (str year "/" month "/" day)
                         :yymmdd   (let [year (string/part year 2 2)]
                                        (str year "/" month "/" day))
                         (return n))))))

(defn timestamp-string->time
  ; @param (string) n
  ; @param (keyword)(opt) format
  ;  :hhmmss, :hhmm
  ;  Default: :hhmmss
  ;
  ; @example
  ;  (time/timestamp-string->time "2020-04-20T16:20:00.123Z" :hhmmss)
  ;  =>
  ;  "16:20:00"
  ;
  ; @return (string)
  ([n]
   (timestamp-string->time n :hhmmss))

  ([n format]
   (if (string/nonempty? n)
       (let [hours   (format/leading-zeros (timestamp-string->hours   n) 2)
             minutes (format/leading-zeros (timestamp-string->minutes n) 2)
             seconds (format/leading-zeros (timestamp-string->seconds n) 2)]
            (case format :hhmmss (str hours ":" minutes ":" seconds)
                         :hhmm   (str hours ":" minutes)
                         (return n))))))

(defn timestamp-string->date-time
  ; @param (string) n
  ; @param (keyword)(opt) date-format
  ;  :yyyymmdd, :yymmdd
  ;  Default: :yyyymmdd
  ; @param (keyword)(opt) time-format
  ;  :hhmmss, :hhmm
  ;  Default: :hhmmss
  ;
  ; @example
  ;  (time/timestamp->date-time "2020-04-20T16:20:00.123Z" :yyyymmdd :hhmmss)
  ;  =>
  ;  "2020/04/20 - 16:20:00"
  ;
  ; @return (string)
  ([n]
   (timestamp-string->date-time n :yyyymmdd :hhmmss))

  ([n time-format]
   (timestamp-string->date-time n :yyyymmdd time-format))

  ([n date-format time-format]
   (if (string/nonempty? n)
       (let [date (timestamp-string->date n date-format)
             time (timestamp-string->time n time-format)]
            (str date " - " time)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ms->time
  ; @param (float, integer or string) n
  ; @param (keyword)(opt) format
  ;  :hhmmssmmm, :hhmmss
  ;
  ; @example
  ;  (time/ms->time 260000)
  ;  =>
  ;  "00:04:20.000"
  ;
  ; @example
  ;  (time/ms->time 260000.123)
  ;  =>
  ;  "00:04:20.000"
  ;
  ; @example
  ;  (time/ms->time 260000 :hhmmss)
  ;  =>
  ;  "00:04:20"
  ;
  ; @return (string)
  ([n]
   (ms->time n :hhmmssmmm))

  ([n format]
   (if (some? n)
       (let [hours        (format/leading-zeros      (-> n ms->h math/floor)       2)
             minutes      (format/leading-zeros (rem (-> n ms->m math/floor) 60)   2)
             seconds      (format/leading-zeros (rem (-> n ms->s math/floor) 60)   2)
             milliseconds (format/leading-zeros (rem (-> n       math/floor) 1000) 3)]
            (case format :hhmmssmmm (str hours ":" minutes ":" seconds "." milliseconds)
                         :hhmmss    (str hours ":" minutes ":" seconds))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn timestamp-string->today?
  ; @param (string) n
  ;
  ; @return (boolean)
  [n]
  #?(:cljs (let [x (timestamp-string)]
                (= (string/part n 0 10)
                   (string/part x 0 10)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-year
  ; @return (integer)
  [])

(defn get-month
  ; @return (integer)
  [])

(defn get-day
  ; @return (integer)
  [])

(defn get-date
  ; @return (string)
  [])

(defn get-hours
  ; @return (integer)
  [])

(defn get-hours-left-from-this-day
  ; @return (integer)
  []
  (let [hours (get-hours)]
       (- 24 hours)))

(defn get-minutes
  ; @return (integer)
  [])

(defn get-minutes-left-from-this-hour
  ; @return (integer)
  []
  (let [minutes (get-minutes)]
       (- 60 minutes)))

(defn get-seconds
  ; @return (integer)
  [])

(defn get-seconds-left-from-this-minute
  ; @return (integer)
  []
  (let [seconds (get-seconds)]
       (- 60 seconds)))

(defn get-milliseconds
  ; @return (integer)
  [])

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
  ;  =>
  ;  (time/set-timeout!    0 #(my-function :a))
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
  ;  =>
  ;  #<DateTime 2020-04-20T00:00:00.000Z>
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
  ;  =>
  ;  #<DateTime 2020-04-20T16:20:00.123Z>
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
  ;  =>
  ;  "2020-04-20T16:20:00.123"
  ;
  ; @return (string)
  [n]
  (str n))



;; -- Parse / unparse date and time in data structures ------------------------
;; ----------------------------------------------------------------------------

(defn parse-date-time
  ; @param (*) n
  ;
  ; @example
  ;  (time/parse-date-time {:my-timestamp "2020-04-20T16:20:00.123Z"})
  ;  =>
  ;  {:my-timestamp #<DateTime 2020-04-20T16:20:00.123Z>}
  ;
  ; @return (*)
  [n]
  (cond (date-string?      n) (parse-date      n)
        (timestamp-string? n) (parse-timestamp n)
        (map?              n) (map/->values    n parse-date-time)
        (vector?           n) (vector/->items  n parse-date-time)
        :else                 (return          n)))

(defn unparse-date-time
  ; @param (*) n
  ;
  ; @example
  ;  (time/unparse-date-time {:my-timestamp #<DateTime 2020-04-20T16:20:00.123Z>})
  ;  =>
  ;  {:my-timestamp "2020-04-20T16:20:00.123Z"}
  ;
  ; @return (*)
  [n]
  (cond (timestamp-object? n) (unparse-timestamp n)
        (map?              n) (map/->values      n unparse-date-time)
        (vector?           n) (vector/->items    n unparse-date-time)
        :else                 (return            n)))
