
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.routes.subs
    (:require [mid-fruits.candy                  :refer [return]]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                    :refer [r]]
              [x.app-router.api                  :as router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-extended-route
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-id
  ;
  ; @return (string)
  [db [_ plugin-id item-id]]
  ; Ha a plugin a szerver-oldali inicializáláskor megkapta a {:base-route "..."} tulajdonságot,
  ; ami alapján hozzáadta az útvonalkezelőhöz a pluginhoz tartozó útvonal(ak)at ...
  ; ... és a plugin kliens-oldali kezelője megkapja a {:base-route "..."} tulajdonságot.
  ; ... akkor a {:base-route "..."} tulajdonságból előállítható az extended-route útvonal.
  (if-let [base-route (r transfer.subs/get-transfer-item db plugin-id :base-route)]
          (str base-route "/" item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-derived-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (string)
  [db [_ _]]
  (if-let [item-id (r router/get-current-route-path-param db :item-id)]
          (return item-id)))

(defn get-derived-view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (keyword)
  [db [_ _]]
  (if-let [view-id (r router/get-current-route-path-param db :view-id)]
          (keyword view-id)))
