
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
(def variable-route-string?        route-handler.helpers/variable-route-string?)
(def resolve-variable-route-string route-handler.helpers/resolve-variable-route-string)



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
                    (http/error-wrap {:body "Access denied" :status 403}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-conflict?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vectors in vector) destructed-routes
  ; @param (vector) route-data
  ;  [(string) route-template
  ;   (map) route-options]
  ;
  ; @example
  ;  (route-handler.helpers/route-conflict? [[...] ["/my-route" {...}] [...] [...] [...]]
  ;                                         ["/my-route" {...}])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [destructed-routes [route-template _]]
  (boolean (some #(= (first %) route-template) destructed-routes)))

(defn route-template-parts-ordered?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Megvizsgálja, hogy a és b route-template-part paraméter abc sorrendben került-e átadásra.
  ; A ":" karakterrel kezdődő route-template-part paramétert (path-param változók nevei)
  ; magasabb értékűnek tekinti.
  ;
  ; @param (string) a
  ; @param (string) b
  ;
  ; @example
  ;  (route-handler.helpers/route-template-parts-ordered? "abcd" "abcd")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (route-handler.helpers/route-template-parts-ordered? "abcd" "efgh")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (route-handler.helpers/route-template-parts-ordered? "efgh" "abcd")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (route-handler.helpers/route-template-parts-ordered? ":abcd" "abcd")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [a b] ; Both a and b are path-param keys.
  (cond (and (string/starts-with? a ":")
             (string/starts-with? b ":"))
        (string/abc? a b)
        ; Only a is path-param keys.
        (string/starts-with? a ":")
        (return false)
        ; Only b is path-param keys.
        (string/starts-with? b ":")
        (return true)
        ; Both a and b are generic strings.
        :else
        (string/abc? a b)))

(defn route-templates-ordered?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Megvizsgálja, hogy a és b route-template paraméter abc sorrendben került-e átadásra.
  ; A ":" karakterrel kezdődő route-template paramétert (path-param változók nevei)
  ; magasabb értékűnek tekinti.
  ;
  ; @param (string) a
  ; @param (string) b
  ;
  ; @example
  ;  (route-handler.helpers/route-templates-ordered? "/ab/cd" "/ab/cd")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (route-handler.helpers/route-templates-ordered? "/ab/cd" "/ef/gh")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (route-handler.helpers/route-templates-ordered? "/ef/gh" "/ab/cd")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (route-handler.helpers/route-templates-ordered? "/:ab/cd" "/ab/cd")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [a b]
  (let [a-parts (string/split a #"/")
        b-parts (string/split b #"/")]
       (vector/compared-items-ordered? a-parts b-parts route-template-parts-ordered?)))

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

(defn destructed-routes->ordered-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vectors in vector) routes
  ;
  ; @example
  ;  (route-handler.helpers/destructed-routes->ordered-routes [["/my-route"   {:get (fn [request] ...)}]
  ;                                                            ["/your-route" {:get (fn [request] ...)}]
  ;                                                            ["/our-route"  {:get (fn [request] ...)}]]
  ;  =>
  ;  [["/my-route"   {:get (fn [request] ...)}]]
  ;   ["/our-route"  {:get (fn [request] ...)}]
  ;   ["/your-route" {:get (fn [request] ...)}]]
  ;
  ; @example
  ;  (route-handler.helpers/destructed-routes->ordered-routes [["/my-route/:a" {...}]
  ;                                                            ["/my-route/c"  {...}]
  ;                                                            ["/my-route/b"  {...}]
  ;                                                            ["/my-route"    {...}]]
  ;  =>
  ;  [["/my-route/b"  {...}]]
  ;   ["/my-route/c"  {...}]
  ;   ["/my-route/:a" {...}]
  ;   ["/my-route"    {...}]]
  ;
  ; @return (vectors in vector)
  [destructed-routes]
  (vector/sort-items-by destructed-routes route-templates-ordered? first))
