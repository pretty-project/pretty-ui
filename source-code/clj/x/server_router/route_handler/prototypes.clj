
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler.prototypes
    (:require [mid-fruits.candy :refer [param return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handler-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (function or map) handler
  ; @param (map) options
  ;  {:restricted? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:handler (function)}
  [handler {:keys [restricted?]}]
  (if restricted? (cond (fn?  handler) (return {:handler (route-handler.engine/route-authenticator           handler)})
                        (map? handler) (assoc   :handler (route-handler.engine/route-authenticator (:handler handler))))
                  (cond (fn?  handler) (return {:handler handler})
                        (map? handler) (return handler))))

(defn route-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) route-props
  ;  {:get (function or map)(opt)
  ;   :post (function or map)(opt)
  ;   :restricted? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:get (map)
  ;   :core-js (string)
  ;   :post (map)}
  [{:keys [get post restricted?] :as route-props}]
  (merge {:core-js route-handler.config/DEFAULT-CORE-JS}
         (param route-props)
         (if get  {:get  (handler-prototype get  {:restricted? restricted?})})
         (if post {:post (handler-prototype post {:restricted? restricted?})})))
