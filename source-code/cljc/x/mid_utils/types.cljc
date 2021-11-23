
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.29
; Description:
; Version: v0.5.8
; Compatibility: x4.1.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-utils.types
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
              [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-list?
  ; @param (*) n
  ;  
  ; @example
  ;  (element-list? [[{:element-name :text-field} {}] [...]])
  ;  => true
  ;
  ; @return (boolean)
  [n]
  ; TODO Use SPEC!
  (boolean (and (vector/nonempty? n)
                (let [first-item (first n)]
                     (and (vector/nonempty? (param  first-item))
                          (map/nonempty?    (first  first-item))
                          (map/nonempty?    (second first-item))
                          (keyword?         (:element-name first-item)))))))

(defn polarity-props?
  ; @param (*) n
  ;  
  ; @example
  ;  (polarity-props? {:start-items [[{:element-name :text-field} {}] [...]]})
  ;  => true
  ;
  ; @return (boolean)
  [n]
  ; TODO Use SPEC!
  (boolean (and (map/nonempty? n)
                (or (element-list? (:start-items  n))
                    (element-list? (:middle-items n))
                    (element-list? (:end-items    n))))))

(defn content-map?
  ; @param (*) n
  ;  
  ; @example
  ;  (content-map? {:content #'my-content}
  ;  => true
  ;
  ; @return (boolean)
  [n]
  ; TODO Use SPEC!
  (boolean (and (map/nonempty? n)
                (some? (:content n)))))
