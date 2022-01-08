
(ns server-extensions.trader.patterns
    (:require [mid-extensions.trader.patterns :as patterns]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-extensions.trader.patterns
(def kline-list-range-details    patterns/kline-list-range-details)
(def kline-list-range-total-high patterns/kline-list-range-total-high)
(def kline-list-range-total-low  patterns/kline-list-range-total-low)
(def kline-dropped?              patterns/kline-dropped?)
(def kline-increased?            patterns/kline-increased?)
(def price-inc?                  patterns/price-inc?)
(def price-inc-from-minimum?     patterns/price-inc-from-minimum?)
(def mountain-length             patterns/mountain-length)
(def valley-length               patterns/valley-length)
(def mountain-highness           patterns/mountain-highness)
(def valley-deepness             patterns/valley-deepness)
(def rising-length               patterns/rising-length)
(def falling-length              patterns/falling-length)
(def rising-highness             patterns/rising-highness)
(def falling-deepness            patterns/falling-deepness)
