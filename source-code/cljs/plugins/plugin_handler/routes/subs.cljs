
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.routes.subs
    (:require [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                    :refer [r]]))



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
