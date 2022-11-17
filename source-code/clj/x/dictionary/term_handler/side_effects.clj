
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.dictionary.term-handler.side-effects
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn translated
  ; @param (map) term
  ; @param (map) options
  ;  {:language (keyword)
  ;   :prefix (string)(opt)
  ;   :replacements (numbers or strings in vector)(opt)
  ;    XXX#4509 (string-api/source-code/cljc/string/replace.cljc)
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (translated {:en "Apple" :hu "Alma"} {:language :en})
  ;  =>
  ;  "Apple"
  ;
  ; @return (string)
  [term options]
 @(r/subscribe [:x.dictionary/translate term options]))

(defn looked-up
  ; @param (keyword) term-id
  ; @param (map) options
  ;  {:language (keyword)
  ;   :prefix (string)(opt)
  ;   :replacements (numbers or strings in vector)(opt)
  ;    XXX#4509 (string-api/source-code/cljc/string/replace.cljc)
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (looked-up :my-term {:language :en})
  ;  =>
  ;  "My term"
  ;
  ; @return (string)
  [term-id options]
 @(r/subscribe [:x.dictionary/look-up term-id options]))
