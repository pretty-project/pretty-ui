
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns bybit.account.sample
    (:require [bybit.api :as bybit]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-my-account-api-key!
  []
  (bybit/request-account-api-key! {:api-key "..." :api-secret "..."}))
