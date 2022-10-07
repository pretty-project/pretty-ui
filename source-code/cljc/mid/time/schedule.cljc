
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns time.schedule
    (:require #?(:clj [tea-time.core :as tea-time.core])))



;; -- aphyr/tea-time ----------------------------------------------------------
;; ----------------------------------------------------------------------------

;https://github.com/aphyr/tea-time
#?(:clj (tea-time.core/start!))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-timeout!
  ; @param (function) f
  ; @param (ms) timeout
  ;
  ; @usage
  ;  (time/set-timeout! #(println "3 sec") 3000)
  ;
  ; @return (tea_time.core.Once object)
  ; @return (integer)
  [f timeout]
  #?(:clj  (tea-time.core/after! (ms->s timeout) f)
     :cljs (.setTimeout js/window f timeout)))

(defn set-interval!
  ; @param (function) f
  ; @param (ms) interval
  ;
  ; @usage
  ;  (time/set-interval! #(println "3 sec") 3000)
  ;
  ; @return (tea_time.core.Every object)
  ; @return (integer)
  [f interval]
  #?(:clj  (tea-time.core/every! (ms->s interval) 0 f)
     :cljs (.setInterval js/window f interval)))

(defn clear-interval!
  ; @param (integer) interval-id
  ;
  ; @return (nil)
  ; @return (nil)
  [interval-id]
  #?(:clj  (return nil)
     :cljs (.clearInterval js/window interval-id)))
