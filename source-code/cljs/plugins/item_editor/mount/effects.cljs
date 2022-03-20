
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.mount.effects
    (:require [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.backup.events :as backup.events]
              [plugins.item-editor.backup.subs   :as backup.subs]
              [plugins.item-editor.mount.events  :as mount.events]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)}
  (fn [{:keys [db]} [_ extension-id item-namespace {:keys [auto-title?] :as body-props}]]
      ; A mount.events/body-did-mount függvény eltárolja ...
      ; ... a body komponens paramétereit, ezért a core.subs/download-data? függvény
      ;     lefutása előtt szükséges meghívni!
      ; ... a body komponens számára esetlegesen átadott {:item-id "..."} paramétert,
      ;     ezért a core.subs/get-auto-title függvény lefutása előtt szükséges meghívni!
      (let [db (r mount.events/body-did-mount db extension-id item-namespace body-props)]
           {:db db :dispatch-n [(if auto-title? [:ui/set-window-title! (r core.subs/get-auto-title db extension-id item-namespace)])
                                (if (r core.subs/download-data? db extension-id item-namespace)
                                    [:item-editor/request-item! extension-id item-namespace]
                                    [:item-editor/load-item!    extension-id item-namespace])]})))

(a/reg-event-fx
  :item-editor/header-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  (fn [{:keys [db]} [_ extension-id item-namespace header-props]]
      {:db (r mount.events/header-did-mount db extension-id item-namespace header-props)}))

(a/reg-event-fx
  :item-editor/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      ; Az item-editor plugin – az elem törlése nélküli – elhagyásakor, ha az elem
      ; el nem mentett változtatásokat tartalmaz, akkor annak az utolsó állapotáról
      ; másolat készül, ami alapján lehetséges azt visszaállítani a változtatások-elvetése
      ; esemény visszavonásának esetleges megtörténtekor.
      (let [current-item-id (r core.subs/get-current-item-id db extension-id item-namespace)
            item-changed?   (r backup.subs/item-changed?     db extension-id item-namespace)
            item-deleted?   (r core.subs/get-meta-item       db extension-id item-namespace :item-deleted?)]
           (if-not (and item-changed? (not item-deleted?))
                   {:db (as-> db % (r mount.events/body-will-unmount     % extension-id item-namespace))}
                   {:db (as-> db % (r backup.events/store-local-changes! % extension-id item-namespace)
                                   (r mount.events/body-will-unmount     % extension-id item-namespace))
                    :dispatch [:item-editor/render-changes-discarded-dialog! extension-id item-namespace current-item-id]}))))

(a/reg-event-fx
  :item-editor/header-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r mount.events/header-will-unmount db extension-id item-namespace)}))
