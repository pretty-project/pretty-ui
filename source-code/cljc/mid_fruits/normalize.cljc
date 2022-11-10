
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.normalize
    (:require [clojure.string :as string]
              #?(:cljs ["normalize-diacritics" :refer [normalize normalizeSync]])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn deaccent
  [text]
  "Remove accent from string"
  #?(:clj  (let [normalized (java.text.Normalizer/normalize text java.text.Normalizer$Form/NFD)]
                (string/replace normalized #"\p{InCombiningDiacriticalMarks}+" ""))
     :cljs  (normalizeSync text)))

(defn soft-cut-special-chars
  [text]
  "Cut special chars, but keep accent characters"
  (string/replace text #"[^a-zA-Z0-9\u00C0-\u017F\- ]" ""))

(defn cut-special-chars
  [text & [except]]
  "Cut special chars including accent characters"
  (let [pattern (re-pattern (str "[^\\w\\s" except "]"))]
       (string/replace text pattern "")))

(defn space->separator
  ; @param (string) n
  ;
  ; @example
  ;  (space->separator "a b  c")
  ;  =>
  ;  "a-b-c"
  ;
  ; @return (string)
  [n]
  "Changing ' ' to '-'. Keep one '-' if more than one are next to each other."
  (string/replace n #"[ |-]{1,}" "-"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clean-text
  ; @param (string) n
  ;
  ; @example
  ;  (clean-text "...")
  ;  =>
  ;  "..."
  ;
  ; @return (string)
  [n]
  (-> n (str)
        (deaccent)
        (cut-special-chars "-")
        (space->separator)
        (string/lower-case)))
