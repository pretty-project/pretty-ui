
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

(defn kline-list-range-total-high
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

(defn kline-list-range-total-low
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vectors in map) kline-list
  ; @param (integer) from
  ; @param (integer) to
  ;
  ; @return (integer)
  [kline-list from to]
  (let [ranged-kline-list (vector/ranged-items kline-list from to)]
       (letfn [(f [o x] (min o (:low x)))]
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
  ; @return (integer)
  ;  A visszatérési érték egy integer, ami kifejezi, hogy hány periódus óta nem volt annyira
  ;  alacsony az ár, mint az utólsó periódus záró ára.
  [{:keys [kline-list]}]
  (let [count      (count  kline-list)
        last-close (get-in kline-list [(- count 1) :close])]
       (letfn [(f [lap]
                  ; Az f függvény az utolsó elemtől az első elem irányába haladva vizsgálja
                  ; a vektort (a lap érték növelésével egyre korábbi elemeket vizsgál)
                  (cond ; Ha elfogytak az elemek ...
                        (> lap count) (return nil)
                        ; Az első lefutáskor az utolsó elemet nincs értelme önmagával összehasonlítani ...
                        (= lap 1) (f (inc lap))
                        ; A minta értelmezéséhez, legalább három elemet szükséges megvizsgálni ...
                        (= lap 2) (f (inc lap))
                        ; Ha a vizsgált elem záró ára alacsonyabb, mint az utolsó periódus záró ára ...
                        ;(> last-close (get-in kline-list [(- count lap) :close])) (dec lap)
                        (> last-close (get-in kline-list [(- count lap) :close])) (return (str " length: "     (dec lap)
                                                                                               " last-close: " last-close
                                                                                               " lap-close: "  (get-in kline-list [(- count lap) :close])))
                        ; Ha a vizsgált elem záró ára NEM alacsonyabb, mint az utolsó periódus záró ára ...
                        :else (f (inc lap))))]
              (f 1))))

(defn valley-length
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (integer)
  ;  A visszatérési érték egy integer, ami kifejezi, hogy hány periódus óta nem volt annyira
  ;  magas az ár, mint az utólsó periódus záró ára.
  [{:keys [kline-list]}]
  (let [count      (count  kline-list)
        last-close (get-in kline-list [(- count 1) :close])]
       (letfn [(f [lap]
                  ; Az f függvény az utolsó elemtől az első elem irányába haladva vizsgálja
                  ; a vektort (a lap érték növelésével egyre korábbi elemeket vizsgál)
                  (cond ; Ha elfogytak az elemek ...
                        (> lap count) (return nil)
                        ; Az első lefutáskor az utolsó elemet nincs értelme önmagával összehasonlítani ...
                        (= lap 1) (f (inc lap))
                        ; A minta értelmezéséhez, legalább három elemet szükséges megvizsgálni ...
                        (= lap 2) (f (inc lap))
                        ; Ha a vizsgált elem záró ára magasabb, mint az utolsó periódus záró ára ...
                        (< last-close (get-in kline-list [(- count lap) :close])) (dec lap)
                        ; Ha a vizsgált elem záró ára NEM magasabb, mint az utolsó periódus záró ára ...
                        :else (f (inc lap))))]
              (f 1))))

(defn mountain-highness
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (USD)
  ;  Az utolsó periódus záró ára és a mountain legmagasabb értéke közötti különbség.
  [{:keys [kline-list] :as kline-data}]
  (if-let [mountain-length (mountain-length kline-data)]
          (if (> mountain-length 1)
              (let [count         (count kline-list)
                    highest-price (kline-list-range-total-high kline-list (- count mountain-length 1) (- count 1))
                    current-price (get-in kline-list [(- count 1) :close])]
                   (- highest-price current-price)))))

(defn valley-deepness
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (USD)
  ;  Az utolsó periódus záró ára és a valley legalacsonyabb értéke közötti különbség.
  [{:keys [kline-list] :as kline-data}]
  (if-let [valley-length (valley-length kline-data)]
          (if (> valley-length 1)
              (let [count         (count kline-list)
                    lowest-price  (kline-list-range-total-low kline-list (- count valley-length 1) (- count 1))
                    current-price (get-in kline-list [(- count 1) :close])]
                   (- current-price lowest-price)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
