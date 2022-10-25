
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

(defn looked-up
  ; @param (keyword) term-id
  ; @param (map)(opt) options
  ;  {:replacements (vector)(opt)
  ;    XXX#4509
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
