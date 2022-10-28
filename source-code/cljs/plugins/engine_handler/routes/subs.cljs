
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.engine-handler.routes.subs
    (:require [mid-fruits.candy                  :refer [return]]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [re-frame.api                      :refer [r]]
              [x.app-router.api                  :as x.router]))



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
  ; ami alapján hozzáadta az útvonalkezelőhöz a pluginhoz tartozó útvonal(ak)at ...
  ; ... és az engine kliens-oldali kezelője megkapja a {:base-route "..."} tulajdonságot.
  ; ... akkor a {:base-route "..."} tulajdonságból előállítható az extended-route útvonal.
  (if-let [base-route (r transfer.subs/get-transfer-item db engine-id :base-route)]
          (str base-route "/" item-id)))



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
