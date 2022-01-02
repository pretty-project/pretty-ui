
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

; @constant (map)
(def DEFAULT-SYMBOL {:label "ETH / USD" :value "ETHUSD"})

; @constant (maps in vector)
(def ELAPSED-TIME-OPTIONS [{:label "20 minutes"  :value 20}
                           {:label "40 minutes"  :value 40}
                           {:label "60 minutes"  :value 60}
                           {:label "90 minutes"  :value 90}
                           {:label "120 minutes" :value 120}
                           {:label "180 minutes" :value 180}
                           {:label "240 minutes" :value 240}
                           {:label "300 minutes" :value 300}
                           {:label "360 minutes" :value 360}
                           {:label "420 minutes" :value 420}
                           {:label "480 minutes" :value 480}])

; @constant (maps in vector)
(def MOVEMENT-OPTIONS [{:label "rising" :value :rising} {:label "falling" :value :falling}])

; @constant (maps in vector)
(def  DIRECTION-OPTIONS [{:label "at least" :value :at-least} {:label "at most" :value :at-most}])

; @constant (maps in vector)
(def PATTERN-OPTIONS [{:label "Rocky Mountains" :value :rocky-mountains}
                      {:label "Grand Canyon"   :value :grand-canyon}])

; @constant (maps in vector)
(def PATTERN-VOLUME-OPTIONS [{:label "20 USD"  :value 20}
                             {:label "40 USD"  :value 40}
                             {:label "60 USD"  :value 60}
                             {:label "80 USD"  :value 80}
                             {:label "100 USD" :value 100}
                             {:label "120 USD" :value 120}])
