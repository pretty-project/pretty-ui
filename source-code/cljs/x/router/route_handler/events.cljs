
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.router.route-handler.events
    (:require [candy.api                      :refer [return]]
              [map.api                        :refer [dissoc-in]]
              [re-frame.api                   :as r :refer [r]]
              [uri.api                        :as uri]
              [vector.api                     :as vector]
              [x.db.api                       :as x.db]
              [x.router.route-handler.helpers :as route-handler.helpers]
              [x.router.route-handler.subs    :as route-handler.subs]
              [x.ui.api                       :as x.ui]))



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
  (let [client-routes  (r route-handler.subs/get-client-routes db)
        ordered-routes (-> client-routes route-handler.helpers/routes->destructed-routes
                                         route-handler.helpers/destructed-routes->ordered-routes)]
       (assoc-in db [:x.router :route-handler/ordered-routes] ordered-routes)))

(defn init-router!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (r order-client-routes! db))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-current-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (string) route-string
  ;
  ; @return (map)
  [db [_ route-id route-string]]
  ; A store-current-route! függvény a route-string paraméterből származtatott értékeket
  ; az útvonal kezelésekor eltárolja, így az ezekre az értékekre történő Re-Frame feliratkozások
  ; kevesebb erőforrást igényelnek, mint ha a feliratkozás függvényekben történne az értékek származtatása.
  (let [route-template     (get-in db [:x.router :route-handler/client-routes route-id :route-template])
        route-fragment     (uri/to-fragment     route-string)
        route-path         (uri/to-path         route-string)
        route-path-params  (uri/to-path-params  route-string route-template)
        route-query-params (uri/to-query-params route-string)]
       (-> db (assoc-in [:x.router :route-handler/meta-items :route-id]           route-id)
              (assoc-in [:x.router :route-handler/meta-items :route-fragment]     route-fragment)
              (assoc-in [:x.router :route-handler/meta-items :route-path]         route-path)
              (assoc-in [:x.router :route-handler/meta-items :route-path-params]  route-path-params)
              (assoc-in [:x.router :route-handler/meta-items :route-query-params] route-query-params)
              (assoc-in [:x.router :route-handler/meta-items :route-string]       route-string)
              ; XXX#4981
              ; Az aktuális útvonalhoz beállított szülő-útvonal értékének forrása...
              ; 1. ... [:x.router/go-to! ...] esemény számára átadott route-props térkép {:parent-route "..."} tulajdonsága,
              ;    amely tulajdonság {:virtual-parent "..."} tulajdonságként kerül ideiglenesen eltárolásra,
              ;    a [:x.router/go-to! ...] esemény és a [:x.router/handle-route! ...] esemény megtörténése között.
              ; 2. ... az útvonal szerver-oldali hozzáadásakor tulajdonságaként beállított {:parent-route "..."}
              ;        értéke.
              ; 3. ... a route-string értékéből származtatott szülő-útvonal.
              (assoc-in  [:x.router :route-handler/meta-items :parent-route]
                         (or (get-in db [:x.router :route-handler/meta-items :virtual-parent])
                             (get-in db [:x.router :route-handler/client-routes route-id :parent-route])
                             (uri/to-parent route-string)))
              (dissoc-in [:x.router :route-handler/meta-items :virtual-parent]))))

(defn reg-to-history!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (string) route-string
  ;
  ; @return (map)
  [db [_ route-id route-string]]
  (r x.db/apply-item! db [:x.router :route-handler/meta-items :history] vector/conj-item [route-string route-id]))

(defn configure-ui!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-id
  ; @param (string) route-string
  ;
  ; @return (map)
  [db [_ route-id _]]
  ; XXX#5670
  (if-let [js-build (r route-handler.subs/get-current-js-build db)]
          (case js-build :app  (r x.ui/set-interface! db :application-ui)
                         :site (r x.ui/set-interface! db :website-ui))
          (return db)))

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
  ; ... eltárolja az aktuális route-id azonosítót.
  ; ...
  (as-> db % (r store-current-route! % route-id route-string)
             (r reg-to-history!      % route-id route-string)
             (r configure-ui!        % route-id route-string)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-swap-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  ; XXX#0781 (source-code/cljs/x/router/route_handler/effects.cljs)
  ; Az útvonal használata előtt az útvonal-kezelőt {:swap-mode? true} állapotba állítja,
  ; ezért a kezelő figyelmen kívül hagyja az útvonalhoz hozzárendelt eseményeket.
  (assoc-in db [:x.router :route-handler/meta-items :swap-mode?] true))

(defn quit-swap-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  ; XXX#0781 (source-code/cljs/x/router/route_handler/effects.cljs)
  (dissoc-in db [:x.router :route-handler/meta-items :swap-mode?]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-to!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  ; @param (map) route-props
  ;  {:parent-route (string)(opt)}
  ;
  ; @return (map)
  [db [_ _ {:keys [parent-route]}]]
  ; XXX#4981
  ; Ha a [:x.router/go-to! ...] esemény számára átadott route-props térkép tatalmazza a {:parent-route "..."}
  ; tulajdonságot, akkor a (r go-to! ...) függvény eltárolja azt {:virtual-parent "..."} tulajdonságként,
  ; hogy a (r handle-route! ...) függvény az útvonal kezelésekor felülírhassa vele az útvonalhoz tartozó
  ; {:parent-route "..."} tulajdonság értékét.
  (if parent-route (assoc-in db [:x.router :route-handler/meta-items :virtual-parent] parent-route)
                   (return   db)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.router/store-current-route! store-current-route!)
