
(ns app-extensions.trader.engine
    (:require [mid-extensions.trader.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-extensions.trader.engine
(def PUBLIC-API-ADDRESS      engine/PUBLIC-API-ADDRESS)
(def PUBLIC-TEST-API-ADDRESS engine/PUBLIC-TEST-API-ADDRESS)
(def PRIVATE-API-ADDRESS     engine/PRIVATE-API-ADDRESS)
(def interval-duration       engine/interval-duration)
(def close-time              engine/close-time)
(def query-duration          engine/query-duration)
(def query-from              engine/query-from)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (maps in vector)
(def INTERVAL-OPTIONS [{:label "1 minute"    :value "1"}
                       {:label "3 minutes"   :value "3"}
                       {:label "5 minutes"   :value "5"}
                       {:label "15 minutes"  :value "15"}
                       {:label "30 minutes"  :value "30"}
                       {:label "60 minutes"  :value "60"}
                       {:label "120 minutes" :value "120"}
                       {:label "240 minutes" :value "240"}
                       {:label "360 minutes" :value "360"}
                       {:label "720 minutes" :value "720"}
                       {:label "1 day"       :value "D"}
                       {:label "1 week"      :value "W"}
                       {:label "1 month"     :value "M"}])

; @constant (maps in vector)
(def SYMBOL-OPTIONS [{:label "ETH / USD" :value "ETHUSD"}
                     {:label "BTC / USD" :value "BTCUSD"}])
