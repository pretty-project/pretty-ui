
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-dictionary.term-handler.engine
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn looked-up
  ; @param (keyword) term-id
  ; @param (map)(opt) options
  ;  {:replacements (vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (dictionary/looked-up :my-term)
  ;  =>
  ;  "My term"
  ;
  ; @return (string)
  [term-id options]
 @(a/subscribe [:dictionary/look-up term-id options]))
