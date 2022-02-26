
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.trader.sample
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.format :as format]
              [x.server-core.api :as a]
              [server-extensions.trader.klines   :as klines]
              [server-extensions.trader.patterns :as patterns]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (m)
(def MINIMUM-MOUNTAIN-DURATION 120)

; @constant (USD)
(def MINIMUM-MOUNTAIN-HIGHNESS 20)

; @constant (USD)
(def MINIMUM-VOLUME 0)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- resolve-market!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [kline-data]
  (if-let [mountain-length (patterns/mountain-length kline-data)]
          (let [mountain-duration (* mountain-length 3)
                mountain-highness (patterns/mountain-highness kline-data)]
               (println "mountain-length:"   mountain-length   "periods")
               (println "mountain-duration:" mountain-duration "minutes")
               (println "mountain-highness:" mountain-highness "USD")
               (if (patterns/price-inc-from-minimum? kline-data)
                   ; Nincs elegendő adat a kline-datában, kellenek régebbi periodusok is!
                   ; Ha price-inc-from-minimum, akkor ne nyisson poziciot, mert nagy a zuhanas
                   (do (println "PRICE INC FROM MINIMUM WARNING!")
                       (a/dispatch [:trader/log! "PRICE INC FROM MINIMUM WARNING!" {:warning? true}])))
               (if (and (>= mountain-highness MINIMUM-MOUNTAIN-HIGHNESS)
                        (>= mountain-duration MINIMUM-MOUNTAIN-DURATION))
                   (let [_ (println "Limits OK")
                         mountain-highness (format/decimals mountain-highness 2)
                         message           (str "ETH / USD price is rising after at least of " mountain-duration
                                                " minutes long and " mountain-highness " USD high Rocky Mountains period")]
                        (println "mountain-highness:" mountain-highness "USD")
                        (println "message:"           message)
                        (a/dispatch [:trader/log! message {:warning? true}])
                        {:mountain-duration mountain-duration
                         :mountain-highness mountain-highness})
                   (let [] ; TEMP
                        (println "Limits NOT OK")
                        (a/dispatch [:trader/log! (str "mountain-highness tul kicsi: " mountain-highness " " mountain-duration) {:warning? true}])
                        (return false))))))
