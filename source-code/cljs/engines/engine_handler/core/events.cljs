
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.core.events
    (:require [engines.engine-handler.body.subs   :as body.subs]
              [engines.engine-handler.core.subs   :as core.subs]
              [engines.engine-handler.routes.subs :as routes.subs]
              [mid-fruits.candy                   :refer [return]]
              [mid-fruits.map                     :refer [dissoc-in]]
              [re-frame.api                       :refer [r]]))



;; -- Meta-item events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-meta-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @return (map)
  [db [_ engine-id item-key item-value]]
  (assoc-in db [:engines :engine-handler/meta-items engine-id item-key] item-value))

(defn remove-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (dissoc-in db [:engines :engine-handler/meta-items engine-id]))

(defn reset-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keywords in vector) keep-keys
  ;
  ; @return (map)
  [db [_ engine-id keep-keys]]
  (update-in db [:engines :engine-handler/meta-items] select-keys keep-keys))



;; -- Mode events -------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) mode-key
  ;
  ; @return (map)
  [db [_ engine-id mode-key]]
  (assoc-in db [:engines :engine-handler/meta-items engine-id mode-key] true))

(defn quit-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) mode-key
  ;
  ; @return (map)
  [db [_ engine-id mode-key]]
  (dissoc-in db [:engines :engine-handler/meta-items engine-id mode-key]))



;; -- Error events ------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-engine-error!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) error-id
  ;
  ; @return (map)
  [db [_ engine-id error-id]]
  (assoc-in db [:engines :engine-handler/meta-items engine-id :engine-error] error-id))



;; -- Query-param events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-query-param!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) param-key
  ; @param (*) param-value
  ;
  ; @return (map)
  [db [_ engine-id param-key param-value]]
  ; XXX#7061
  (assoc-in db [:engines :engine-handler/meta-items engine-id :query-params param-key] param-value))

(defn remove-query-param!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) param-key
  ;
  ; @return (map)
  [db [_ engine-id param-key]]
  ; XXX#7061
  (dissoc-in db [:engines :engine-handler/meta-items engine-id :query-params param-key]))



;; -- Current item events -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ engine-id item-id]]
  (assoc-in db [:engines :engine-handler/meta-items engine-id :item-id] item-id))

(defn clear-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (dissoc-in db [:engines :engine-handler/meta-items engine-id :item-id]))

(defn update-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  ; XXX#9143
  ; Az engine által aktuálisan megnyitandó elem azonosítójának különböző lehetséges
  ; forrásaiból, azok prioritása szerint aktualizálja a current-item-id értékét.
  ;
  ; A) Ha az engine útvonal-vezérelt, akkor az aktuális elem azonosítójának forrása ...
  ;    1. az aktuális útvonal :item-id útvonal-paramétere
  ;    2. a body komponens {:item-id "..."} tulajdonsága.
  ;    3. a body komponens {:default-item-id "..."} tulajdonsága.
  ;
  ; B) Ha az engine NEM útvonal-vezérelt, akkor az aktuális elem azonosítójának forrása ...
  ;    1. az engine eseményei vagy a engine-t használó modul eseményei által előre beállított érték.
  ;    2. a body komponens {:item-id "..."} tulajdonsága.
  ;    3. a body komponens {:default-item-id "..."} tulajdonsága.
  (if-let [route-handled? (r routes.subs/route-handled? db engine-id)]
          ; A)
          (if-let [derived-item-id (r routes.subs/get-derived-item-id db engine-id)]
                  (r set-item-id! db engine-id derived-item-id)
                  (if-let [item-id (r body.subs/get-body-prop db engine-id :item-id)]
                          (r set-item-id! db engine-id item-id)
                          (let [default-item-id (r body.subs/get-body-prop db engine-id :default-item-id)]
                               (r set-item-id! db engine-id default-item-id))))
          ; B)
        (let []
          (if-let [current-item-id (r core.subs/get-current-item-id db engine-id)]
                  (return db)
                  (if-let [item-id (r body.subs/get-body-prop db engine-id :item-id)]
                          (r set-item-id! db engine-id item-id)
                          (let [default-item-id (r body.subs/get-body-prop db engine-id :default-item-id)]
                               (r set-item-id! db engine-id default-item-id)))))))



;; -- Current view events -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) view-id
  ;
  ; @return (map)
  [db [_ engine-id view-id]]
  (assoc-in db [:engines :engine-handler/meta-items engine-id :view-id] view-id))

(defn clear-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (dissoc-in db [:engines :engine-handler/meta-items engine-id :view-id]))

(defn update-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  ; XXX#9143
  (if-let [route-handled? (r routes.subs/route-handled? db engine-id)]
          ; A)
          (if-let [derived-view-id (r routes.subs/get-derived-view-id db engine-id)]
                  (r set-view-id! db engine-id derived-view-id)
                  (if-let [view-id (r body.subs/get-body-prop db engine-id :view-id)]
                          (r set-view-id! db engine-id view-id)
                          (let [default-view-id (r body.subs/get-body-prop db engine-id :default-view-id)]
                               (r set-view-id! db engine-id default-view-id))))
          ; B)
          (if-let [current-view-id (r core.subs/get-current-view-id db engine-id)]
                  (return db)
                  (if-let [view-id (r body.subs/get-body-prop db engine-id :view-id)]
                          (r set-view-id! db engine-id view-id)
                          (let [default-view-id (r body.subs/get-body-prop db engine-id :default-view-id)]
                               (r set-view-id! db engine-id default-view-id))))))
