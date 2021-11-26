
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.03
; Description:
; Version: v0.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.gestures
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.loop   :refer [do-while]]
              [mid-fruits.mixed  :as mixed]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- suffix->trail-base
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) suffix
  ;
  ; @example
  ;  (gestures/suffix->trail-base "copy")
  ;  =>
  ;  " copy "
  ;
  ; @return (string)
  [suffix]
  (str " " suffix " "))

(defn- label->label-base
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) n
  ; @param (string) suffix
  ;
  ; @example
  ;  (gestures/label->label-base "My item" "copy")
  ;  =>
  ;  "My item"
  ;
  ; @example
  ;  (gestures/label->label-base "My item copy" "copy")
  ;  =>
  ;  "My item"
  ;
  ; @example
  ;  (gestures/label->label-base "My item copy 2" "copy")
  ;  =>
  ;  "My item"
  ;
  ; WARNING!
  ;  A függvény nem vizsgálja, hogy a trail-base után következő szöveg
  ;  valóban csak számot tartalmaz!
  ; @example
  ;  (gestures/label->label-base "My item copy 2 xyz" "copy")
  ;  =>
  ;  "My item"
  ;
  ; @return (string)
  [n suffix]
  (let [trail-base (str " " suffix)]
       (if (string/contains-part?        n trail-base)
           (string/before-last-occurence n trail-base)
           (return n))))

(defn- label->nth-copy-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) n
  ; @param (string) suffix
  ;
  ; @example
  ;  (gestures/label->nth-copy-dex "My item copy" "copy")
  ;  =>
  ;  ""
  ;
  ; @example
  ;  (gestures/label->nth-copy-dex "My item copy 2" "copy")
  ;  =>
  ;  "2"
  ;
  ; WARNING!
  ;  A függvény nem vizsgálja, hogy a trail-base után következő szöveg
  ;  valóban csak számot tartalmaz!
  ; @example
  ;  (gestures/label->nth-copy-dex "My item copy 2 xyz" "copy")
  ;  =>
  ;  "2 xyz"
  ;
  ; @return (string)
  [n suffix]
  (let [trail-base (suffix->trail-base suffix)]
       (string/after-last-occurence n trail-base)))

(defn- label->first-copy-label?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) n
  ; @param (string) suffix
  ;
  ; @example
  ;  (gestures/label->first-copy-label? "My item" "copy")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (gestures/label->first-copy-label? "My item copy" "copy")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (gestures/label->first-copy-label? "My item copy 2" "copy")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n suffix]
  (string/ends-with? (param n)
                     (str " " suffix)))

(defn- label->nth-copy-label?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) n
  ; @param (string) suffix
  ;
  ; @example
  ;  (gestures/label->nth-copy-label? "My item" "copy")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (gestures/label->nth-copy-label? "My item copy" "copy")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (gestures/label->nth-copy-label? "My item copy 2" "copy")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n suffix]
  (let [trail-base (suffix->trail-base suffix)]
       (boolean (if-let [trail-end (string/after-last-occurence n trail-base)]
                        (mixed/mixed->number? trail-end)))))

(defn- label->next-copy-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) n
  ; @param (string) suffix
  ;
  ; @example
  ;  (gestures/label->next-copy-label "My item" "copy")
  ;  =>
  ;  "My item copy"
  ;
  ; @example
  ;  (gestures/label->next-copy-label "My item copy" "copy")
  ;  =>
  ;  "My item copy 2"
  ;
  ; @example
  ;  (gestures/label->next-copy-label "My item copy 2" "copy")
  ;  =>
  ;  "My item copy 3"
  ;
  ; @return (string)
  [n suffix]
        ; "My item copy" => "My item copy 2"
  (cond (label->first-copy-label? n suffix)
        (str n " 2")

        ; "My item copy 2" => "My item copy 3"
        (label->nth-copy-label?   n suffix)
        (let [copy-dex      (label->nth-copy-dex        n suffix)
              next-copy-dex (mixed/mixed->update-number copy-dex inc)
              label-base    (label->label-base          n suffix)]
             (str label-base " " suffix " " next-copy-dex))

        ; "My item" => "My item copy"
        :else (str n " " suffix)))

(defn item-label->duplicated-item-label
  ; Az original-label paraméterként átadott címkét ellátja a suffix paraméterként
  ; átadott toldalékkal. Névütközés esetén sorszámmal látja el a címkét.
  ;
  ; @param (string) original-label
  ; @param (strings in vector)(opt) concurent-labels
  ; @param (string) suffix
  ;
  ; @example
  ;  (gestures/item-label->duplicated-item-label "My item"
  ;                                              ["Your item" "Their item"]
  ;                                              "copy")
  ;  =>
  ;  "My item copy"
  ;
  ; @example
  ;  (gestures/item-label->duplicated-item-label "My item"
  ;                                              ["My item copy"]
  ;                                              "copy")
  ;  =>
  ;  "My item copy 2"
  ;
  ; @example
  ;  (gestures/item-label->duplicated-item-label "My item copy"
  ;                                              ["Your item"]
  ;                                              "copy")
  ;  =>
  ;  "My item copy 2"
  ;
  ; @return (string)
  ([original-label suffix]
   (item-label->duplicated-item-label original-label [] suffix))

  ([original-label concurent-labels suffix]
   (do-while (fn [%] (label->next-copy-label % suffix))
             (param original-label)
             (fn [%] (not (vector/contains-item? concurent-labels %))))))
