
(ns server-extensions.trader.patterns
    (:require [mid-extensions.trader.patterns :as patterns]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-extensions.trader.patterns
(def kline-data-inconsistent? patterns/kline-data-inconsistent?)
(def kline-dropped?           patterns/kline-dropped?)
(def kline-increased?         patterns/kline-increased?)
(def price-inc?               patterns/price-inc?)
(def price-inc-from-minimum?  patterns/price-inc-from-minimum?)
(def mountain-length          patterns/mountain-length)
(def mountain-highness        patterns/mountain-highness)
