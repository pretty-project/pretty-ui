
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns bybit.account.uri
    (:require [bybit.request.sign :as request.sign]
              [bybit.uri.config   :as uri.config]
              [time.api           :as time]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn account-api-key-uri
  ; @param (map) uri-props
  ;  {:api-key (string)
  ;   :api-secret (string)
  ;   :use-mainnet? (boolean)(opt)
  ;    Default: false}
  ;
  ; @example
  ;  (bybit/account-api-key-uri {:api-key "..." :api-secret "..."})
  ;  =>
  ; https://api-testnet.bybit.com/v2/private/account/api-key?api_key={api_key}&timestamp={timestamp}&sign={sign}
  ;
  ; @example
  ;  (bybit/account-api-key-uri {:api-key "..." :api-secret "..." :use-mainnet? true})
  ;  =>
  ; https://api.bybit.com/v2/private/account/api-key?api_key={api_key}&timestamp={timestamp}&sign={sign}
  ;
  ; @return (string)
  [{:keys [api-key api-secret use-mainnet?]}]
  ; Please make sure that your timestamp is in sync with our (bybit.com) server time.
  ; You can use the Server Time endpoint.
  (let [timestamp    (time/epoch-ms)
        query-string (str "api_key=" api-key "&timestamp=" timestamp)
        address      (if use-mainnet? uri.config/PRIVATE-API-ADDRESS uri.config/PRIVATE-TEST-API-ADDRESS)]
       (str address "/account/api-key?" (request.sign/signed-query-string query-string api-secret))))
