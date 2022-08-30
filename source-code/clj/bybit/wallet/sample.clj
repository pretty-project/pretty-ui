
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns bybit.wallet.sample
    (:require [bybit.api :as bybit]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-my-wallet-balance!
  []
  (bybit/request-wallet-balance! {:api-key "..." :api-secret "..."}))
