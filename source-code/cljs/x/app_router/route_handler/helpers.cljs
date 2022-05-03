
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.helpers
    (:require [mid-fruits.candy                   :refer [param return]]
              [x.mid-router.route-handler.helpers :as route-handler.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-router.route-handler.helpers
(def route-conflict?                   route-handler.helpers/route-conflict?)
(def destructed-routes->ordered-routes route-handler.helpers/destructed-routes->ordered-routes)
(def variable-route-string?            route-handler.helpers/variable-route-string?)
(def resolve-variable-route-string     route-handler.helpers/resolve-variable-route-string)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-props->route-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;  {:route-template (string)}
  ;
  ; @example
  ;  (route-handler.helpers/route-props->route-data :my-route {:route-template "/my-route"})
  ;  =>
  ;  ["/my-route" :my-route]
  ;
  ; @return (vector)
  ;  [(string) route-template
  ;   (string) route-id]
  [route-id {:keys [route-template]}]
  [route-template route-id])

(defn routes->destructed-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) routes
  ;
  ; @example
  ;  (route-handler.helpers/routes->destructed-routes {:my-route   {:route-template "/my-route"}
  ;                                                    :your-route {:route-template "/your-route"}})
  ;  =>
  ;  [["/my-route"   :my-route]]
  ;   ["/your-route" :your-route]]
  ;
  ; @return (vectors in vector)
  [routes]
  (letfn [(f [destructed-routes route-id {:keys [route-template] :as route-props}]
             (if (route-conflict? destructed-routes route-template)
                 (let []
                   (println (str "r-conflict: " route-template))
                  (return          destructed-routes))
                 (let [route-data (route-props->route-data route-id route-props)]
                      (conj destructed-routes route-data))))]
         (reduce-kv f [] routes)))
