
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler.helpers
    (:require [mid-fruits.candy                     :refer [return]]
              [mid-fruits.string                    :as string]
              [mid-fruits.vector                    :as vector]
              [mid-fruits.uri                       :as uri]
              [re-frame.api                         :as r]
              [server-fruits.http                   :as http]
              [x.mid-router.route-handler.helpers   :as route-handler.helpers]
              [x.server-router.route-handler.config :as route-handler.config]
              [x.server-user.api                    :as user]))



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
  ; @param (map) route-props
  ;  {:get (function or map)
  ;   :post (function or map)
  ;   :route-template (string)}
  ;
  ; @example
  ;  (route-handler.helpers/route-props->route-data {:route-template "/my-route" :get (fn [request] ...)})
  ;  =>
  ;  ["/my-route" {:get (fn [request] ...)}]
  ;
  ; @return (vector)
  ;  [(string) route-template
  ;   (map) route-props
  ;     {:get (function or map)
  ;      :post (function or map)}]
  [{:keys [route-template] :as route-props}]
  [route-template (dissoc route-props :route-template)])

(defn routes->destructed-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) routes
  ;
  ; @example
  ;  (route-handler.helpers/routes->destructed-routes {:my-route   {:route-template "/my-route"   :get (fn [request] ...)}
  ;                                                    :your-route {:route-template "/your-route" :get (fn [request] ...)}})
  ;  =>
  ;  [["/my-route"   {:get (fn [request] ...)}]]
  ;   ["/your-route" {:get (fn [request] ...)}]]
  ;
  ; @return (vectors in vector)
  [routes]
  (letfn [(f [destructed-routes route-id {:keys [route-template] :as route-props}]
             (if (route-conflict? destructed-routes route-template)
                 (return          destructed-routes)
                 (let [route-data (route-props->route-data route-props)]
                      (conj destructed-routes route-data))))]
         (reduce-kv f [] routes)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->route-prop
  ; @param (map) request
  ; @param (keyword) prop-key
  ; @param (*)(opt) default-value
  ;
  ; @usage
  ;  (router/request->route-prop {...} :my-prop)
  ;
  ; @return (*)
  ([request prop-key]
   (request->route-prop request prop-key nil))

  ([request prop-key default-value]
   ; A request->route-prop függvény a request térképből kiolvassa az aktuális útvonalat,
   ; majd az útvonalkezelőhöz adott útvonalak közül megkeresi az első olyat, aminek
   ; a route-template tulajdonságával az aktuális útvonal összeilleszthető ...
   ; ... és ha talál az útvonalkezelőben ilyen útvonalat, akkor visszatér annak a prop-key
   ;     kulcshoz tartozó tulajdonságával.
   ; ... és ha NEM talál az útvonalkezelőben ilyen útvonalat (pl. 404), akkor visszatér
   ;     az esetlegesen átadott default-value paraméterrel.
   ;
   ; A request->route-prop függvény először az útvonalkezelő szerver-oldali, majd a kliens-oldali
   ; útvonalain végigiterálva keres egyező útvonalat.
   (let [route-path (http/request->route-path request)]
        (letfn [(f [[_ {:keys [route-template] :as route-props}]]
                   (if (uri/path->match-template? route-path route-template)
                       (prop-key route-props)))]
               (or (some f @(r/subscribe [:router/get-server-routes]))
                   (some f @(r/subscribe [:router/get-client-routes]))
                   (return default-value))))))

(defn request->route-template-matched?
  ; @param (map) request
  ; @param (string) route-template
  ;
  ; @usage
  ;  (router/request->route-template-matched? {} "/my-route/:item-id")
  ;
  ; @return (boolean)
  [request route-template]
  (let [route-path (http/request->route-path request)]
       (uri/path->match-template? route-path route-template)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-authenticator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (function)
  ;
  ; @return (function)
  [handler]
  (fn [request] (if (user/request->authenticated? request)
                    (handler                      request)
                    (http/error-wrap {:body :access-denied :status 403}))))
