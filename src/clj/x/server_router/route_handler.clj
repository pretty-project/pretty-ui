
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.10
; Description:
; Version: v1.1.2
; Compatibility: x4.2.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]
              [x.server-core.api :as a :refer [r]]
              [x.server-db.api   :as db]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name route-data
;  ["/my-route" {:get #(my-handler %)}]
;
; @name route-template
;  "/my-route"
;
; @name route-props
;  {:get #(my-handler %)
;   :post {...}
;   :route-template "/my-route"}
;
; @name structured-routes
;  A route-ok és azok adatai route-id - route-props kulcs-érték párokként
;  tárolódnak a szerver-oldali Re-Frame adatbázisban.
;
;  {:my-route    {:route-template "/my-route"
;                 :get {...}}
;   :your-route {:route-template "/your-route"
;                :post {...}}}
;
; @name destructed-routes
;  Az applikáció indításakor a Reitit router számára egy vektorba struktúrálva
;  adódnak át a route-ok és azok adatai.
;
; [["/my-route"   {:get #(my-handler %)}]
;  ["/your-route" {:post {...}}]]
;
; @name ordered-routes
;  A vektorba struktúrált route-ok a route-template értékük szerint abc sorrendbe
;  rendezve kerülnek átadásra a Reitit router számára úgy, hogy a path-param
;  változók nevei magasabb értékűnek számítanak – így azok a vektor későbbi elemei,
;  hogy a Reitit router ne kezelje őket konfliktusként.
;
; [["/my-route"   ...]
;  ["/our-route"  ...]
;  ["/our-route/your-page"]
;  ["/our-route/:my-param"   ...]
;  ["/our-route/:your-param" ...]
;  ["/your-route" ...]]



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
  ;  (router/route-conflict? [[...] ["/my-route" {...}] [...] [...] [...]]
  ;                          ["/my-route" {...}])
  ;  => true
  ;
  ; @return (boolean)
  [destructed-routes [route-template _]]
  (boolean (some #(= (first %) route-template)
                  (param destructed-routes))))

(defn route-template-parts-ordered?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Megvizsgálja, hogy a és b route-template-part paraméter abc sorrendben
  ; került-e átadásra.
  ; A ":" karakterrel kezdődő route-template-part paramétert (path-param változók nevei)
  ; magasabb értékűnek tekinti.
  ;
  ; @param (string) a
  ; @param (string) b
  ;
  ; @example
  ;  (router/route-template-parts-ordered? "abcd" "abcd")
  ;  => true
  ;
  ; @example
  ;  (router/route-template-parts-ordered? "abcd" "efgh")
  ;  => true
  ;
  ; @example
  ;  (router/route-template-parts-ordered? "efgh" "abcd")
  ;  => false
  ;
  ; @example
  ;  (router/route-template-parts-ordered? ":abcd" "abcd")
  ;  => false
  ;
  ; @return (boolean)
  [a b]
        ; Both a and b are path-param-id identifiers.
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
  ; Megvizsgálja, hogy a és b route-template paraméter abc sorrendben került-e
  ; átadásra.
  ; A ":" karakterrel kezdődő route-template paramétert (path-param változók nevei)
  ; magasabb értékűnek tekinti.
  ;
  ; @param (string) a
  ; @param (string) b
  ;
  ; @example
  ;  (router/route-templates-ordered? "/ab/cd" "/ab/cd")
  ;  => true
  ;
  ; @example
  ;  (router/route-templates-ordered? "/ab/cd" "/ef/gh")
  ;  => true
  ;
  ; @example
  ;  (router/route-templates-ordered? "/ef/gh" "/ab/cd")
  ;  => false
  ;
  ; @example
  ;  (router/route-templates-ordered? "/:ab/cd" "/ab/cd")
  ;  => false
  ;
  ; @return (boolean)
  [a b]
  (let [a-parts (string/split a #"/")
        b-parts (string/split b #"/")]
       (vector/compared-items-ordered? a-parts b-parts route-template-parts-ordered?)))



;; -- Converters --------------------------------------------------------------
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
  ;  (route-props->route-data {:route-template "/my-route" :get (fn [request] ...)})
  ;  => ["/my-route" {:get (fn [request] ...)}]
  ;
  ; @return (vector)
  ;  [(string) route-template
  ;   (map) route-props
  ;    {:get (function or map)
  ;     :post (function or map)}]
  [{:keys [route-template] :as route-props}]
  [route-template (dissoc route-props :route-template)])

(defn routes->destructed-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) routes
  ;
  ; @example
  ;  (routes->destructed-routes {:my-route   {:route-template "/my-route"   :get (fn [request] ...)}
  ;                              :your-route {:route-template "/your-route" :get (fn [request] ...)}})
  ;  => [["/my-route"   {:get (fn [request] ...)}]
  ;      ["/your-route" {:get (fn [request] ...)}]]
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
  ;  (destructed-routes->ordered-routes [["/my-route"   {:get (fn [request] ...)}]
  ;                                      ["/your-route" {:get (fn [request] ...)}]
  ;                                      ["/our-route"  {:get (fn [request] ...)}]]
  ;  => [["/my-route"   {:get (fn [request] ...)}]
  ;      ["/our-route"  {:get (fn [request] ...)}]
  ;      ["/your-route" {:get (fn [request] ...)}]]
  ;
  ; @example
  ;  (destructed-routes->ordered-routes [["/my-route/:a" {...}]
  ;                                      ["/my-route/c"  {...}]
  ;                                      ["/my-route/b"  {...}]
  ;                                      ["/my-route"    {...}]]
  ;  => [["/my-route/b"  {...}]
  ;      ["/my-route/c"  {...}]
  ;      ["/my-route/:a" {...}]
  ;      ["/my-route"    {...}]]
  ;
  ; @return (vectors in vector)
  [destructed-routes]
  (vector/order-items-by destructed-routes route-templates-ordered? first))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (vectors in vector)
  [db _]
  (get-in db (db/path ::routes)))

(defn get-destructed-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (vectors in vector)
  ;  [[(string) route-template
  ;    (map) route-props
  ;     {:get (function or map)
  ;      :post (function or map)}]
  ;   [...]
  ;   [...]]
  [db _]
  (let [routes (r get-routes db)]
       (routes->destructed-routes routes)))

(defn get-ordered-routes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (vectors in vector)
  ;  [[(string) route-template
  ;    (map) route-props
  ;     {:get (function or map)
  ;      :post (function or map)}]
  ;   [...]
  ;   [...]]
  [db _]
  (let [destructed-routes (r get-destructed-routes db)]
       (destructed-routes->ordered-routes destructed-routes)))

(a/reg-sub :x.server-router/get-ordered-routes get-ordered-routes)

(defn get-reserved-routes
  ; @example
  ;  (r router/get-reserved-routes db)
  ;  => ["/my-route" "/your-route" "/:my-route-param" ...]
  ;
  ; @return (strings in vector)
  [db _]
  (let [routes (r get-routes db)]
       (reduce-kv (fn [route-templates route-id {:keys [route-template]}]
                      (vector/conj-item route-templates route-template))
                  (param [])
                  (param routes))))

(a/reg-sub :x.server-router/get-reserved-routes get-reserved-routes)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-routes!
  ; @param (map) routes
  ;
  ; @usage
  ;  (r router/add-routes! db {:my-route {:route-template "/my-route"
  ;                                       :get {:handler my-handler}}
  ;                            :your-route {...}})
  ;
  ; @return (map)
  [db [_ routes]]
  (assoc-in db (db/path ::routes)
               (merge (param routes)
                      (r get-routes db))))

; @usage
;  [:x.server-router/add-routes! {:my-route {:route-template "/my-route"
;                                            :get {:handler my-handler}}
;                                 :your-route {...}}]
(a/reg-event-db :x.server-router/add-routes! add-routes!)

(defn add-route!
  ; @param (keyword)(opt) route-id
  ; @param (map) route-props
  ;  {:get (function or map)
  ;    Only w/ {:route-handler :server}
  ;   :post (function or map)
  ;    Only w/ {:route-handler :server}

  ; TEMP
  ;   :route-handler (keyword)(opt)
  ;    :server, :client
  ;    Default: :server
  ; TEMP

  ;   :route-template (string)}
  ;
  ; @usage
  ;  (r router/add-route! db {...})
  ;
  ; @usage
  ;  (r router/add-route! db :my-route {...})
  ;
  ;; @usage
  ;  (r router/add-route! db :my-route {:route-template "/my-route"
  ;                                     :get (fn [request] ...)})
  ;
  ; @return (map)
  [db event-vector]
  (let [route-id    (a/event-vector->second-id   event-vector)
        route-props (a/event-vector->first-props event-vector)]
       (assoc-in db (db/path ::routes route-id) route-props)))

; @usage
;  [:x.server-router/add-route! {...}]
;
; @usage
;  [:x.server-router/add-route! :my-route {...}]
(a/reg-event-db :x.server-router/add-route! add-route!)
