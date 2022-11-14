
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.router.route-handler.events
    (:require [candy.api                         :refer [return]]
              [mid-fruits.vector                 :as vector]
              [re-frame.api                      :as r :refer [r]]
              [x.router.route-handler.config     :as route-handler.config]
              [x.router.route-handler.helpers    :as route-handler.helpers]
              [x.router.route-handler.prototypes :as route-handler.prototypes]
              [x.router.route-handler.subs       :as route-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-route-to-sitemap!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;  {:route-template (string)}
  ;
  ; @return (map)
  [db [_ _ {:keys [route-template]}]]
  (update-in db [:x.router :sitemap-handler/routes] vector/conj-item route-template))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cache-route-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;
  ; @return (map)
  [db [_ route-id route-props]]
  ; XXX#7708 (source-code/clj/x/server_router/README.md)
  (let [cached-routes    (r route-handler.subs/get-cached-routes db)
        destructed-route (-> route-props (select-keys route-handler.config/CACHED-ROUTE-KEYS)
                                         (route-handler.helpers/route-props->destructed-route))]
       (assoc-in db [:x.router :route-handler/cached-routes]
                    (-> cached-routes (vector/conj-item destructed-route)
                                      (route-handler.helpers/destructed-routes->ordered-routes)))))

(defn store-server-route-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;  {:get (function or map)(opt)
  ;   :post (function or map)(opt)}
  ;
  ; @return (map)
  [db [_ route-id {:keys [get post] :as route-props}]]
  ; XXX#7706 (source-code/clj/x/server_router/README.md)
  (if (or get post)
      (assoc-in db [:x.router :route-handler/server-routes route-id]
                   (select-keys route-props route-handler.config/SERVER-ROUTE-KEYS))
      (return   db)))

(defn store-client-route-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;  {:client-event (metamorphic-event)(opt)
  ;   :on-leave-event (metamorphic-event)(opt)}
  ;
  ; @return (map)
  [db [_ route-id {:keys [client-event on-leave-event] :as route-props}]]
  ; XXX#7707 (source-code/clj/x/server_router/README.md)
  (if (or client-event on-leave-event)
      (assoc-in db [:x.router :route-handler/client-routes route-id]
                   (select-keys route-props route-handler.config/CLIENT-ROUTE-KEYS))
      (return   db)))

(defn store-route-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;  {:add-to-sitemap? (boolean)(opt)}
  ;
  ; @return (map)
  [db [_ route-id {:keys [add-to-sitemap?] :as route-props}]]
  (if add-to-sitemap? (as-> db % (r add-route-to-sitemap!     % route-id route-props)
                                 (r store-server-route-props! % route-id route-props)
                                 (r store-client-route-props! % route-id route-props)
                                 (r cache-route-props!        % route-id route-props))
                      (as-> db % (r store-server-route-props! % route-id route-props)
                                 (r store-client-route-props! % route-id route-props)
                                 (r cache-route-props!        % route-id route-props))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-route!
  ; @param (keyword)(opt) route-id
  ; @param (map) route-props
  ;  {:add-to-sitemap? (boolean)(opt)
  ;    Default: false
  ;   :get (function or map)(opt)
  ;   :js-build (keyword)(opt)
  ;   :post (function or map)(opt)
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
  ;  (r add-route! db {...})
  ;
  ; @usage
  ;  (r add-route! db :my-route {...})
  ;
  ; @usage
  ;  (r add-route! db :my-route {:route-template "/my-route"
  ;                              :get (fn [request] ...)})
  ;
  ; @usage
  ;  (r add-route! db :my-route {:route-template "/my-route"
  ;                              :client-event [:my-event]
  ;                              :js-build :my-build})
  ;
  ; @return (map)
  [db [_ route-id route-props]]
  (let [route-props (r route-handler.prototypes/route-props-prototype db route-id route-props)]
       (if-let [route-template (get route-props :route-template)]
               ; If route-props contains route-template ...
               (let [app-home       (r route-handler.subs/get-app-home db)
                     route-template (route-handler.helpers/resolve-variable-route-string route-template app-home)
                     route-props    (assoc route-props :route-template route-template)]
                    (r store-route-props! db route-id route-props))
               ; If route-props NOT contains route-template ...
               (r store-route-props! db route-id route-props))))

(defn add-routes!
  ; @param (map) routes
  ;
  ; @usage
  ;  (r add-routes! db {:my-route {:route-template "/my-route"
  ;                                :get {:handler my-handler}}
  ;                     :your-route {...}})
  ;
  ; @return (map)
  [db [_ routes]]
  (letfn [(f [db route-id route-props]
             (r add-route! db route-id route-props))]
         (reduce-kv f db routes)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.router/add-route! {...}]
;
; @usage
;  [:x.router/add-route! :my-route {...}]
(r/reg-event-db :x.router/add-route! [r/event-vector<-id] add-route!)

; @usage
;  [:x.router/add-routes! {:my-route {:route-template "/my-route"
;                                   :get {:handler my-handler}}
;                        :your-route {...}}]
(r/reg-event-db :x.router/add-routes! add-routes!)
