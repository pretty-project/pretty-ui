
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.mount.events
    (:require [mid-fruits.map :refer [dissoc-in]]
              [x.app-core.api :refer [r]]))



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
  ;  {:item-id (string)(opt)}
  ;
  ; @return (map)
  [db [_ extension-id item-namespace {:keys [item-id] :as body-props}]]
  (cond-> db :store-body-props (assoc-in [:plugins :item-editor/body-props extension-id] body-props)
             item-id           (assoc-in [:plugins :item-editor/meta-items extension-id :item-id] item-id)))

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
  (dissoc-in db [:plugins :item-editor/body-props extension-id]))


;



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount_
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace header-props]])
  ; XXX#4036
  ; A header-props és body-props térképeket a header és body komponensek React fába csatolásakor
  ; merge függvény használatával szükséges tárolni, hogy a két térkép egymásba fésülve egy helyről
  ; legyen elérhető!
  ;(r db/apply-item! db [extension-id :item-editor/meta-items] merge header-props))

(defn body-did-mount_
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace body-props]])
  ; XXX#4036
  ;(r db/apply-item! db [extension-id :item-editor/meta-items] merge body-props))

(defn body-will-unmount_
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]])
  ; Az item-editor plugin elhagyásakor visszaállítja a plugin állapotát, így a következő betöltéskor
  ; az init-body! függvény lefutása előtt nem villan fel a legutóbbi állapot!



  ; EZT NEM OLDJA MEG A FELTÉTELES MEGJELNEÍTÉS? MÁRMINT KELL RESETELNI CSAK A LEÍRÁS NEM LESZ ÉRVÉNYES



  ;(r reset-editor! db extension-id item-namespace))
