
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-dictionary.side-effects
    (:require [server-fruits.io  :as io]
              [x.server-core.api :as a]
              [x.server-dictionary.term-handler.engine :as term-handler.engine]))



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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- import-project-dictionary!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (a/dispatch [:dictionary/add-terms! (io/read-edn-file term-handler.engine/PROJECT-DICTIONARY-FILEPATH)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :dictionary/import-project-dictionary! import-project-dictionary!)
