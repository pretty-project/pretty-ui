
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.helpers
    (:require [mid-fruits.candy                   :refer [param return]]
              [x.mid-router.route-handler.helpers :as route-handler.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-router.route-handler.helpers
(def variable-route-string?        route-handler.helpers/variable-route-string?)
(def resolve-variable-route-string route-handler.helpers/resolve-variable-route-string)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn routes->router-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) routes
  ;
  ; @example
  ;  (route-handler.helpers/routes->router-routes {:my-route   {:route-template "/my-route"}
  ;                                                :your-route {:route-template "/my-app/your-route"}})
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
