
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.router.route-handler.helpers
    (:require [candy.api                          :refer [return]]
              [mid.x.router.route-handler.helpers :as route-handler.helpers]
              [plugins.clerk.api                  :as clerk]
              [re-frame.api                       :as r]
              [reagent.api                        :as reagent]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.router.route-handler.helpers
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
  ;  (route-props->route-data :my-route {:route-template "/my-route"})
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
  ;  (routes->destructed-routes {:my-route   {:route-template "/my-route"}
  ;                              :your-route {:route-template "/your-route"}})
  ;  =>
  ;  [["/my-route"   :my-route]]
  ;   ["/your-route" :your-route]]
  ;
  ; @return (vectors in vector)
  [routes]
  (letfn [(f [destructed-routes route-id {:keys [route-template] :as route-props}]
             (if (route-conflict? destructed-routes route-template)
                 (return          destructed-routes)
                 (let [route-data (route-props->route-data route-id route-props)]
                      (conj destructed-routes route-data))))]
         (reduce-kv f [] routes)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn nav-handler-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) route-string
  [route-string]
 ; BUG#3001
 ; {:reload-same-path? true} beállítással használt accountant plugin esetén a nav-handler-f
 ; függvény kétszer hívódik meg, ha az útvonal megváltozik!

 ;(reagent/after-render clerk/after-render!)
  (r/dispatch [:x.router/handle-route! route-string]))
 ;(clerk/navigate-page! route-string))

(defn path-exists-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) route-string
  ;
  ; @return (boolean)
  [route-string]
  ; The fn should return truthy if the path is handled by your SPA, because accountant will
  ; call event.preventDefault() to prevent the browser from doing a full page request.
  (r/subscribed [:x.router/route-template-exists? route-string]))
