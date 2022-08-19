
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.api
    (:require [re-frame.core :as core]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; re-frame.core
(def ->interceptor re-frame.core/->interceptor)
(def reg-cofx      re-frame.core/reg-cofx)
(def reg-sub       re-frame.core/reg-sub)
(def subscribe     re-frame.core/subscribe)
(def inject-cofx   re-frame.core/inject-cofx)
