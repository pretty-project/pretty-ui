
(ns mongo-db.date-time
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.time   :as time]
              [mid-fruits.vector :as vector]))



;; -- Parse / unparse date and time in data structures ------------------------
;; ----------------------------------------------------------------------------

(defn parse-date-time
  ; @param (*) n
  ;
  ; @example
  ;  (time/parse-date-time {:my-timestamp "2020-04-20T16:20:00.123Z"})
  ;  => {:my-timestamp #<DateTime 2020-04-20T16:20:00.123Z>}
  ;
  ; @return (*)
  [n]
  (letfn [(reduce-map
            ; @param (map) n
            ;
            ; @return (map)
            [n]
            (reduce-kv (fn [result k v]
                           (let [v (parse-date-time v)]
                                (assoc result k v)))
                       (param {})
                       (param n)))

          (reduce-vector
            ; @param (vector) n
            ;
            ; @return (vector)
            [n]
            (reduce (fn [result x]
                        (let [x (parse-date-time x)]
                             (vector/conj-item result x)))
                    (param [])
                    (param n)))]

         ; parse-date-time
         (cond (time/date-string?      n) (time/parse-date      n)
               (time/timestamp-string? n) (time/parse-timestamp n)
               (map?                   n) (reduce-map           n)
               (vector?                n) (reduce-vector        n)
               :else                      (return               n))))

(defn unparse-date-time
  ; @param (*) n
  ;
  ; @example
  ;  (time/unparse-date-time {:my-timestamp #<DateTime 2020-04-20T16:20:00.123Z>})
  ;  => {:my-timestamp "2020-04-20T16:20:00.123Z"}
  ;
  ; @return (*)
  [n]
  (letfn [(reduce-map
            ; @param (map) n
            ;
            ; @return (map)
            [n]
            (reduce-kv (fn [result k v]
                           (let [v (unparse-date-time v)]
                                (assoc result k v)))
                       (param {})
                       (param n)))

          (reduce-vector
            ; @param (vector) n
            ;
            ; @return (vector)
            [n]
            (reduce (fn [result x]
                        (let [x (unparse-date-time x)]
                             (vector/conj-item result x)))
                    (param [])
                    (param n)))]

         ; unparse-date-time
         (cond (time/timestamp-object? n) (time/unparse-timestamp n)
               (map?                   n) (reduce-map             n)
               (vector?                n) (reduce-vector          n)
               :else                      (return                 n))))
