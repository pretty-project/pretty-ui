
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.gestures
    (:require [mid-fruits.loop   :refer [do-while]]
              [mid-fruits.mixed  :as mixed]
              [mid-fruits.regex  :refer [re-match?]]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ordered-label?
  ; @param (string) n
  ;
  ; @usage
  ;  (ordered-label? "My item #3")
  ;
  ; @return (boolean)
  [n]
  (re-match? n #".*\#\d$"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-label->copy-label
  ; Az original-label paraméterként átadott címkét ellátja a suffix paraméterként
  ; átadott toldalékkal. Névütközés esetén sorszámmal látja el a címkét.
  ;
  ; @param (string) item-label
  ; @param (strings in vector)(opt) concurent-labels
  ;
  ; @example
  ;  (item-label->copy-label "My item" ["Your item" "Their item"])
  ;  =>
  ;  "My item #2"
  ;
  ; @example
  ;  (item-label->copy-label "My item" ["My item" "My item #2"])
  ;  =>
  ;  "My item #3"
  ;
  ; @example
  ;  (item-label->copy-label "My item #2" ["Your item"])
  ;  =>
  ;  "My item #3"
  ;
  ; @return (string)
  ([item-label]
   (item-label->copy-label item-label []))

  ([item-label concurent-labels]
   (letfn [(test-f [n] (not (vector/contains-item? concurent-labels n)))
           (f      [n] (if (ordered-label? n)
                           (let [copy-dex      (string/after-last-occurence  n "#" {:return? false})
                                 label-base    (string/before-last-occurence n "#" {:return? true})
                                 next-copy-dex (mixed/update-whole-number copy-dex inc)]
                                (str label-base "#" next-copy-dex))
                           (str n " #2")))]
          (do-while f item-label test-f))))
