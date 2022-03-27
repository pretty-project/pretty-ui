
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.plugin-handler.routes.helpers
    (:require [mid-fruits.string :as string]
              [mid-fruits.uri    :as uri]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) route-key
  ;
  ; @example
  ;  (routes.helpers/route-id :my-namespace/my-plugin :extended)
  ;  =>
  ;  :my-namespace.my-plugin/extended-route
  ;
  ; @return (keyword)
  [plugin-id route-key]
  (if-let [namespace (namespace plugin-id)]
          (keyword (str namespace "." (name plugin-id)) (str (name route-key) "-route"))
          (keyword (str               (name plugin-id)) (str (name route-key) "-route"))))

(defn base-route
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) plugin-props
  ;  {:route-template (string)}
  ;
  ; @example
  ;  (routes.helpers/base-route :my-plugin {:route-template "/@app-home/my-plugin/:item-id"})
  ;  =>
  ;  "/@app-home/my-plugin"
  ;
  ; @return (string)
  [_ {:keys [route-template]}]
  (let [base-route (string/before-last-occurence route-template "/:")]
       (if (string/nonempty? base-route)
           (uri/valid-path   base-route)
           (uri/valid-path   route-template))))

(defn extended-route
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) plugin-props
  ;  {:route-template (string)}
  ;
  ; @example
  ;  (routes.helpers/extended-route :my-plugin {:route-template "/@app-home/my-plugin/:item-id"})
  ;  =>
  ;  "/@app-home/my-plugin/:item-id"
  ;
  ; @return (string)
  [_ {:keys [route-template]}]
  (uri/valid-path route-template))

(defn parent-route
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) plugin-props
  ;  {:route-template (string)}
  ;
  ; @example
  ;  (routes.helpers/parent-route :my-plugin {:route-template "/@app-home/my-plugin"})
  ;  =>
  ;  "/@app-home"
  ;
  ; @example
  ;  (routes.helpers/parent-route :my-plugin {:route-template "/@app-home/my-plugin/:item-id"})
  ;  =>
  ;  "/@app-home"
  ;
  ; @return (string)
  [_ {:keys [route-template]}]
  ; Abban az esetben szükséges alkalmazni a parent-route függvény által visszaadott útvonalat szülő-
  ; -útvonalként, ha a "/@app-home/my-plugin/:item-id" útvonal szülő-útvonala NEM az "/@app-home/my-plugin"
  ; útvonal, hanem a "/@app-home" útvonal.
  ; Pl.
  (let [base-route (string/before-last-occurence route-template "/:")]
       (if (string/nonempty?    base-route)
           (uri/uri->parent-uri base-route)
           (uri/uri->parent-uri route-template))))
