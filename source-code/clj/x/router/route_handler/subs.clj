
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.router.route-handler.subs
    (:require [candy.api                       :refer [return]]
              [mid.x.router.route-handler.subs :as route-handler.subs]
              [re-frame.api                    :as r :refer [r]]
              [x.router.route-handler.helpers  :as route-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.router.route-handler.subs
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
 ;  (r get-sitemap-routes db)
 ;
 ; @return (strings in vector)
 [db _]
 (get-in db [:x.router :sitemap-handler/routes]))

(defn get-server-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @usage
  ;  (r get-server-routes db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:x.router :route-handler/server-routes]))

(defn get-client-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @usage
  ;  (r get-client-routes db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:x.router :route-handler/client-routes]))

(defn get-cached-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @usage
  ;  (r get-cached-routes db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:x.router :route-handler/cached-routes]))

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
  ;  (r get-ordered-routes db)
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

(defn route-template-available?
  ; @param (string) n
  ;
  ; @usage
  ;  (r route-template-available? db "/my-route")
  ;
  ; @return (boolean)
  [db [_ n]]
  (let [cached-routes (r get-cached-routes db)]
       (letfn [(f [[route-template _]] (= route-template n))]
              (not (some f cached-routes)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.router/get-sitemap-routes]
(r/reg-sub :x.router/get-sitemap-routes get-sitemap-routes)

; @usage
;  [:x.router/get-server-routes]
(r/reg-sub :x.router/get-server-routes get-server-routes)

; @usage
;  [:x.router/get-client-routes]
(r/reg-sub :x.router/get-client-routes get-client-routes)

; @usage
;  [:x.router/get-cached-routes]
(r/reg-sub :x.router/get-cached-routes get-cached-routes)

; @usage
;  [:x.router/get-ordered-routes]
(r/reg-sub :x.router/get-ordered-routes get-ordered-routes)

; @usage
;  [:x.router/route-template-available? "/my-route"]
(r/reg-sub :x.router/route-template-available? route-template-available?)
