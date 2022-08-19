
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.lifecycle-handler.helpers
    (:require [mid-fruits.candy  :refer [return]]
              [mid-fruits.random :as random]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn generate-life-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (namespaced keyword)
  []
  (keyword (random/generate-string) "lifecycles"))

(defn life-id->namespace
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword or namespaced keyword) life-id
  ;
  ; @return (string)
  [life-id]
  (if-let [namespace (namespace life-id)]
          (return namespace)
          (random/generate-string)))
