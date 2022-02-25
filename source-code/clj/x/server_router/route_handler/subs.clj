
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler.subs
    (:require [x.server-core.api      :as a :refer [r]]
              [x.server-router.engine :as engine]
              [x.server-user.api      :as user]
              [x.mid-router.route-handler.subs :as route-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-router.route-handler.subs
(def get-app-home     route-handler.subs/get-app-home)
(def get-resolved-uri route-handler.subs/get-resolved-uri)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-sitemap-routes
 ; WARNING! NON-PUBLIC! DO NOT USE!
 ;
 ; @usage
 ;  (r router/get-sitemap-routes db)
 ;
 ; @return (strings in vector)
 [db _]
 (get-in db [:router :sitemap-handler/data-items]))

(defn get-server-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @usage
  ;  (r router/get-server-routes db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:router :route-handler/server-routes]))

(defn get-client-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @usage
  ;  (r router/get-client-routes db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:router :route-handler/client-routes]))

(defn get-destructed-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (vectors in vector)
  ;  [[(string) route-template
  ;    (map) route-props
  ;      {:get (function or map)
  ;       :post (function or map)}]
  ;   [...]
  ;   [...]]
  [db _]
  (let [server-routes (r get-server-routes db)]
       (engine/routes->destructed-routes server-routes)))

(defn get-ordered-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @example
  ;  (r router/get-ordered-routes db)
  ;  =>
  ;  [["my-route"  {:get  {...}}]
  ;   ["your-route {:post {...}}"]]
  ;
  ; @return (vectors in vector)
  ;  [[(string) route-template
  ;    (map) route-props
  ;      {:get (function or map)
  ;       :post (function or map)}]
  ;   [...]
  ;   [...]]
  [db _]
  (let [destructed-routes (r get-destructed-routes db)]
       (engine/destructed-routes->ordered-routes destructed-routes)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:router/get-sitemap-routes]
(a/reg-sub :router/get-sitemap-routes get-sitemap-routes)

; @usage
;  [:router/get-server-routes]
(a/reg-sub :router/get-server-routes get-server-routes)

; @usage
;  [:router/get-client-routes]
(a/reg-sub :router/get-client-routes get-client-routes)

; @usage
;  [:router/get-ordered-routes]
(a/reg-sub :router/get-ordered-routes get-ordered-routes)
