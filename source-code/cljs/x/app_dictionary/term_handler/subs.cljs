
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-dictionary.term-handler.subs
    (:require [mid-fruits.string                  :as string]
              [x.app-core.api                     :as a :refer [r]]
              [x.app-locales.api                  :as locales]
              [x.mid-dictionary.term-handler.subs :as term-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
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
  ;  (r dictionary/look-up :my-term)
  ;  =>
  ;  "My term"
  ;
  ; @example
  ;  (r dictionary/look-up :my-name-is-n {:replacements ["John"]})
  ;  =>
  ;  "My name is John"
  ;
  ; @return (string)
  [db [_ term-id {:keys [prefix replacements suffix]}]]
  (let [language-id     (r locales/get-selected-language db)
        translated-term (r get-term db term-id language-id)]
       (if replacements (string/use-replacements (str prefix translated-term suffix) replacements)
                        (str prefix translated-term suffix))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:dictionary/look-up! :my-term]
(a/reg-sub :dictionary/look-up look-up)
