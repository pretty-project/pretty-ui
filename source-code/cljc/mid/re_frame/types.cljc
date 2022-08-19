

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.types
    (:require [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector?
  ; @param (*) n
  ;
  ; @example
  ;  (re-frame/event-vector? [:my-event ...])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (and (-> n vector?)
       (-> n first keyword?)))

(defn subscription-vector?
  ; @param (*) n
  ;
  ; @example
  ;  (re-frame/subscription-vector? [:my-namespace/get-something ...])
  ;  =>
  ;  true
  ;
  ; @example
  ;  (re-frame/subscription-vector? [:my-namespace/something-happened? ...])
  ;  =>
  ;  true
  ;
  ; @example
  ;  (re-frame/subscription-vector? [:div ...])
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  (and (-> n vector?)
       (and (-> n first keyword?)
            (or (-> n first name (string/starts-with? "get-"))
                (-> n first name (string/ends-with?   "?"))))))
