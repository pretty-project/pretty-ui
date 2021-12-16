
(ns pathom.errors
    (:require [mid-fruits.random :as random]
              [mid-fruits.time   :as time]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-answer
  ; @param (keyword) error-code
  ;
  ; @usage
  ;  (pathom/error-answer :my-error-code)
  ;
  ; @return (map)
  ;  {:error/code (keyword)
  ;   :error/id (string)
  ;   :error/occured-at (string)}
  [error-code]
  {:error/code       error-code
   :error/id         (random/generate-string)
   :error/occured-at (time/timestamp-string)})
