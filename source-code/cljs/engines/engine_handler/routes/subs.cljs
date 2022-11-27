
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.routes.subs
    (:require [candy.api                            :refer [return]]
              [engines.engine-handler.transfer.subs :as transfer.subs]
              [re-frame.api                         :refer [r]]
              [x.router.api                         :as x.router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-handled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  ; Ha az engine szerver-oldali kezelője megkapja paraméterként a {:base-route "..."}
  ; tulajdonságot, ...
  ; ... akkor hozzáadja az engine elindításához szükséges útvonal(ak)at a rendszerhez.
  ; ... akkor az engine útvonal-vezéreltnek számít.
  (let [base-route (r transfer.subs/get-transfer-item db engine-id :base-route)]
       (some? base-route)))



;; -- Route subscriptions -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-extended-route
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (string)
  [db [_ engine-id item-id]]
  ; Ha az engine a szerver-oldali inicializáláskor megkapta a {:base-route "..."} tulajdonságot,
  ; ami alapján hozzáadta az útvonalkezelőhöz az enginehez tartozó útvonal(ak)at ...
  ; ... és az engine kliens-oldali kezelője megkapja a {:base-route "..."} tulajdonságot.
  ; ... akkor a {:base-route "..."} tulajdonságból előállítható az extended-route útvonal.
  (if-let [base-route (r transfer.subs/get-transfer-item db engine-id :base-route)]
          (let [extended-route (str base-route "/" item-id)]
               (r x.router/use-path-params db extended-route))))



;; -- Derived item-id/view-id subscriptions -----------------------------------
;; ----------------------------------------------------------------------------

(defn get-derived-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (string)
  [db [_ _]]
  (if-let [item-id (r x.router/get-current-route-path-param db :item-id)]
          (return item-id)))

(defn get-derived-view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (keyword)
  [db [_ _]]
  (if-let [view-id (r x.router/get-current-route-path-param db :view-id)]
          (keyword view-id)))
