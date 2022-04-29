
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.core.events
    (:require [mid-fruits.candy                   :refer [return]]
              [mid-fruits.map                     :refer [dissoc-in]]
              [plugins.plugin-handler.body.subs   :as body.subs]
              [plugins.plugin-handler.core.subs   :as core.subs]
              [plugins.plugin-handler.routes.subs :as routes.subs]
              [x.app-core.api                     :refer [r]]))



;; ----------------------------------------------------------------------------
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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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



;; ----------------------------------------------------------------------------
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

(defn set-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) view-id
  ;
  ; @return (map)
  [db [_ plugin-id view-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items plugin-id :view-id] view-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  ; XXX#9143
  ;
  ; A) Ha a plugin útvonal-vezérelt, akkor az aktuális elem azonosítójának forrása
  ;    az aktuális útvonal :item-id útvonal-paramétere, annak hiányában a body komponens
  ;    {:default-item-id "..."} tulajdonsága.
  ;
  ; B) Ha a plugin NEM útvonal-vezérelt, akkor az aktuális elem azonosítóját a plugin
  ;    eseményei vagy a plugint használó modul eseményei állíthatják be.
  ;    Ha az update-item-id! függvény alkalmazása előtt az aktuális elem azonosítójának
  ;    beállítása nem történt meg, akkor az azonosító forrása a body komponens {:default-item-id "..."}
  ;    tulajdonságának értéke.
  (if-let [route-handled? (r routes.subs/route-handled? db plugin-id)]
          ; A)
          (if-let [derived-item-id (r routes.subs/get-derived-item-id db plugin-id)]
                  (r set-item-id! db plugin-id derived-item-id)
                  (let [default-item-id (r body.subs/get-body-prop db plugin-id :default-item-id)]
                       (r set-item-id! db plugin-id default-item-id)))
          ; B)
          (if-let [current-item-id (r core.subs/get-current-item-id db plugin-id)]
                  (return db)
                  (let [default-item-id (r body.subs/get-body-prop db plugin-id :default-item-id)]
                       (r set-item-id! db plugin-id default-item-id)))))

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
                  (let [default-view-id (r body.subs/get-body-prop db plugin-id :default-view-id)]
                       (r set-view-id! db plugin-id default-view-id)))
          ; B)
          (if-let [current-view-id (r core.subs/get-current-view-id db plugin-id)]
                  (return db)
                  (let [default-view-id (r body.subs/get-body-prop db plugin-id :default-view-id)]
                       (r set-view-id! db plugin-id default-view-id)))))
