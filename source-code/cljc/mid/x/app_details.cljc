
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.app-details)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def app-codename    "x4")
(def app-description "Are we alone in the universe?")
(def app-version     "4.8.7")



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copyright-label
  ; @param (integer or string) current-year
  ;
  ; @usage
  ;  (time/copyright-label 2022)
  ;
  ; @return (string)
  [current-year]
  (str current-year" Monotech.hu"))

(defn copyright-information
  ; @param (integer or string) current-year
  ;
  ; @usage
  ;  (time/copyright-information 2022)
  ;
  ; @return (string)
  [current-year]
  (str "2020-"current-year" Monotech.hu - All rights reserved"))
