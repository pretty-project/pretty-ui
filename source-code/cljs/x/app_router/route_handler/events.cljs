
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.events
    (:require [mid-fruits.candy                   :refer [return]]
              [mid-fruits.map                     :refer [dissoc-in]]
              [mid-fruits.uri                     :as uri]
              [mid-fruits.vector                  :as vector]
              [re-frame.api                       :refer [r]]
              [x.app-db.api                       :as db]
              [x.app-router.route-handler.config  :as route-handler.config]
              [x.app-router.route-handler.helpers :as route-handler.helpers]
              [x.app-router.route-handler.subs    :as route-handler.subs]
              [x.app-ui.api                       :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-swap-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (assoc-in db [:router :route-handler/meta-items :swap-mode?] true))

(defn quit-swap-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (assoc-in db [:router :route-handler/meta-items :swap-mode?] false))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
        route-query-params (uri/uri->query-params route-string)]
       (-> db (assoc-in [:router :route-handler/meta-items :route-id]           route-id)
              (assoc-in [:router :route-handler/meta-items :route-fragment]     route-fragment)
              (assoc-in [:router :route-handler/meta-items :route-path]         route-path)
              (assoc-in [:router :route-handler/meta-items :route-path-params]  route-path-params)
              (assoc-in [:router :route-handler/meta-items :route-query-params] route-query-params)
              (assoc-in [:router :route-handler/meta-items :route-string]       route-string)
              ; XXX#4981
              ; Az aktuális útvonalhoz beállított szülő-útvonal értékének forrása...
              ; 1. ... [:router/go-to! ...] esemény számára átadott route-props térkép {:route-parent "..."} tulajdonsága,
              ;    amely tulajdonság {:virtual-parent "..."} tulajdonságként kerül ideiglenesen eltárolásra,
              ;    a [:router/go-to! ...] esemény és a [:router/handle-route! ...] esemény megtörténése között.
              ; 2. ... az útvonal szerver-oldali hozzáadásakor tulajdonságaként beállított {:route-parent "..."}
              ;        értéke.
              ; 3. ... a route-string értékéből származtatott szülő-útvonal.
              (assoc-in  [:router :route-handler/meta-items :route-parent]
                         (or (get-in db [:router :route-handler/meta-items :virtual-parent])
                             (get-in db [:router :route-handler/client-routes route-id :route-parent])
                             (uri/uri->parent-uri route-string)))
              (dissoc-in [:router :route-handler/meta-items :virtual-parent]))))

(defn reg-to-history!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ;
  ; @return (map)
  [db [_ route-id]]
  (r db/apply-item! db [:router :route-handler/meta-items :history] vector/conj-item route-id))

(defn configure-ui!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-id
  ;
  ; @return (map)
  [db [_ route-id]]
  ; XXX#5670
  (let [js-build (r route-handler.subs/get-current-js-build db)]
       (case js-build :app  (r ui/set-interface! db :application-ui)
                      :site (r ui/set-interface! db :website-ui))))

(defn handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (string) route-string
  ;
  ; @return (map)
  [db [_ route-id route-string]]
  ; A handle-route! függvény ...
  ; ... eltárolja az aktuális route-string paraméterből származtatott értékeket.
  ; ... eltárolja az aktuális route-id azonosítót
  ; ... kilépteti az útvonal-kezelőt az esetlegesen beállított {:swap-mode? true} állapotból.
  (as-> db % (r store-current-route! % route-string)
             (r reg-to-history!      % route-id)
             (r quit-swap-mode!      % route-id)
             (r configure-ui!        % route-id)))

(defn go-to!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ; @param (map) route-props
  ;  {:route-parent (string)(opt)}
  ;
  ; @return (map)
  [db [_ _ {:keys [route-parent]}]]
  ; XXX#4981
  ; Ha a [:router/go-to! ...] esemény számára átadott route-props térkép tatalmazza a {:route-parent "..."}
  ; tulajdonságot, akkor a (r go-to! ...) függvény eltárolja azt {:virtual-parent "..."} tulajdonságként,
  ; hogy a (r handle-route! ...) függvény az útvonal kezelésekor felülírhassa vele az útvonalhoz tartozó
  ; {:route-parent "..."} tulajdonság értékét.
  (if route-parent (assoc-in db [:router :route-handler/meta-items :virtual-parent] route-parent)
                   (return   db)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn order-client-routes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  ; A konfliktus-hierarchia szerinti sorrendbe rendezett útvonalakat az útvonal-kezelő indulásakor
  ; szükséges eltárolni, hogy ne kelljen az egyes útvonalak kezelésekor ezt a vektroba rendezett
  ; adatszerkezetet újra és újra létrehozni.
  (let [client-routes     (r route-handler.subs/get-client-routes db)
        destructed-routes (route-handler.helpers/routes->destructed-routes         client-routes)
        ordered-routes    (route-handler.helpers/destructed-routes->ordered-routes destructed-routes)]
       (assoc-in db [:router :route-handler/ordered-routes] ordered-routes)))

(defn init-router!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (as-> db % (r set-default-routes!  % route-handler.config/DEFAULT-ROUTES)
             (r order-client-routes! %)))
