
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-dictionary.term-handler.side-effects
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn translated
  ; @param (map) term
  ; @param (map)(opt) options
  ;  {:prefix (string)(opt)
  ;   :replacements (numbers or strings in vector)(opt)
  ;    XXX#4509 (mid-fruits.string)
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (translated {:en "Apple" :hu "Alma"})
  ;  =>
  ;  "Apple"
  ;
  ; @return (string)
  [term options]
 @(r/subscribe [:dictionary/translate term options]))

(defn looked-up
  ; @param (keyword) term-id
  ; @param (map)(opt) options
  ;  {:prefix (string)(opt)
  ;   :replacements (numbers or strings in vector)(opt)
  ;    XXX#4509 (mid-fruits.string)
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (looked-up :my-term)
  ;  =>
  ;  "My term"
  ;
  ; @return (string)
  [term-id options]
 @(r/subscribe [:dictionary/look-up term-id options]))
