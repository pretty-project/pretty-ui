
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.engine
    (:require [mid-fruits.candy    :refer [param return]]
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
  (letfn [(f [o k v] (if-let [route-template (get v :route-template)]
                             (conj   o [route-template k])
                             (return o)))]
         (reduce-kv f [] routes)))
