
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.15
; Description:
; Version: v0.3.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.random  :as random]))




;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn id
  ; @param (keyword or nil)(opt) id
  ;
  ; @return (keyword)
  ([]   (random/generate-keyword))
  ([id] (if (keyword? id)
            (return   id)
            (random/generate-keyword))))

(defn prot
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; @param (function) prototype-f
  ;
  ; @usage
  ;  (defn- my-prototype-f [my-props] (do-something-with my-props))
  ;  (let [my-props (a/prot my-props my-prototype-f)])
  ;
  ; @usage
  ;  (defn- my-prototype-f [my-id my-props] (r do-something-with db my-id my-props))
  ;  (let [my-props (a/prot my-id my-props my-prototype-f)])
  ;
  ; @return (map)
  ([props prototype-f]
   (prot nil props prototype-f))

  ([id props prototype-f]
   (if (some? id)
       (prototype-f id props)
       (prototype-f props))))

(defn sub-prot
  ; @param (map) context
  ; @param (vector) params
  ; @param (function) prototype-f
  ;
  ; @usage
  ;  (defn- my-prototype-f [db [my-id]] (r do-something-with db my-id))
  ;  (let [my-props (a/sub-prot db [my-id] my-prototype-f)])
  ;
  ; @usage
  ;  (defn- my-prototype-f [db [my-id my-props]] (r do-something-with db my-id my-props))
  ;  (let [my-props (a/sub-prot db [my-id my-props] my-prototype-f)])
  ;
  ; @return (map)
  [context params prototype-f]
  (prototype-f context params))

(defn get-namespace
  ; @param (namespaced keyword) sample
  ;
  ; @return (keyword)
  [sample]
  (keyword/get-namespace sample))
