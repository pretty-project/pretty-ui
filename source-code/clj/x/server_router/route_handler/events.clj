
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler.events
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.map     :as map]
              [mid-fruits.vector  :as vector]
              [x.server-core.api  :as a :refer [r]]
              [x.server-router.engine     :as engine]
              [x.server-router.route-handler.engine :as route-handler.engine]
              [x.server-router.route-handler.subs   :as route-handler.subs]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- handler-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (function or map) handler
  ; @param (map) options
  ;  {:restricted? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:handler (function)}
  [handler {:keys [restricted?]}]
  (if restricted? (cond (fn?  handler) (return {:handler (route-handler.engine/route-authenticator           handler)})
                        (map? handler) (assoc   :handler (route-handler.engine/route-authenticator (:handler handler))))
                  (cond (fn?  handler) (return {:handler handler})
                        (map? handler) (return handler))))

(defn- route-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) route-props
  ;  {:get (function or map)(opt)
  ;   :post (function or map)(opt)
  ;   :restricted? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:get (map)
  ;   :core-js (string)
  ;   :post (map)}
  [{:keys [get post restricted?] :as route-props}]
  (merge {:core-js "app.js"}
         (param route-props)
         (if get  {:get  (handler-prototype get  {:restricted? restricted?})})
         (if post {:post (handler-prototype post {:restricted? restricted?})})))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-server-route-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;  {:get (function or map)(opt)
  ;   :post (function or map)(opt)}
  ;
  ; @return (map)
  [db [_ route-id {:keys [get post] :as route-props}]]
  (if (or get post)
      (assoc-in db [:router :route-handler/server-routes route-id]
                   (select-keys route-props route-handler.engine/SERVER-ROUTE-KEYS))
      (return   db)))

(defn- store-client-route-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;  {:client-event (metamorphic-event)(opt)
  ;   :on-leave-event (metamorphic-event)(opt)}
  ;
  ; @return (map)
  [db [_ route-id {:keys [client-event on-leave-event] :as route-props}]]
  (if (or client-event on-leave-event)
      (assoc-in db [:router :route-handler/client-routes route-id]
                   (select-keys route-props route-handler.engine/CLIENT-ROUTE-KEYS))
      (return   db)))

(defn- add-route-to-sitemap!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;  {:route-template (string)}
  ;
  ; @return (map)
  [db [_ _ {:keys [route-template]}]]
  (update-in db [:router :sitemap-handler/data-items] vector/conj-item route-template))

(defn- store-route-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;
  ; @return (map)
  [db [_ route-id {:keys [add-to-sitemap?] :as route-props}]]
  (as-> db % (r store-server-route-props! % route-id route-props)
             (r store-client-route-props! % route-id route-props)
             (if-not add-to-sitemap? % (r add-route-to-sitemap! % route-id route-props))))

(defn add-route!
  ; @param (keyword)(opt) route-id
  ; @param (map) route-props
  ;  {:add-to-sitemap? (boolean)(opt)
  ;    Default: false
  ;   :get (function or map)(opt)
  ;   :post (function or map)(opt)
  ;   :core-js (string)(opt)
  ;    Default: "app.js"
  ;   :restricted? (boolean)(opt)
  ;    Default: false
  ;   :route-parent (string)(opt)
  ;   :route-template (string)
  ;   :client-event (metamorphic-event)(opt)
  ;    Az útvonal meghívásakor a kliens-oldalon megtörténő esemény
  ;   :on-leave-event (metamorphic-event)(opt)
  ;    Az útvonal elhagyásakor a kliens-oldalon megtörténő esemény
  ;   :server-event (metamorphic-event)(opt)}
  ;    Az útvonal meghívásakor a szerver-oldalon megtörténő esemény}
  ;
  ; @usage
  ;  (r router/add-route! db {...})
  ;
  ; @usage
  ;  (r router/add-route! db :my-route {...})
  ;
  ; @usage
  ;  (r router/add-route! db :my-route {:route-template "/my-route"
  ;                                     :get (fn [request] ...)})
  ;
  ; @return (map)
  [db [_ route-id route-props]]
  (let [route-props (route-props-prototype route-props)]
       (if-let [route-template (get route-props :route-template)]
               ; If route-props contains route-template ...
               (if (engine/variable-route-string? route-template)
                   ; If route-template is variable ...
                   (let [app-home       (r route-handler.subs/get-app-home db)
                         route-template (engine/resolve-variable-route-string route-template app-home)
                         route-props    (assoc route-props :route-template route-template)]
                        (r store-route-props! db route-id route-props))
                   ; If route-template is static ...
                   (r store-route-props! db route-id route-props))
               ; If route-props NOT contains route-template ...
               (r store-route-props! db route-id route-props))))

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
  (letfn [(f [db route-id route-props]
             (r add-route! db route-id route-props))]
         (reduce-kv f db routes)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:router/add-route! {...}]
;
; @usage
;  [:router/add-route! :my-route {...}]
(a/reg-event-db :router/add-route! [a/event-vector<-id] add-route!)

; @usage
;  [:router/add-routes! {:my-route {:route-template "/my-route"
;                                   :get {:handler my-handler}}
;                        :your-route {...}}]
(a/reg-event-db :router/add-routes! add-routes!)
