
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.body.effects
    (:require [plugins.item-editor.body.events   :as body.events]
              [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.backup.events :as backup.events]
              [plugins.item-editor.backup.subs   :as backup.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ editor-id body-props]]
      ; A body.events/body-did-mount függvény eltárolja ...
      ; ... a body komponens paramétereit, ezért a core.subs/download-data? függvény
      ;     lefutása előtt szükséges meghívni!
      ; ... a body komponens paramétereit, ezért a core.subs/get-editor-title függvény
      ;     lefutása előtt szükséges meghívni!
      ; ... a body komponens számára esetlegesen átadott {:item-id "..."} paramétert,
      ;     ezért a core.subs/get-auto-title függvény lefutása előtt szükséges meghívni!
      (let [db (r body.events/body-did-mount db editor-id body-props)]
           {:db db :dispatch-n [(if-let [editor-title (r core.subs/get-editor-title db editor-id)]
                                        [:ui/set-window-title! editor-title])
                                [:item-editor/request-item! editor-id]]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      ; Az item-editor plugin – az elem törlése nélküli – elhagyásakor, ha az elem
      ; el nem mentett változtatásokat tartalmaz, akkor annak az utolsó állapotáról
      ; másolat készül, ami alapján lehetséges azt visszaállítani a változtatások-elvetése
      ; esemény visszavonásának esetleges megtörténtekor.
      (let [current-item-id (r core.subs/get-current-item-id db editor-id)
            item-changed?   (r backup.subs/item-changed?     db editor-id)
            item-deleted?   (r core.subs/get-meta-item       db editor-id :item-deleted?)]
           (if-not (and item-changed? (not item-deleted?))
                   {:db (as-> db % (r body.events/body-will-unmount      % editor-id))}
                   {:db (as-> db % (r backup.events/store-local-changes! % editor-id)
                                   (r body.events/body-will-unmount      % editor-id))
                    :dispatch [:item-editor/render-changes-discarded-dialog! editor-id current-item-id]}))))
