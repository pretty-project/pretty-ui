
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-dictionary.term-handler.engine
    (:require [x.server-core.api :as a]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def PROJECT-DICTIONARY-FILEPATH "monoset-environment/x.project-dictionary.edn")



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn looked-up
  ; @param (keyword) term-id
  ; @param (map) options
  ;  {:language-id (keyword)
  ;   :replacements (vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (dictionary/looked-up :my-term {:language-id :en})
  ;  =>
  ;  "My term"
  ;
  ; @return (string)
  [term-id options]
 @(a/subscribe [:dictionary/look-up term-id options]))
