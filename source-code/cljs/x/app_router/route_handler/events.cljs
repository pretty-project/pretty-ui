
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.events
    (:require [mid-fruits.map                    :refer [dissoc-in]]
              [mid-fruits.vector                 :as vector]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-db.api                      :as db]
              [x.app-router.route-handler.config :as route-handler.config]
              [x.app-router.route-handler.subs   :as route-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-temporary-parent!
  ; @param (string) temporary-parent
  ;
  ; @usage
  ;  (r router/set-temporary-parent! db "/my-route")
  ;
  ; @return (map)
  [db [_ temporary-parent]]
  ; - A set-temporary-parent! függvény hanszálatával lehetséges az aktuális útvonal szűlő-útvonalát
  ;   ideiglenesen beállítani egy tetszőleges útvonalra
  ; - Az aktuális útvonal megváltozásakor az ideiglenesen beállított szűlő-útvonal törlődik
  (assoc-in db [:router :route-handler/meta-items :temporary-parent]
               (r route-handler.subs/get-resolved-uri db temporary-parent)))

(defn unset-temporary-parent!
  ; @usage
  ;  (r router/unset-temporary-parent! db)
  ;
  ; @return (map)
  [db _]
  (dissoc-in db [:router :route-handler/meta-items :temporary-parent]))



;; ----------------------------------------------------------------------------
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
                    (merge route-handler.config/DEFAULT-ROUTES client-routes))))

(defn- reg-to-history!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (map)
  [db [_ route-id]]
  (r db/apply-item! db [:router :route-handler/meta-items :history] vector/conj-item route-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:router/set-temporary-parent! "/my-route"]
(a/reg-event-db :router/set-temporary-parent! set-temporary-parent!)

; @usage
;  [:router/unset-temporary-parent!]
(a/reg-event-db :router/unset-temporary-parent! unset-temporary-parent!)
