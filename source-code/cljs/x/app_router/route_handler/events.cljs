
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.events
    (:require [mid-fruits.map                    :refer [dissoc-in]]
              [mid-fruits.uri                    :as uri]
              [mid-fruits.vector                 :as vector]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-db.api                      :as db]
              [x.app-router.route-handler.config :as route-handler.config]
              [x.app-router.route-handler.subs   :as route-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-current-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (map)
  [db [_ route-string]]
  ; A store-current-route! függvény a route-string paraméterből származtatott értékeket
  ; az útvonal kezelésekor eltárolja, így az ezekre az értékekre történő Re-Frame feliratkozások
  ; kevesebb erőforrást igényelnek, mint ha a feliratkozás függvényekben történne az értékek származtatása.
  (let [route-id           (r route-handler.subs/match-route-id db route-string)
        route-template     (get-in db [:router :route-handler/client-routes route-id :route-template])
        route-fragment     (uri/uri->fragment     route-string)
        route-path         (uri/uri->path         route-string)
        route-path-params  (uri/uri->path-params  route-string route-template)
        route-query-params (uri/uri->query-params route-string)
        route-parent (get-in db [:router :route-handler/client-routes route-id :route-parent]
                                (uri/uri->parent-uri route-string))]
       (-> db (assoc-in [:router :route-handler/meta-items :route-id]           route-id)
              (assoc-in [:router :route-handler/meta-items :route-fragment]     route-fragment)
              (assoc-in [:router :route-handler/meta-items :route-parent]       route-parent)
              (assoc-in [:router :route-handler/meta-items :route-path]         route-path)
              (assoc-in [:router :route-handler/meta-items :route-path-params]  route-path-params)
              (assoc-in [:router :route-handler/meta-items :route-query-params] route-query-params)
              (assoc-in [:router :route-handler/meta-items :route-string]       route-string))))

(defn set-default-routes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  ; A szerverről érkezett client-routes útvonalak magasabb prioritásúak, mint a DEFAULT-ROUTES útvonalak!
  ; Így lehetséges a szerver-oldalon beállított útvonalakkal felülírni a kliens-oldali DEFAULT-ROUTES útvonalakat.
  (let [client-routes (r route-handler.subs/get-client-routes db)]
       (assoc-in db [:router :route-handler/client-routes]
                    (merge route-handler.config/DEFAULT-ROUTES client-routes))))

(defn reg-to-history!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (map)
  [db [_ route-id]]
  (r db/apply-item! db [:router :route-handler/meta-items :history] vector/conj-item route-id))
