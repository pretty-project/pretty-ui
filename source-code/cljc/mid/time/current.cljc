
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns time.current)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-year
  ; @return (integer)
  []
  #?(:cljs (.getFullYear (js/Date.))
     :clj  (-> (timestamp-object)
               (timestamp-object->year))))

(defn get-month
  ; @return (integer)
  [])

(defn get-day
  ; @return (integer)
  [])

(defn get-date
  ; @return (string)
  []
  #?(:cljs ()
     :clj  (-> (timestamp-string)
               (timestamp-string->date))))

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
