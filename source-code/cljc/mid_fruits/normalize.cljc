
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.normalize
    (:require [clojure.string :refer [lower-case replace]]
              #?(:cljs ["normalize-diacritics" :refer [normalize normalizeSync]])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn deaccent
  [text]
  "Remove accent from string"
  #?(:clj  (let [normalized (java.text.Normalizer/normalize text java.text.Normalizer$Form/NFD)]
                (replace normalized #"\p{InCombiningDiacriticalMarks}+" ""))
     :cljs  (normalizeSync text)))

(defn soft-cut-special-chars
  [text]
  "Cut special chars, but keep accent characters"
  (replace text #"[^a-zA-Z0-9\u00C0-\u017F\- ]" ""))

(defn cut-special-chars
  [text & [except]]
  "Cut special chars including accent characters"
  (let [pattern (re-pattern (str "[^\\w\\s" except "]"))]
       (replace text pattern "")))

(defn space->separator
  [text]
  "Changing ' ' to '-'. Keep one '-' if more than one are next to each other."
  (replace text #"[ |-]{1,}" "-"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clean-url
  [text]
  (-> text (str)
           (deaccent)
           (cut-special-chars "-")
           (space->separator)
           (lower-case)))
