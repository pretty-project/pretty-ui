
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.lifecycle-handler.engine
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.random :as random]))



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(def LIFES (atom {}))



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
