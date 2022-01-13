
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.17
; Description:
; Version: v0.3.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.validator
    (:require [mid-fruits.candy :refer [param return]]
              [mid-fruits.time  :as time]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn validate-data
  ; @param (map) n
  ;
  ; @example
  ;  (validator/validate-data {:my-key "My value"})
  ;  =>
  ;  {:my-key                 "My value"
  ;   :validator/validated-at "..."}
  ;
  ; @return (map)
  ;  {:validator/validated-at (string)}
  [n]
  (if (map?   n)
      (assoc  n :validator/validated-at (time/timestamp-string))
      (return n)))

(defn data-valid?
  ; @param (map) n
  ;  {:validator/validated-at (string)(opt)}
  ;
  ; @example
  ;  (validator/data-valid? {:my-key "My value"})
  ;  =>
  ;  false
  ;
  ; @example
  ;  (validator/data-valid? {:my-key                 "My value"
  ;                          :validator/validated-at "..."})
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (boolean (and (map? n)
                (get  n :validator/validated-at))))

(defn data-invalid?
  ; @param (map) n
  ;  {:validator/validated-at (string)(opt)}
  ;
  ; @example
  ;  (validator/data-invalid? {:my-key "My value"})
  ;  =>
  ;  true
  ;
  ; @example
  ;  (validator/data-invalid? {:my-key                 "My value"
  ;                            :validator/validated-at "..."})
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  (-> n data-valid? not))

(defn clean-validated-data
  ; @param (map) n
  ;  {:validator/validated-at (string)(opt)}
  ;
  ; @example
  ;  (validator/clean-validated-data {:my-key                 "My value"
  ;                                   :validator/validated-at "..."})
  ;  =>
  ;  {:my-key "My value"}
  ;
  ; @return (map)
  [n]
  (if (map?   n)
      (dissoc n :validator/validated-at)
      (return n)))
