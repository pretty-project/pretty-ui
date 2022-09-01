
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.sub
    (:require [mid.re-frame.core :as core]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.re-frame.core
(def subscribe core/subscribe)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn subscribed
  ; @param (subscription-vector) subscriber
  ;
  ; @usage
  ;  (re-frame/subscribed [:my-subscription])
  ;
  ; @return (*)
  [subscriber]
  (-> subscriber subscribe deref))
