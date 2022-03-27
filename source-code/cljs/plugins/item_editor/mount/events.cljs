
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.mount.events
    (:require [mid-fruits.map                      :refer [dissoc-in]]
              [plugins.item-editor.core.events     :as core.events]
              [plugins.item-editor.download.events :as download.events]
              [plugins.plugin-handler.mount.events :as mount.events]
              [x.app-core.api                      :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.mount.events
(def store-body-props!    mount.events/store-body-props!)
(def store-header-props!  mount.events/store-header-props!)
(def remove-body-props!   mount.events/remove-body-props!)
(def remove-header-props! mount.events/remove-header-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ editor-id header-props]]
  (r store-header-props! db editor-id header-props))

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;  {:initial-item (map)(opt)
  ;   :item-id (string)(opt)
  ;   :item-path (vector)}
  ;
  ; @return (map)
  [db [_ editor-id {:keys [initial-item item-id item-path] :as body-props}]]
  ; Az item-editor plugin body komponensének ...
  ; ... {:item-id "..."} tulajdonsága is lehet a szerkesztett elem azonosítójának forrása,
  ;     így lehetséges a szerkesztett dokumentum azonosítóját a body komponens paramétereként is átadni.
  ; ... {:initial-item {...}} tulajdonságával megadható a szerkesztett dokumentum kezdeti állapota ...
  ;     ... így elkerülhető az input mezők {:initial-value ...} tulajdonságának használata, ami miatt
  ;         a plugin felhasználói változtatás nélküli elhagyásakor az tévesen felajánlaná
  ;         a "Nem mentett változtatások visszaállítása" lehetőséget.
  ;     ... így beállíthatók a dokumentum felhasználó által nem szerkeszthető tulajdonságai.
  (cond-> db :store-body-props! (as-> % (r store-body-props! % editor-id body-props))
             item-id            (assoc-in [:plugins :plugin-handler/meta-items editor-id :item-id] item-id)
             initial-item       (assoc-in item-path initial-item)))

(defn header-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (r remove-header-props! db editor-id))

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (as-> db % (r core.events/remove-meta-items!   % editor-id)
             (r download.events/reset-downloads! % editor-id)
             (r remove-body-props!               % editor-id)))
