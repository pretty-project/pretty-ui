
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.log
    (:require [mid-fruits.candy          :refer [return]]
              [mid.re-frame.context      :as context]
              [mid.re-frame.interceptors :as interceptors]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (?)
(def LOG-EVENT! (interceptors/->interceptor :id :re-frame/log-event!
                                            :before #(let [event-vector (context/context->event-vector %1)]
                                                         ; Szükséges korlátozni a fájl maximális méretét!
                                                         ;(write events to log file ...)
                                                          (return %1))))
