
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.11
; Description:
; Version: v0.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.random
    (:require [mid-fruits.candy :refer [param]]))



;; -- Untyped generators ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn generate-boolean
  ; @usage
  ;  (random/generate-boolean)
  ;
  ; @param (boolean)
  []
  (= (rand-int 2)
     (param    0)))



;; -- Typed generators --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn generate-uuid
  ; @usage
  ;  (random/generate-uuid)
  ;
  ; @return (string)
  []
  #?(:cljs (str (random-uuid))
     :clj  (str (java.util.UUID/randomUUID))))

(defn generate-string
  ; @usage
  ;  (random/generate-string)
  ;
  ; @return (string)
  []
  (generate-uuid))

(defn generate-keyword
  ; @param (string)(opt) namespace
  ;
  ; @usage
  ;  (random/generate-keyword)
  ;
  ; @return (keyword)
  ([]
   (keyword (generate-uuid)))

  ([namespace]
   (keyword (str namespace "/" (generate-uuid)))))

(defn generate-namespaced-keyword
  ; @return (namespaced keyword)
  []
  (keyword (str (generate-uuid) "/" (generate-uuid))))

(defn generate-react-key
  ; @usage
  ;  (random/generate-react-key)
  ;
  ; @return (string)
  []
  (generate-uuid))
