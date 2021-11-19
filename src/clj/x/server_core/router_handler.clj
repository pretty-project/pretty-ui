
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.24
; Description:
; Version: v0.9.6
; Compatibility: x4.2.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.router-handler
    (:require [reitit.core                      :as reitit]
              [reitit.ring                      :as reitit-ring]
              [server-fruits.http               :as http]
              [x.server-core.event-handler      :as event-handler]
              [x.server-core.middleware-handler :refer [middleware]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [default-routes           (event-handler/subscribed [:x.server-router/get-default-routes])
        resource-handler-options (event-handler/subscribed [:x.server-core/get-resource-handler-options])]
       (reitit-ring/routes (reitit-ring/create-resource-handler resource-handler-options)
                           (reitit-ring/create-default-handler  default-routes))))

(defn router
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [ordered-routes (event-handler/subscribed [:x.server-router/get-ordered-routes])]
       ; Disable route conflicts handling:
       ;(reitit-ring/router ordered-routes)

       ; Enable route conflicts handling:
       (reitit-ring/router ordered-routes {:conflicts nil})))

(defn ring-handler
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (reitit-ring/ring-handler (router)
                            (options)
                            (middleware)))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-template->route-match
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-template
  ;
  ; @return (reitit.core.Match class)
  ;  {:data (map)}
  [route-template]
  (let [router (router)]
       (reitit/match-by-path router route-template)))

(defn route-template->route-param
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-template
  ; @param (keyword) param-id
  ;
  ; @example
  ;  (a/route-template->route-param "/my-route" :get)
  ;  =>
  ;  {:handler my-route-handler}
  ;
  ; @return (*)
  [route-template param-id]
  (let [route-match (route-template->route-match route-template)]
       (get-in route-match [:data param-id])))

(defn request->route-match
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (reitit.core.Match class)
  ;  {:data (map)}
  [request]
  (let [route-template (http/request->route-template request)]
       (route-template->route-match route-template)))

(defn request->route-param
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (keyword) param-id
  ;
  ; @example
  ;  (a/request->route-param {...} :get)
  ;  =>
  ;  {:handler my-route-handler}
  ;
  ; @return (*)
  [request param-id]
  (let [route-template (http/request->route-template request)]
       (route-template->route-param route-template param-id)))
