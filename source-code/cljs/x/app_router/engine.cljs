
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.17
; Description:
; Version: v0.2.6
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.engine
    (:require [mid-fruits.candy    :refer [param return]]
              [mid-fruits.string   :as string]
              [mid-fruits.vector   :as vector]
              [x.mid-router.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-router.engine
(def variable-route-string?        engine/variable-route-string?)
(def resolve-variable-route-string engine/resolve-variable-route-string)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn routes->router-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) routes
  ;
  ; @example
  ;  (engine/routes->router-routes {:my-route   {:route-template "/my-route"}
  ;                                 :your-route {:route-template "/my-app/your-route"}})
  ;  =>
  ;  [["/my-route"          :my-route]
  ;   ["/my-app/your-route" :your-route]]
  ;
  ; @return (vector)
  [routes]
  (reduce-kv #(if-let [route-template (get %3 :route-template)]
                      (vector/conj-item %1 [route-template %2])
                      (return           %1))
              (param [])
              (param routes)))
