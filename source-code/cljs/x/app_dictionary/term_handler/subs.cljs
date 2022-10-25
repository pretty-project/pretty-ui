
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-dictionary.term-handler.subs
    (:require [mid-fruits.string                  :as string]
              [re-frame.api                       :as r :refer [r]]
              [x.app-locales.api                  :as locales]
              [x.mid-dictionary.term-handler.subs :as term-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-dictionary.term-handler.subs
(def get-term     term-handler.subs/get-term)
(def term-exists? term-handler.subs/term-exists?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn look-up
  ; @param (keyword) term-id
  ; @param (map)(opt) options
  ;  {:prefix (string)(opt)
  ;   :replacements (vector)(opt)
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
  (let [language        (r locales/get-selected-language db)
        translated-term (r get-term db term-id language)]
       (if replacements (string/use-replacements (str prefix translated-term suffix) replacements)
                        (str prefix translated-term suffix))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:dictionary/look-up! :my-term]
(r/reg-sub :dictionary/look-up look-up)
