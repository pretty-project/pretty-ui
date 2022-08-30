
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns bybit.klines.errors
    (:require [bybit.klines.helpers :as klines.helpers]
              [mid-fruits.candy     :refer [return]]
              [mid-fruits.loop      :refer [some-indexed]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn kline-list-data->time-error
  ; @param (map) kline-list-data
  ;  {:kline-list (maps in vector)}
  ; @param (map) options
  ;
  ; @usage
  ;  (bybit/kline-list-data->time-error {...} {...})
  ;
  ; @return (namespaced keyword)
  ;  :time-error/different-intervals, :time-error/time-slippage
  [{:keys [kline-list]} _]
  (letfn [(f [dex x]
             (cond ; Az első elemet nincs mivel összehasonlítani ...
                   (= dex 0) (return nil)
                   ; Ha az interval értéke nem egyezik meg az előző elem interval értékével ...
                   (not= (get-in kline-list [(dec dex) :interval])
                         (:interval x))
                   (return :time-error/different-intervals)
                   ; Ha az open-time értéke nem egyenlő az előző elem open-time értékének
                   ; és a periódus hosszának összegével ...
                   (not= (+ (get-in kline-list [(dec dex) :open-time])
                            (klines.helpers/interval-duration (:interval x)))
                         (:open-time x))
                   (return :time-error/time-slippage)))]
         (some-indexed f kline-list)))

(defn kline-list-data->limit-error
  ; @param (map) kline-list-data
  ;  {:kline-list (maps in vector)}
  ; @param (map) options
  ;  {:limit (integer)}
  ;
  ; @usage
  ;  (bybit/kline-list-data->limit-error {...} {...})
  ;
  ; @return (namespaced keyword)
  ;  :limit-error/too-few-kline, :limit-error/too-many-kline
  [{:keys [kline-list]} {:keys [limit]}]
  (cond (> limit (count kline-list)) (return :limit-error/too-few-kline)
        (< limit (count kline-list)) (return :limit-error/too-many-kline)))

(defn kline-list-data->error
  ; @param (map) kline-list-data
  ;  {:kline-list (maps in vector)}
  ; @param (map) options
  ;  {:limit (integer)}
  ;
  ; @usage
  ;  (bybit/kline-list-data->error {...} {...})
  ;
  ; @return (namespaced keyword)
  [kline-list-data options]
  (or (kline-list-data->time-error  kline-list-data options)
      (kline-list-data->limit-error kline-list-data options)))

(defn kline-list-data<-error
  ; @param (map) kline-list-data
  ;  {:kline-list (maps in vector)}
  ; @param (map) options
  ;  {:limit (integer)}
  ;
  ; @usage
  ;  (bybit/kline-list-data<-error {...} {...})
  ;
  ; @return (map)
  ;  {:error (namespaced keyword)}
  [kline-list-data options]
  (if-let [error (kline-list-data->error kline-list-data options)]
          (assoc  kline-list-data :error error)
          (return kline-list-data)))
