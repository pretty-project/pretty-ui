
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.2.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.engine
    (:require [mid-fruits.candy    :refer [param return]]
              [mid-fruits.string   :as string]
              [mid-fruits.vector   :as vector]
              [x.mid-router.engine :as engine]
              [x.server-core.api   :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-router.engine
(def variable-route-string?        engine/variable-route-string?)
(def resolve-variable-route-string engine/resolve-variable-route-string)
(def route-props->server-route?    engine/route-props->server-route?)
(def route-props->client-route?    engine/route-props->client-route?)



;; -- Helpers -----------------------------------------------------------------
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
  ;  (engine/route-conflict? [[...] ["/my-route" {...}] [...] [...] [...]]
  ;                          ["/my-route" {...}])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [destructed-routes [route-template _]]
  (boolean (some #(= (first %) route-template)
                  (param destructed-routes))))

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
  ;  (engine/route-template-parts-ordered? "abcd" "abcd")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (engine/route-template-parts-ordered? "abcd" "efgh")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (engine/route-template-parts-ordered? "efgh" "abcd")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (engine/route-template-parts-ordered? ":abcd" "abcd")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [a b] ; Both a and b are path-param-id identifiers.
  (cond (and (string/starts-with? a ":")
             (string/starts-with? b ":"))
        (string/abc? a b)
        ; Only a is path-param-id identifiers.
        (string/starts-with? a ":")
        (return false)
        ; Only b is path-param-id identifiers.
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
  ;  (engine/route-templates-ordered? "/ab/cd" "/ab/cd")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (engine/route-templates-ordered? "/ab/cd" "/ef/gh")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (engine/route-templates-ordered? "/ef/gh" "/ab/cd")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (engine/route-templates-ordered? "/:ab/cd" "/ab/cd")
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
  ;  (engine/route-props->route-data {:route-template "/my-route" :get (fn [request] ...)})
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
  ;  (engine/routes->destructed-routes {:my-route   {:route-template "/my-route"   :get (fn [request] ...)}
  ;                                     :your-route {:route-template "/your-route" :get (fn [request] ...)}})
  ;  =>
  ;  [["/my-route"   {:get (fn [request] ...)}]]
  ;   ["/your-route" {:get (fn [request] ...)}]]
  ;
  ; @return (vectors in vector)
  [routes]
  (reduce-kv (fn [destructed-routes route-id {:keys [route-template] :as route-props}]
                 (if (route-conflict? destructed-routes route-template)
                     (return destructed-routes)
                     (let [route-data (route-props->route-data route-props)]
                          (vector/conj-item destructed-routes route-data))))
             (param [])
             (param routes)))

(defn destructed-routes->ordered-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vectors in vector) routes
  ;
  ; @example
  ;  (engine/destructed-routes->ordered-routes [["/my-route"   {:get (fn [request] ...)}]
  ;                                             ["/your-route" {:get (fn [request] ...)}]
  ;                                             ["/our-route"  {:get (fn [request] ...)}]]
  ;  =>
  ;  [["/my-route"   {:get (fn [request] ...)}]]
  ;   ["/our-route"  {:get (fn [request] ...)}]
  ;   ["/your-route" {:get (fn [request] ...)}]]
  ;
  ; @example
  ;  (engine/destructed-routes->ordered-routes [["/my-route/:a" {...}]
  ;                                             ["/my-route/c"  {...}]
  ;                                             ["/my-route/b"  {...}]
  ;                                             ["/my-route"    {...}]]
  ;  =>
  ;  [["/my-route/b"  {...}]]
  ;   ["/my-route/c"  {...}]
  ;   ["/my-route/:a" {...}]
  ;   ["/my-route"    {...}]]
  ;
  ; @return (vectors in vector)
  [destructed-routes]
  (vector/order-items-by destructed-routes route-templates-ordered? first))
