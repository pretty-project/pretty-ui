
(ns server-extensions.trader.engine
    (:require [server-fruits.hash :as hash]
              [mid-extensions.trader.engine :as engine]))



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
(def query-kline-uri         engine/query-kline-uri)
(def api-key-info-uri        engine/api-key-info-uri)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn signed-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uri api-secret]
  (let [sign (hash/sha256 uri api-secret)]
       (str uri "&sign=" sign)))
