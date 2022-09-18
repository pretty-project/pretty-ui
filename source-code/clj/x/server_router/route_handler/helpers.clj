
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
              [server-fruits.http                   :as http]
              [x.mid-router.route-handler.helpers   :as route-handler.helpers]
              [x.server-core.api                    :as a]
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
  ;
  ; @usage
  ;  (router/request->route-prop {...} :my-prop)
  ;
  ; @return (*)
  ;  Először az útvonalak szerver-oldali, majd a kliens-oldali tulajdonságain végigiterálva keres
  ;  a route-path értékével összeilleszthető {:route-template "..."} tulajdonságú útvonalat,
  ;  ami rendelkezik a prop-key tulajdonságként átadott tulajdonsággal.
  [request prop-key]
  (let [route-path (http/request->route-path request)]
       (letfn [(f [[_ {:keys [route-template] :as route-props}]]
                  (if (uri/path->match-template? route-path route-template) (get route-props prop-key)))]
              (or (some f @(a/subscribe [:router/get-server-routes]))
                  (some f @(a/subscribe [:router/get-client-routes]))))))

(defn request->core-js
  ; @param (map) request
  ;
  ; @usage
  ;  (router/request->core-js {...})
  ;
  ; @return (string)
  ;  Ha a request->route-prop függvény az útvonalak szerver-oldali és kliens-oldali tulajdonságai
  ;  között sem talált az aktuális útvonallal összeilleszthető {:route-template "..."} tulajdonságú
  ;  útvonalat, aminek a {:core-js "..."} tulajdonságával visszatérhetne (404, nem található útvonal),
  ;  akkor a visszatérési érték az útvonalak hozzáadásakor is használt DEFAULT-CORE-JS alapbeállítás.
  [request]
  (or (request->route-prop request :core-js) route-handler.config/DEFAULT-CORE-JS))

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
