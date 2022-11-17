
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.dictionary.term-handler.helpers
    (:require [string.api :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn join-string
  ; @param (string) value
  ; @param (string) prefix
  ; @param (string) suffix
  ; @param (numbers or strings in vector) replacements
  ;
  ; @example
  ;  (join-string "Hi, my name is %!" nil nil ["John"])
  ;  =>
  ;  "Hy, my name is John!"
  ;
  ; @return (string)
  [value prefix suffix replacements]
  (if-not (empty? value)
          (if replacements (string/use-replacements (str prefix value suffix) replacements)
                           (str prefix value suffix))))
