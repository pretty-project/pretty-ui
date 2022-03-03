
(ns mid-extensions.trader.klines
    (:require [mid-extensions.trader.engine :as engine]
              [mid-fruits.candy             :refer [param return]]
              [mid-fruits.keyword           :as keyword]
              [mid-fruits.loop              :refer [some-indexed]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn kline-data-repetition-error
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A kline-data-repetition-error függvény megvizsgálja, hogy a kline-list elemei között,
  ; van-e olyan elem, amelynek key paraméterként átadott kulcshoz tartozó értéke megegyezik
  ; a kline-list előző öt elemének ugyanazon tulajdonságával (egymás utáni 6-szoros ismétlődés)
  ;
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (namespaced keyword)
  [{:keys [kline-list] :as kline-data} key]
  (letfn [(test-f [dex lap]
                  ; A test-f függvény megvizsgálja, hogy a kline-list dex sorszámú elemének key
                  ; tulajdonsága megegyezik-e a kline-list előző öt elemének key tulajdonságával
                  (cond ; Ha dex és (dex - lap) sorszámú elem key tulajdonsága nem egyezik meg ...
                        (not= (get-in kline-list [(- dex 0)   key])
                              (get-in kline-list [(- dex lap) key]))
                        (return false)
                        ; Ha dex és (dex - lap) sorszámú elem key tulajdonsága megegyezik ...
                        (< lap 5) (test-f dex (inc lap))
                        ; Ha a lap értéke nagyobb, mint 5, akkor az eddigi iterációkban megegyeztek
                        ; az értékek ...
                        :else (return true)))
          (f [dex]
             (cond ; A test-f függvény lefutásához a dex értékének nagyobbnak kell lennie, mint 5!
                   (< dex 5) (f (inc dex))
                   ; Ha a dex értéke nagyobb, mint a sorozat utolsó elemének sorszáma, akkor
                   ; az iteráció nem talált 6-szoros ismétlődést ...
                   (= dex (count kline-list)) (return nil)
                   ; Ha a dex értéke megfelelő a test-f függvény lefutásához
                   :else (if (test-f dex 1)
                             ; Ha a test-f függvény visszatérési értéke true, akkor az iteráció
                             ; talált 6-szoros ismétlődést ...
                             (keyword/add-namespace :repetition-error key)
                             ; Ha a test-f függvény visszatérési értéke false, akkor megviszgálja
                             ; a vektor következő elemét ...
                             (f (inc dex)))))]
         ; WARNING#6070
         (f 0)))

(defn kline-data-time-error
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (namespaced keyword)
  [{:keys [kline-list]}]
  (letfn [(f [dex x]
             (cond ; Az első elemet nincs mivel összehasonlítani ...
                   (= dex 0) (return nil)
                   ; Ha az interval értéke nem egyezik meg az előző elem interval értékével ...
                   (not= (get-in kline-list [(dec dex) :interval])
                         (:interval x))
                   (return :time-error/different-intervals)
                   ; Ha az open-time értéke nem egyenlő az előző elem open-time értékének
                   ; és a periódus hosszának összegével ...
                   (not= (+ (get-in kline-list [(dec dex) :open-time])
                            (engine/interval-duration (:interval x)))
                         (:open-time x))
                   (return :time-error/time-slippage)))]
         (some-indexed f kline-list)))

(defn kline-data-limit-error
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ; @param (map) options
  ;  {:limit (integer)}
  ;
  ; @return (namespaced keyword)
  [{:keys [kline-list]} {:keys [limit]}]
  (cond (> limit (count kline-list)) (return :limit-error/too-few-kline)
        (< limit (count kline-list)) (return :limit-error/too-many-kline)))

(defn kline-data-error
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ; @param (map) options
  ;  {:limit (integer)}
  ;
  ; @return (namespaced keyword)
  [kline-data options]
  (or (kline-data-time-error  kline-data)
      (kline-data-limit-error kline-data options)))
      ;(kline-data-repetition-error kline-data :open)
      ;(kline-data-repetition-error kline-data :close)
      ;(kline-data-repetition-error kline-data :high)
      ;(kline-data-repetition-error kline-data :low)
