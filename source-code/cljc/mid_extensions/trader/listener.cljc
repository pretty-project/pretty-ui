
(ns mid-extensions.trader.listener
    (:require [mid-fruits.loop   :refer [reduce-indexed]]
              [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inc-from-lowest?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [kline-list]} test-count]
  ; - A megadott számú idősáv közül, az utolsó előtti zárt a legalacsonyabb árfolyamon
  ; - Az utolsó idősáv záró ára magasabb, mint az utolsó előtti időszak záró ára
  (let [test-kline-list   (vector/last-items kline-list test-count)
        second-last-close (get-in test-kline-list [(- test-count 2) :close])]
       (letfn [(test-f [{:keys [price-min] :as o} dex {:keys [close] :as x}]
                       (cond-> o ; Set initial price-min ...
                                 (nil? price-min) (assoc :price-min close)
                                 ; If price-min is higher than close ...
                                 (and (some? price-min) (> price-min close))
                                 (assoc :price-min close)
                                 ; If x is the second last ...
                                 (and (= dex (- test-count 2))
                                      ; And x is the lowest ...
                                      (< close price-min))
                                 (assoc :second-last-is-the-lowest? true)
                                 ; If x is the last ...
                                 (and (= dex (- test-count 1))
                                      ; And the last is higher than the second last ...
                                      (< second-last-close close))
                                 (assoc :last-is-higher-than-the-second-last? true)))]
              (let [result (reduce-indexed test-f {} test-kline-list)]
                   (boolean (and (:second-last-is-the-lowest?           result)
                                 (:last-is-higher-than-the-second-last? result)))))))

(defn inc-from-minimum?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [kline-list price-min]}]
  ; - Az összes idősáv közül, az utolsó előtti zárt a legalacsonyabb árfolyamon
  ; - Az utolsó idősáv záró ára magasabb, mint az utolsó előtti időszak záró ára
  (let [count       (count kline-list)
        second-last (nth   kline-list (- count 2))
        last        (nth   kline-list (- count 1))]
       (and (= (:close second-last) price-min)
            (< (:close second-last)
               (:close last)))))
