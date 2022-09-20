
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-dictionary.term-handler.side-effects
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn looked-up
  ; @param (keyword) term-id
  ; @param (map) options
  ;  {:language-id (keyword)
  ;   :replacements (vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (dictionary/looked-up :my-term {:language-id :en})
  ;  =>
  ;  "My term"
  ;
  ; @return (string)
  [term-id options]
 @(a/subscribe [:dictionary/look-up term-id options]))
