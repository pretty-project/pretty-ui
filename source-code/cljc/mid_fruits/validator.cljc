
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.17
; Description:
; Version: v0.3.0
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.validator
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
              [mid-fruits.time   :as time]
              [mid-fruits.vector :as vector]))



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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn validate-data-structure
  ; @param (map or vector) n
  ;
  ; @example
  ;  (validator/validate-data-structure {:my-map {:my-key "My value"}})
  ;  =>
  ;  {:my-map {:my-key                 "My value"
  ;            :validator/validated-at "..."}}
  ;
  ; @example
  ;  (validator/validate-data-structure [{:my-key "My value"}])
  ;  =>
  ;  [{:my-key                 "My value"
  ;    :validator/validated-at "..."}]
  ;
  ; @return (map or vector)
  [n])
  ; TODO ...

(defn data-structure-valid?
  ; @param (map or vector) n
  ;
  ; @example
  ;  (validator/data-structure-valid? {:my-map {:my-key                 "My value"
  ;                                             :validator/validated-at "..."}})
  ;  =>
  ;  true
  ;
  ; @example
  ;  (validator/data-structure-valid? [{:my-key                 "My value"
  ;                                     :validator/validated-at "..."}])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (cond (map?    n) (map/all-values-match?   n data-valid?)
        (vector? n) (vector/all-items-match? n data-valid?)
        :else       (return false)))

(defn data-structure-invalid?
  ; @param (map or vector) n
  ;
  ; @example
  ;  (validator/data-structure-invalid? {:my-map {:my-key                 "My value"
  ;                                               :validator/validated-at "..."}})
  ;  =>
  ;  false
  ;
  ; @example
  ;  (validator/data-structure-invalid? [{:my-key                 "My value"
  ;                                       :validator/validated-at "..."}])
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  (cond (map?    n) (map/any-value-match?   n data-invalid?)
        (vector? n) (vector/any-item-match? n data-invalid?)
        :else       (return true)))

(defn clean-validated-data-structure
  ; @param (map) n
  ;  {:validator/validated-at (string)(opt)}
  ;
  ; @example
  ;  (validator/clean-validated-data-structure {:my-map {:my-key                 "My value"
  ;                                                      :validator/validated-at "..."}})
  ;  =>
  ;  {:my-map {:my-key "My value"}}
  ;
  ; @example
  ;  (validator/clean-validated-data-structure [{:my-key                 "My value"
  ;                                              :validator/validated-at "..."}])
  ;  =>
  ;  [{:my-key "My value"}]
  ;
  ; @return (map)
  [n]
  (cond (map?    n) (reduce-kv #(assoc %1 %2 (clean-validated-data %3)) {} n)
        (vector? n) (reduce    #(conj  %1    (clean-validated-data %2)) [] n)
        :else       (return false)))
