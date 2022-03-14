
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.default-handler.events
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-default-route!
  ; https://github.com/metosin/reitit/blob/master/doc/ring/default_handler.md
  ;
  ; @param (keyword) route-id
  ;  :method-not-allowed, :not-acceptable, :not-found
  ; @param (map) route-props
  ;  {:body (*)
  ;   :status (integer)}
  ;
  ; @usage
  ;  (r router/set-default-route! db :my-default-route {...})
  ;
  ; @return (map)
  [db [_ route-id route-props]]
  (assoc-in db [:router :default-handler/data-items route-id] route-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:router/set-default-route! :my-default-route {...}]
(a/reg-event-db :router/set-default-route! set-default-route!)
