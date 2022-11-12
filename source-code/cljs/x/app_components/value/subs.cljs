
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.value.subs
    (:require [candy.api            :refer [return]]
              [re-frame.api         :as r :refer [r]]
              [x.app-dictionary.api :as x.dictionary]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-metamorphic-value
  ; @param (metamorphic-content) value
  ;  {:content (keyword, hiccup, integer, number or string)
  ;   :prefix (string)(opt)
  ;   :replacements (numbers or strings in vector)(opt)
  ;   :suffix (string)(opt)}
  ;
  ; @usage
  ;  (r get-metamorphic-value db {...})
  ;
  ; @example
  ;  (r get-metamorphic-value db {:content :username})
  ;  =>
  ;  "Username"
  ;
  ; @example (string)
  ;  (r get-metamorphic-value db {:content "Hakuna Matata"})
  ;  =>
  ;  "Hakuna Matata"
  ;
  ; @example (string)
  ;  (r get-metamorphic-value db "Hakuna Matata")
  ;  =>
  ;  "Hakuna Matata"
  ;
  ; @return (string)
  [db [_ {:keys [content prefix replacements suffix] :as value}]]
  (cond (-> value map? not)   (r get-metamorphic-value db {:content value})
        (-> content string?)  (x.dictionary/join-string content prefix suffix replacements)
        (-> content keyword?) (let [content (r x.dictionary/look-up db content)
                                    value   (assoc value :content content)]
                                   (r get-metamorphic-value db value))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:components/get-metamorphic-value ...]
(r/reg-sub :components/get-metamorphic-value get-metamorphic-value)
