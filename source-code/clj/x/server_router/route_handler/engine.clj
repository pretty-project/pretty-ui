
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler.engine
    (:require [mid-fruits.uri     :as uri]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [x.server-user.api  :as user]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-CORE-JS "app.js")

; @constant (keywords in vector)
(def CLIENT-ROUTE-KEYS [:client-event :core-js :on-leave-event :restricted? :route-parent :route-template])

; @constant (keywords in vector)
(def SERVER-ROUTE-KEYS [:get :core-js :post :restricted? :route-template :server-event])



;; -- Helpers -----------------------------------------------------------------
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
  (or (request->route-prop request :core-js) DEFAULT-CORE-JS))

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