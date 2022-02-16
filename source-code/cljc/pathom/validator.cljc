
(ns pathom.validator
    (:require [mid-fruits.candy :refer [param return]]
              [mid-fruits.time  :as time]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn validate-data
  ; @param (map) n
  ;
  ; @example
  ;  (pathom/validate-data {:my-key "My value"})
  ;  =>
  ;  {:my-key              "My value"
  ;   :pathom/validated-at "..."}
  ;
  ; @return (map)
  ;  {:pathom/validated-at (string)}
  [n]
  (if (map?   n)
      (assoc  n :pathom/validated-at (time/timestamp-string))
      (return n)))

(defn clean-validated-data
  ; @param (map) n
  ;  {:pathom/validated-at (string)(opt)}
  ;
  ; @example
  ;  (pathom/clean-validated-data {:my-key              "My value"
  ;                                :pathom/validated-at "..."})
  ;  =>
  ;  {:my-key "My value"}
  ;
  ; @return (map)
  [n]
  (if (map?   n)
      (dissoc n :pathom/validated-at)
      (return n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-valid?
  ; @param (map) n
  ;  {:pathom/validated-at (string)(opt)}
  ;
  ; @example
  ;  (pathom/data-valid? {:my-key "My value"})
  ;  =>
  ;  false
  ;
  ; @example
  ;  (pathom/data-valid? {:my-key              "My value"
  ;                       :pathom/validated-at "..."})
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (boolean (and (map? n)
                (get  n :pathom/validated-at))))

(defn data-invalid?
  ; @param (map) n
  ;  {:pathom/validated-at (string)(opt)}
  ;
  ; @example
  ;  (pathom/data-invalid? {:my-key "My value"})
  ;  =>
  ;  true
  ;
  ; @example
  ;  (pathom/data-invalid? {:my-key              "My value"
  ;                         :pathom/validated-at "..."})
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  (-> n data-valid? not))
