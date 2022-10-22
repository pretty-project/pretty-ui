
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler.subs
    (:require [re-frame.api                          :as r :refer [r]]
              [x.mid-router.route-handler.subs       :as route-handler.subs]
              [x.server-router.route-handler.helpers :as route-handler.helpers]
              [x.server-user.api                     :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-router.route-handler.subs
(def get-app-home   route-handler.subs/get-app-home)
(def use-app-home   route-handler.subs/use-app-home)
(def get-app-domain route-handler.subs/get-app-domain)
(def use-app-domain route-handler.subs/use-app-domain)



;; ----------------------------------------------------------------------------
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
       (route-handler.helpers/routes->destructed-routes server-routes)))

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
       (route-handler.helpers/destructed-routes->ordered-routes destructed-routes)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:router/get-sitemap-routes]
(r/reg-sub :router/get-sitemap-routes get-sitemap-routes)

; @usage
;  [:router/get-server-routes]
(r/reg-sub :router/get-server-routes get-server-routes)

; @usage
;  [:router/get-client-routes]
(r/reg-sub :router/get-client-routes get-client-routes)

; @usage
;  [:router/get-ordered-routes]
(r/reg-sub :router/get-ordered-routes get-ordered-routes)
