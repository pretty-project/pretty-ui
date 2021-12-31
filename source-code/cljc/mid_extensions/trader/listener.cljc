
(ns mid-extensions.trader.listener
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.logical :refer [or=]]
              [mid-fruits.loop    :refer [reduce-indexed]]
              [mid-fruits.vector  :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn kline-dropped?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [close open]}]
  (< close open))

(defn kline-increased?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [close open]}]
  (> close open))

(defn price-inc?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [kline-list]}]
  (let [count (count kline-list)]
       (> (get-in kline-list [(- count 1) :close])
          (get-in kline-list [(- count 2) :close]))))

(defn price-inc-from-minimum?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [kline-list price-min]}]
  ; - Az összes periódus közül, az utolsó előtti zárt a legalacsonyabb árfolyamon
  ; - Az utolsó periódus záró ára magasabb, mint az utolsó előtti periódus záró ára
  (let [count       (count kline-list)
        second-last (nth   kline-list (- count 2))
        last        (nth   kline-list (- count 1))]
       (and (= (:close second-last) price-min)
            (< (:close second-last)
               (:close last)))))

(defn drop-length
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [kline-list]}]
  ; - Ha az utolsó periódusban csökkent az ár, akkor a visszatérési érték false,
  ;   mert még nem fejeződött be a drop.
  ; - Ha az utólsó periódusban nőtt az ár, de az utolsó előtti periódusban nem csökkent,
  ;   akkor a visszatérési érték false, mert nem volt drop.
  ; - Ha az utólsó periódusban nő az ár, akkor a visszatérési érték egy integer,
  ;   ami kifejezi, hogy az utólsó előtti periódus zárásáig hány periódus telt el,
  ;   az előző legalacsonyabban záró periódus zárása óta.
  (let [count                (count  kline-list)
        last-increased?      (kline-increased? (nth kline-list (- count 1)))
        second-last-dropped? (kline-dropped?   (nth kline-list (- count 2)))
        second-last-close    (get-in kline-list [(- count 2) :close])]
       (println (str "second-last-dropped?: " second-last-dropped? " dex: " (- count 2)))
       (println (str "last-increased?: "           last-increased? " dex: " (- count 1)))
       (println (str "count: " count))
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
                  (reduce-indexed f nil kline-list)))))

(defn price-drop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [kline-list] :as kline-data}]
  ; Az utolsó előtti periódus záró értékéig mekkora volt az árfolyam csökkenés mértéke
  (if-let [drop-length (drop-length kline-data)]
          (let [count             (count kline-list)
                price-before-drop (get-in kline-list [(- count drop-length 1) :open])
                dropped-price     (get-in kline-list [(- count 1)             :open])]
               (- price-before-drop dropped-price))))
