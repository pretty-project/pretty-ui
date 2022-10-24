
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-dictionary.term-handler.subs
    (:require [mid-fruits.candy                   :refer [param return]]
              [mid-fruits.string                  :as string]
              [re-frame.api                       :as r :refer [r]]
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
  ; @param (map) options
  ;  {:language (keyword)
  ;   :replacements (vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (r look-up :my-term! {:language :en})
  ;  =>
  ;  "My term"
  ;
  ; @example
  ;  (r look-up :my-name-is-n {:language :en :replacements ["John"]})
  ;  =>
  ;  "My name is John"
  ;
  ; @return (string)
  [db [_ term-id {:keys [language replacements suffix]}]]
  (let [translated-term (r get-term db term-id language)
        suffixed-term   (str translated-term suffix)]
       (if replacements (string/use-replacements suffixed-term replacements)
                        (return                  suffixed-term))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:dictionary/look-up :my-term {:language :en}]
(r/reg-sub :dictionary/look-up look-up)
