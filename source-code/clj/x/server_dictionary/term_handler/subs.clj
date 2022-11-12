
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-dictionary.term-handler.subs
    (:require [candy.api                                :refer [return]]
              [mid-fruits.string                        :as string]
              [mid.x.dictionary.term-handler.subs       :as term-handler.subs]
              [re-frame.api                             :as r :refer [r]]
              [x.server-dictionary.term-handler.helpers :as term-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.dictionary.term-handler.subs
(def get-term     term-handler.subs/get-term)
(def term-exists? term-handler.subs/term-exists?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn translate
  ; @param (keyword) term-id
  ; @param (map) options
  ;  {:language (keyword)
  ;   :prefix (string)(opt)
  ;   :replacements (numbers or strings in vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (r translate {:en "Apple" :hu "Alma"} {:language :en})
  ;  =>
  ;  "Apple"
  ;
  ; @return (string)
  [db [_ term {:keys [language prefix replacements suffix]}]]
  (let [translated-term (term language)]
       (term-handler.helpers/join-string translated-term prefix suffix replacements)))

(defn look-up
  ; @param (keyword) term-id
  ; @param (map) options
  ;  {:language (keyword)
  ;   :prefix (string)(opt)
  ;   :replacements (numbers or strings in vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (r look-up :my-term {:language :en})
  ;  =>
  ;  "My term"
  ;
  ; @example
  ;  (r look-up :my-name-is-n {:language :en :replacements ["John"]})
  ;  =>
  ;  "My name is John"
  ;
  ; @return (string)
  [db [_ term-id {:keys [language prefix replacements suffix]}]]
  (let [translated-term (r get-term db term-id language)]
       (term-handler.helpers/join-string translated-term prefix suffix replacements)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:dictionary/translate {:en "Apple" :hu "Alma"} {:language :en}]
(r/reg-sub :dictionary/translate translate)

; @usage
;  [:dictionary/look-up :my-term {:language :en}]
(r/reg-sub :dictionary/look-up look-up)
