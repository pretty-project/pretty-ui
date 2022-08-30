
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns bybit.klines.sample
    (:require [bybit.api :as bybit]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-my-kline-list!
  []
  (bybit/request-kline-list! {:interval "1" :limit 60 :symbol "ETHUSD"}))
