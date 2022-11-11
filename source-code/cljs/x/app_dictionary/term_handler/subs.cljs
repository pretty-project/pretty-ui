
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-dictionary.term-handler.subs
    (:require [mid.x.dictionary.term-handler.subs    :as term-handler.subs]
              [re-frame.api                          :as r :refer [r]]
              [x.app-dictionary.term-handler.helpers :as term-handler.helpers]
              [x.app-locales.api                     :as x.locales]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.dictionary.term-handler.subs
(def get-term     term-handler.subs/get-term)
(def term-exists? term-handler.subs/term-exists?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn translate
  ; @param (map) term
  ; @param (map)(opt) options
  ;  {:prefix (string)(opt)
  ;   :replacements (numbers or strings in vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (r translate db {:en "Apple" :hu "Alma"})
  ;  =>
  ;  "Apple"
  ;
  ; @return (string)
  [db [_ term {:keys [prefix replacements suffix]}]]
  (let [language        (r x.locales/get-selected-language db)
        translated-term (get term language)]
       (term-handler.helpers/join-string translated-term prefix suffix replacements)))

(defn look-up
  ; @param (keyword) term-id
  ; @param (map)(opt) options
  ;  {:prefix (string)(opt)
  ;   :replacements (numbers or strings in vector)(opt)
  ;    XXX#4509 
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (r look-up :my-term)
  ;  =>
  ;  "My term"
  ;
  ; @example
  ;  (r look-up :my-name-is-n {:replacements ["John"]})
  ;  =>
  ;  "My name is John"
  ;
  ; @return (string)
  [db [_ term-id {:keys [prefix replacements suffix]}]]
  (let [language        (r x.locales/get-selected-language db)
        translated-term (r get-term db term-id language)]
       (term-handler.helpers/join-string translated-term prefix suffix replacements)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:dictionary/translate {:en "Apple" :hu "Alma"}]
(r/reg-sub :dictionary/translate translate)

; @usage
;  [:dictionary/look-up :my-term]
(r/reg-sub :dictionary/look-up look-up)
