
(ns mid.extensions.trader.patterns
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.logical :refer [or=]]
              [mid-fruits.loop    :refer [reduce-indexed]]
              [mid-fruits.vector  :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn kline-list-range-details
  ; @param (vectors in map) kline-list
  ; @param (integer) from
  ; @param (integer) to
  ;
  ; @return (map)
  ;  {:close (USD)
  ;   :high (USD)
  ;   :low (USD)
  ;   :open (USD)}
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
  ; @param (vectors in map) kline-list
  ; @param (integer) from
  ; @param (integer) to
  ;
  ; @return (USD)
  [kline-list from to]
  (let [ranged-kline-list (vector/ranged-items kline-list from to)]
       (letfn [(f [o x] (max o (:high x)))]
              ; A :high értékek összehasonlításának kezdőértéke: 0 ...
              (reduce f 0 ranged-kline-list))))

(defn kline-list-range-total-low
  ; @param (vectors in map) kline-list
  ; @param (integer) from
  ; @param (integer) to
  ;
  ; @return (USD)
  [kline-list from to]
  (let [ranged-kline-list (vector/ranged-items kline-list from to)
        first-low         (get-in kline-list [from :low])]
       (letfn [(f [o x] (min o (:low x)))]
              ; A :low értékek összehasonlításának kezdőértéke az első periódus :low értéke ...
              (reduce f first-low ranged-kline-list))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn kline-dropped?
  ; @param (map) n
  ;
  ; @return (boolean)
  [{:keys [close open]}]
  (< close open))

(defn kline-increased?
  ; @param (map) n
  ;
  ; @return (boolean)
  [{:keys [close open]}]
  (> close open))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn price-inc?
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (boolean)
  [{:keys [kline-list]}]
  (let [count (count kline-list)]
       (> (get-in kline-list [(- count 1) :close])
          (get-in kline-list [(- count 2) :close]))))

(defn price-inc-from-minimum?
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
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (period)
  ; - A visszatérési érték egy integer, ami kifejezi, hogy hány periódus óta nem volt annyira
  ;   alacsony az ár, mint az utólsó periódus záró ára.
  ; - A comparator függvények futtatása miatt szükséges, hogy a visszatérési érték minden esetben
  ;   integer legyen!
  [{:keys [kline-list]}]
  (let [count      (count  kline-list)
        last-close (get-in kline-list [(- count 1) :close])]
       (letfn [(f [lap]
                  ; Az f függvény az utolsó periódustól az első periódus irányába haladva vizsgálja
                  ; a vektort (a lap érték növelésével egyre korábbi periódusokat vizsgál)
                  (cond ; Ha elfogytak az periódusok ...
                        (> lap count)
                        ; *
                        (return 0)
                        ; Az első lefutáskor az utolsó periódust nincs értelme önmagával összehasonlítani ...
                        (= lap 1)
                        ; *
                        (f (inc lap))
                        ; Ha a vizsgált periódus záró ára alacsonyabb, mint az utolsó periódus záró ára ...
                        (> last-close (get-in kline-list [(- count lap) :close]))
                        ; *
                        (dec lap)
                        ; Ha a vizsgált periódus záró ára NEM alacsonyabb, mint az utolsó periódus záró ára ...
                        :else
                        ; ... akkor megvizsgálja a következő periódust.
                        (f (inc lap))))]
              (f 1))))

(defn valley-length
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (period)
  ; - A visszatérési érték egy integer, ami kifejezi, hogy hány periódus óta nem volt annyira
  ;   magas az ár, mint az utólsó periódus záró ára.
  ; - A comparator függvények futtatása miatt szükséges, hogy a visszatérési érték minden esetben
  ;   integer legyen!
  [{:keys [kline-list]}]
  (let [count      (count  kline-list)
        last-close (get-in kline-list [(- count 1) :close])]
       (letfn [(f [lap]
                  ; Az f függvény az utolsó periódustól az első periódus irányába haladva vizsgálja
                  ; a vektort (a lap érték növelésével egyre korábbi periódusokat vizsgál)
                  (cond ; Ha elfogytak a periódusok ...
                        (> lap count)
                        ; *
                        (return 0)
                        ; Az első lefutáskor az utolsó periódust nincs értelme önmagával összehasonlítani ...
                        (= lap 1)
                        ; *
                        (f (inc lap))
                        ; Ha a vizsgált periódus záró ára magasabb, mint az utolsó periódus záró ára ...
                        (< last-close (get-in kline-list [(- count lap) :close]))
                        ; *
                        (dec lap)
                        ; Ha a vizsgált periódus záró ára NEM magasabb, mint az utolsó periódus záró ára ...
                        :else
                        ; ... akkor megvizsgálja a következő periódust.
                        (f (inc lap))))]
              (f 1))))

(defn mountain-highness
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (USD)
  ; - Az utolsó periódus záró ára és a mountain legmagasabb értéke közötti különbség.
  ; - A comparator függvények futtatása miatt szükséges, hogy a visszatérési érték minden esetben
  ;   integer legyen!
  [{:keys [kline-list] :as kline-data}]
  (if-let [mountain-length (mountain-length kline-data)]
          (if (> mountain-length 1)
              (let [count         (count kline-list)
                    highest-price (kline-list-range-total-high kline-list (- count mountain-length 1) (- count 1))
                    current-price (get-in kline-list [(- count 1) :close])]
                   (- highest-price current-price))
              (return 0))
          (return 0)))

(defn valley-deepness
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (USD)
  ; - Az utolsó periódus záró ára és a valley legalacsonyabb értéke közötti különbség.
  ; - A comparator függvények futtatása miatt szükséges, hogy a visszatérési érték minden esetben
  ;   integer legyen!
  [{:keys [kline-list] :as kline-data}]
  (if-let [valley-length (valley-length kline-data)]
          (if (> valley-length 1)
              (let [count         (count kline-list)
                    lowest-price  (kline-list-range-total-low kline-list (- count valley-length 1) (- count 1))
                    current-price (get-in kline-list [(- count 1) :close])]
                   (- current-price lowest-price))
              (return 0))
          (return 0)))



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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn rising-length
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (period)
  ; "What happens in a function, stays in that function!"
  [{:keys [kline-list]}]
  (let [count (count kline-list)]
       (letfn [(f [lap]
                  ; Az f függvény az utolsó periódustól az első periódus irányába haladva vizsgálja
                  ; a vektort (a lap érték növelésével egyre korábbi periódusokat vizsgál)
                  (cond ; Ha elfogytak a periódusok ...
                        (> lap count)
                        ; *
                        (return 0)
                        ; Ha a vizsgált periódusban NEM emelkedett az ár ...
                        (not (kline-increased? (get kline-list (- count lap))))
                        ; ... akkor az előző vizsgált periódus volt az utolsó, amiben emelkedett az ár.
                        (return (dec lap))
                        ; Ha a vizsgált periódusban emelkedett az ár ...
                        :else
                        ; ... akkor megvizsgálja a következő periódust.
                        (f (inc lap))))]
              (f 1))))

(defn falling-length
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (period)
  [{:keys [kline-list]}]
  (let [count (count kline-list)]
       (letfn [(f [lap]
                  ; Az f függvény az utolsó periódustól az első periódus irányába haladva vizsgálja
                  ; a vektort (a lap érték növelésével egyre korábbi periódusokat vizsgál)
                  (cond ; Ha elfogytak a periódusok ...
                        (> lap count)
                        ; *
                        (return 0)
                        ; Ha a vizsgált periódusban NEM csökkent az ár ...
                        (not (kline-dropped? (get kline-list (- count lap))))
                        ; ... akkor az előző vizsgált periódus volt az utolsó, amiben csökkent az ár.
                        (return (dec lap))
                        ; Ha a vizsgált periódusban csökkent az ár ...
                        :else
                        ; ... akkor megvizsgálja a következő periódust.
                        (f (inc lap))))]
              (f 1))))

(defn rising-highness
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (USD)
  ; - Az aktuális egymás után következő növekvő periódusok közül az első nyitó ára és az utolsó
  ;   periódus záró ára közötti különbség.
  ; - A comparator függvények futtatása miatt szükséges, hogy a visszatérési érték minden esetben
  ;   integer legyen!
  [{:keys [kline-list] :as kline-data}]
  (if-let [rising-length (rising-length kline-data)]
          (if (> rising-length 1)
              (let [count         (count kline-list)
                    open-price    (get-in kline-list [(- count rising-length) :open])
                    current-price (get-in kline-list [(- count 1) :close])]
                   (- current-price open-price))
              (return 0))
          (return 0)))

(defn falling-deepness
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (USD)
  ; - Az aktuális egymás után következő csökken periódusok közül az első nyitó ára és az utolsó
  ;   periódus záró ára közötti különbség.
  ; - A comparator függvények futtatása miatt szükséges, hogy a visszatérési érték minden esetben
  ;   integer legyen!
  [{:keys [kline-list] :as kline-data}]
  (if-let [falling-length (falling-length kline-data)]
          (if (> falling-length 1)
              (let [count         (count kline-list)
                    open-price    (get-in kline-list [(- count falling-length) :open])
                    current-price (get-in kline-list [(- count 1) :close])]
                   (- open-price current-price))
              (return 0))
          (return 0)))
