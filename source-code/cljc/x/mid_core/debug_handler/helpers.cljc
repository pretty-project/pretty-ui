
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.debug-handler.helpers
    (:require [mid-fruits.string               :as string]
              [mid-fruits.vector               :as vector]
              [x.mid-core.debug-handler.config :as debug-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn query-string->debug-mode
  ; @param (string) query-string
  ;
  ; @return (string)
  [query-string]
  (vector/first-filtered debug-handler.config/SAFEWORDS #(string/contains-part? query-string %)))
