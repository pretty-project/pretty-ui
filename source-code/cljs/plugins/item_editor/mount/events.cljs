
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.mount.events
    (:require [mid-fruits.map                      :refer [dissoc-in]]
              [plugins.item-editor.core.events     :as core.events]
              [plugins.item-editor.download.events :as download.events]
              [x.app-core.api                      :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace header-props]]
  (assoc-in db [:plugins :item-editor/header-props extension-id] header-props))

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)
  ;   :initial-item (map)(opt)
  ;   :item-id (string)(opt)
  ;   :item-path (vector)}
  ;
  ; @return (map)
  [db [_ extension-id item-namespace {:keys [auto-title? initial-item item-id item-path] :as body-props}]]
  ; Az item-editor plugin body komponensének ...
  ; ... {:item-id "..."} tulajdonsága is lehet a szerkesztett elem azonosítójának forrása,
  ;     így lehetséges a szerkesztett dokumentum azonosítóját paraméterként átadni a plugin számára.
  ; ... {:initial-item {...}} tulajdonságával megadható a szerkesztett dokumentum kezdeti állapota ...
  ;     ... így elkerülhető az input mezők {:initial-value ...} tulajdonságának használata, ami miatt
  ;         a plugin felhasználói változtatás nélküli elhagyásakor az tévesen felajánlaná
  ;         a "Nem mentett változtatások visszaállítása" lehetőséget.
  ;     ... így beállíthatók a dokumentum felhasználó által nem szerkeszthető tulajdonságai.
  (cond-> db :store-body-props! (assoc-in [:plugins :item-editor/body-props extension-id] body-props)
             item-id            (assoc-in [:plugins :item-editor/meta-items extension-id :item-id] item-id)
             initial-item       (assoc-in item-path initial-item)
             auto-title?        (as-> % (r core.events/set-auto-title! % extension-id item-namespace))))

(defn header-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (dissoc-in db [:plugins :item-editor/header-props extension-id]))

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; A body komponens React-fából történő lecsatolásakor a {:handler-key ...} paraméter értékét
  ; nem törli a Re-Frame adatbázisból, mert az esetlegesen kitörölt elem visszaállításához
  ; szükséges a {:handler-key ...} tulajdonság ismerete a komponens lecsatolása után is!
  (as-> db % (r core.events/reset-meta-items!    % extension-id item-namespace)
             (r download.events/reset-downloads! % extension-id item-namespace)
             (dissoc-in % [:plugins :item-editor/body-props extension-id])
             (assoc-in  % [:plugins :item-editor/body-props extension-id]
                          (select-keys (get-in db [:plugins :item-editor/body-props extension-id])
                                       [:handler-key]))))
