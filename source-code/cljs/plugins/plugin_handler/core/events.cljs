
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.core.events
    (:require [mid-fruits.candy                   :refer [return]]
              [mid-fruits.map                     :refer [dissoc-in]]
              [plugins.plugin-handler.body.subs   :as body.subs]
              [plugins.plugin-handler.core.subs   :as core.subs]
              [plugins.plugin-handler.routes.subs :as routes.subs]
              [re-frame.api                       :refer [r]]))



;; -- Meta-item events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-meta-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @return (map)
  [db [_ plugin-id item-key item-value]]
  (assoc-in db [:plugins :plugin-handler/meta-items plugin-id item-key] item-value))

(defn remove-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (dissoc-in db [:plugins :plugin-handler/meta-items plugin-id]))

(defn reset-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keywords in vector) keep-keys
  ;
  ; @return (map)
  [db [_ plugin-id keep-keys]]
  (update-in db [:plugins :plugin-handler/meta-items] select-keys keep-keys))



;; -- Mode events -------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) mode-key
  ;
  ; @return (map)
  [db [_ plugin-id mode-key]]
  (assoc-in db [:plugins :plugin-handler/meta-items plugin-id mode-key] true))

(defn quit-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) mode-key
  ;
  ; @return (map)
  [db [_ plugin-id mode-key]]
  (dissoc-in db [:plugins :plugin-handler/meta-items plugin-id mode-key]))



;; -- Query-param events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-query-param!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) param-key
  ; @param (*) param-value
  ;
  ; @return (map)
  [db [_ plugin-id param-key param-value]]
  ; XXX#7061
  (assoc-in db [:plugins :plugin-handler/meta-items plugin-id :query-params param-key] param-value))

(defn remove-query-param!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) param-key
  ;
  ; @return (map)
  [db [_ plugin-id param-key]]
  ; XXX#7061
  (dissoc-in db [:plugins :plugin-handler/meta-items plugin-id :query-params param-key]))



;; -- Current item events -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ plugin-id item-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items plugin-id :item-id] item-id))

(defn clear-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (dissoc-in db [:plugins :plugin-handler/meta-items plugin-id :item-id]))

(defn update-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  ; XXX#9143
  ; A plugin által aktuálisan megnyitandó elem azonosítójának különböző lehetséges
  ; forrásaiból, azok prioritása szerint aktualizálja a current-item-id értékét.
  ;
  ; A) Ha a plugin útvonal-vezérelt, akkor az aktuális elem azonosítójának forrása ...
  ;    1. az aktuális útvonal :item-id útvonal-paramétere
  ;    2. a body komponens {:item-id "..."} tulajdonsága.
  ;    3. a body komponens {:default-item-id "..."} tulajdonsága.
  ;
  ; B) Ha a plugin NEM útvonal-vezérelt, akkor az aktuális elem azonosítójának forrása ...
  ;    1. a plugin eseményei vagy a plugint használó modul eseményei által előre beállított érték.
  ;    2. a body komponens {:item-id "..."} tulajdonsága.
  ;    3. a body komponens {:default-item-id "..."} tulajdonsága.
  (if-let [route-handled? (r routes.subs/route-handled? db plugin-id)]
          ; A)
          (if-let [derived-item-id (r routes.subs/get-derived-item-id db plugin-id)]
                  (r set-item-id! db plugin-id derived-item-id)
                  (if-let [item-id (r body.subs/get-body-prop db plugin-id :item-id)]
                          (r set-item-id! db plugin-id item-id)
                          (let [default-item-id (r body.subs/get-body-prop db plugin-id :default-item-id)]
                               (r set-item-id! db plugin-id default-item-id))))
          ; B)
        (let []
          (if-let [current-item-id (r core.subs/get-current-item-id db plugin-id)]
                  (return db)
                  (if-let [item-id (r body.subs/get-body-prop db plugin-id :item-id)]
                          (r set-item-id! db plugin-id item-id)
                          (let [default-item-id (r body.subs/get-body-prop db plugin-id :default-item-id)]
                               (r set-item-id! db plugin-id default-item-id)))))))



;; -- Current view events -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) view-id
  ;
  ; @return (map)
  [db [_ plugin-id view-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items plugin-id :view-id] view-id))

(defn clear-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (dissoc-in db [:plugins :plugin-handler/meta-items plugin-id :view-id]))

(defn update-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  ; XXX#9143
  (if-let [route-handled? (r routes.subs/route-handled? db plugin-id)]
          ; A)
          (if-let [derived-view-id (r routes.subs/get-derived-view-id db plugin-id)]
                  (r set-view-id! db plugin-id derived-view-id)
                  (if-let [view-id (r body.subs/get-body-prop db plugin-id :view-id)]
                          (r set-view-id! db plugin-id view-id)
                          (let [default-view-id (r body.subs/get-body-prop db plugin-id :default-view-id)]
                               (r set-view-id! db plugin-id default-view-id))))
          ; B)
          (if-let [current-view-id (r core.subs/get-current-view-id db plugin-id)]
                  (return db)
                  (if-let [view-id (r body.subs/get-body-prop db plugin-id :view-id)]
                          (r set-view-id! db plugin-id view-id)
                          (let [default-view-id (r body.subs/get-body-prop db plugin-id :default-view-id)]
                               (r set-view-id! db plugin-id default-view-id))))))
