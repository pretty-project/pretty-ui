
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.events
    (:require [mid-fruits.vector   :as vector]
              [x.app-core.api      :as a :refer [r]]
              [x.app-db.api        :as db]
              [x.app-router.route-handler.engine :as route-handler.engine]
              [x.app-router.route-handler.subs   :as route-handler.subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-current-route-string!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (map)
  [db [_ route-string]]
  (assoc-in db [:router :route-handler/meta-items :route-string] route-string))

(defn- store-current-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ;
  ; @return (map)
  [db [_ route-string]]
  (r store-current-route-string! db route-string))

(defn- set-default-routes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (let [client-routes (r route-handler.subs/get-client-routes db)]
       ; A szerverről érkezett client-routes útvonalak magasabb prioritásúak,
       ; mint a DEFAULT-ROUTES útvonalak.
       ; Így lehetséges a szerver-oldalon beállított útvonalakkal felülírni
       ; a kliens-oldali DEFAULT-ROUTES útvonalakat.
       (assoc-in db [:router :route-handler/client-routes]
                    (merge route-handler.engine/DEFAULT-ROUTES client-routes))))

(defn- reg-to-history!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (map)
  [db [_ route-id]]
  (r db/apply! db [:router :route-handler/meta-items :history] vector/conj-item route-id))
