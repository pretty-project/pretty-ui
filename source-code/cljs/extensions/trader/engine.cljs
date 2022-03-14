
(ns extensions.trader.engine)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (maps in vector)
(def SYNC-TIMEOUT-OPTIONS [{:label "2 sec"  :value 2000}
                           {:label "5 sec"  :value 5000}
                           {:label "15 sec" :value 15000}
                           {:label "30 sec" :value 30000}
                           {:label "60 sec" :value 60000}])

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
