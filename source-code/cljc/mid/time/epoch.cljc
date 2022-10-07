
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns time.epoch
    (:require [time.converters :as converters]
              #?(:clj [clj-time.coerce :as clj-time.coerce])
              #?(:clj [clj-time.core   :as clj-time.core])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn epoch-ms
  ; @return (ms)
  []
  #?(:clj (-> (clj-time.core/now)
              (clj-time.coerce/to-long))))

(defn epoch-s
  ; @return (s)
  []
  #?(:clj (-> (clj-time.core/now)
              (clj-time.coerce/to-long)
              (quot 1000))))

(defn epoch-ms->timestamp-string
  ; @param (ms) n
  ;
  ; @example
  ;  (time/epoch-ms->timestamp-string 1640800860000)
  ;  =>
  ;  "2021-12-29T18:01:00.000Z"
  ;
  ; @return (string)
  [n]
  #?(:clj (if n (-> n clj-time.coerce/from-long str))))

(defn epoch-s->timestamp-string
  ; @param (s) n
  ;
  ; @example
  ;  (time/epoch-s->timestamp-string 1640800860)
  ;  =>
  ;  "2021-12-29T18:01:00.000Z"
  ;
  ; @return (string)
  [n]
  #?(:clj (if n (-> n converters/s->ms clj-time.coerce/from-long str))))
