
(ns mid-extensions.trader.patterns
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.logical :refer [or=]]
              [mid-fruits.loop    :refer [reduce-indexed]]
              [mid-fruits.vector  :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn kline-list-range-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vectors in map) kline-list
  ; @param (integer) from
  ; @param (integer) to
  ;
  ; @return (map)
  ;  {:close (integer)
  ;   :high (integer)
  ;   :low (integer)
  ;   :open (integer)}
  [kline-list from to]
  (let [ranged-kline-list (vector/ranged-items kline-list from to)]
       (letfn [(f [{:keys [high low close open]} dex x]
                  {:high  (max high (:high x))
                   :low   (min low  (:low  x))
                   :open  (or  open (:open x))
                   :close (if (vector/dex-last? ranged-kline-list dex)
                              (:close x))})]
              (reduce-indexed f {} ranged-kline-list))))

(defn kline-list-range-high
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vectors in map) kline-list
  ; @param (integer) from
  ; @param (integer) to
  ;
  ; @return (integer)
  [kline-list from to]
  (let [ranged-kline-list (vector/ranged-items kline-list from to)]
       (letfn [(f [o x] (max o (:high x)))]
              (reduce f 0 ranged-kline-list))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn kline-dropped?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) n
  ;
  ; @return (boolean)
  [{:keys [close open]}]
  (< close open))

(defn kline-increased?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) n
  ;
  ; @return (boolean)
  [{:keys [close open]}]
  (> close open))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn price-inc?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (boolean)
  [{:keys [kline-list]}]
  (let [count (count kline-list)]
       (> (get-in kline-list [(- count 1) :close])
          (get-in kline-list [(- count 2) :close]))))

(defn price-inc-from-minimum?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)
  ;   :total-low (integer)}
  ;
  ; @return (boolean)
  [{:keys [kline-list total-low]}]
  ; - Az összes periódus közül, az utolsó előtti zárt a legalacsonyabb árfolyamon
  ; - Az utolsó periódus záró ára magasabb, mint az utolsó előtti periódus záró ára
  (let [count       (count kline-list)
        second-last (nth   kline-list (- count 2))
        last        (nth   kline-list (- count 1))]
       (and (= (:close second-last) total-low)
            (< (:close second-last)
               (:close last)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mountain-length
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (boolean or integer)
  [{:keys [kline-list]}]
  ; - Ha az utolsó periódusban csökkent az ár, akkor a visszatérési érték false,
  ;   mert még nem fejeződött be a csökkenés.
  ; - Ha az utólsó periódusban nőtt az ár, de az utolsó előtti periódusban nem csökkent,
  ;   akkor a visszatérési érték false, mert nem volt csökkenés.
  ; - Ha az utólsó periódusban nő az ár, akkor a visszatérési érték egy integer,
  ;   ami kifejezi, hogy hány periódus óta nem volt annyira alacsony az ár, mint az
  ;   utólsó előtti periódus záró ára.
  (let [count                (count  kline-list)
        last-increased?      (kline-increased? (nth kline-list (- count 1)))
        second-last-dropped? (kline-dropped?   (nth kline-list (- count 2)))
        second-last-close    (get-in kline-list [(- count 2) :close])]
;       (println "")
;       (println "drop-length ...")
;       (println "last-increased? " last-increased?)
;       (println "last range:" (str (:open  (nth kline-list (- count 1))))
;                "-"           (str (:close (nth kline-list (- count 1))))]
;       (println "second-last-dropped? " second-last-dropped?)
;       (println "second-last range:" (str (:open  (nth kline-list (- count 2))))
;                "-"                  (str (:close (nth kline-list (- count 2))))]
;       (println (str (mid-fruits.time/timestamp-string)))
;       (println "")
       (if ; If price is NOT increasing from a drop ...
           (or (not last-increased?) (not second-last-dropped?))
           (return false)
           ; If price is increasing ...
           (letfn [(f [o dex {:keys [close]}]
                      (cond ; If x is the last or the second last ...
                            ; ... then it is unnecessary to evaluate the test ...
                            (or= dex (- count 1) (- count 2))
                            ; If x is the last or the second last, but o is nil, ...
                            ; ... then returns 0.
                            (or o 0)
                            ; If x close is NOT higher than the second last close ...
                            ; ... then the second last is NOT the lowest yet.
                            (<= close second-last-close) (return nil)
                            ; If x close is higher than the second last close,
                            ; and the result is nil ...
                            ; ... then returns the distance from the second last.
                            (nil? o) (- count 2 dex)
                            ; If x close is higher than the second last close,
                            ; and the result is NOT nil ...
                            ; ... then returns the result.
                            :else (return o)))]
                  (let [mountain-length (reduce-indexed f nil kline-list)]
                       (if (>      mountain-length 1)
                           (return mountain-length)
                           (return false)))))))

(defn mountain-highness
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (USD)
  [{:keys [kline-list] :as kline-data}]
  ; - Az utolsó előtti periódus záró értéke és mountain legmagasabb értéke közötti különbség
  (if-let [mountain-length (mountain-length kline-data)]
          (if (> mountain-length 1)
              (let [count         (count  kline-list)
                    highest-price (kline-list-range-high kline-list (- count mountain-length 1) (- count 2))
                    dropped-price (get-in kline-list [(- count 1) :open])]
                   (- highest-price dropped-price)))))


(defn price-bouncing-uwd?
  ; Price bouncing upward?
  [])

(defn price-bouncing-dwd?
  ; Price bouncing downward?
  [])

(defn price-bouncing-fwd?
  ; Price bouncing forward?
  [])

(defn trend-direction
  ; Az utobbi interval hosszuságu idöben mi a trend iránya
  [kline-data interval])
