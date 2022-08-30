
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.id
    (:require [mid-fruits.candy  :refer [return]]
              [mid-fruits.random :as random]
              [mid.re-frame.core :as core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector<-id-f
  ; @param (map) context
  ;
  ; @usage
  ;  (re-frame/event-vector<-id-f {...})
  ;
  ; @return (map)
  [context]
  (letfn [(f ; @param (vector) event-vector
             ;
             ; @example
             ;  (f [:my-event :my-id {...}])
             ;  =>
             ;  [:my-event :my-id {...}]
             ;
             ; @example
             ;  (f [:my-event {...}])
             ;  =>
             ;  [:my-event :0ce14671-e916-43ab-b057-0939329d4c1b {...}]
             ;
             ; @return (vector)
             [event-vector]
             (if (->     event-vector second keyword?)
                 (return event-vector)
                 (concat [(first event-vector) (random/generate-keyword)] (rest event-vector))))]
         (update-in context [:coeffects :event] f)))

; @constant (?)
(def event-vector<-id (core/->interceptor :id :core/event-vector<-id
                                          :before event-vector<-id-f))
