
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.effects
    (:require [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.core.events   :as core.events]
              [plugins.item-editor.download.subs :as download.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  :item-editor/load-editor!
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [on-load (r core.subs/get-meta-item db extension-id item-namespace :on-load)]
           {:db (r core.events/load-editor! db extension-id item-namespace)
            :dispatch on-load})))



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
  (fn [{:keys [db]} [_ extension-id item-namespace body-props]]
      ; - Az events/init-body! függvény által meghívott events/reset-editor! függvény lépteti ki az item-editor
      ;   plugint a {:recovery-mode? true} állapotból, ami miatt szükséges az events/init-body! függvényt
      ;   a subs/download-data? függvény lefutása előtt meghívni!
      ; - Az events/init-body! függvény által meghívott events/store-body-props! függvény tárolja el
      ;   az {:auto-title? ...} beállítást, ami miatt szükséges az events/init-body! függvényt
      ;   a subs/set-auto-title? függvény lefutása előtt meghívni!
      (let [db (r core.events/body-did-mount db extension-id item-namespace body-props)]
           {:db db :dispatch-n [(if (r core.subs/set-auto-title? db extension-id item-namespace)
                                    (if-let [auto-title (r core.subs/get-auto-title db extension-id item-namespace)]
                                            [:ui/set-title! auto-title]))
                                (if (r download.subs/download-data? db extension-id item-namespace)
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
      {:db (r core.events/header-did-mount db extension-id item-namespace header-props)}))

(a/reg-event-fx
  :item-editor/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r core.events/body-will-unmount db extension-id item-namespace)}))

      ; BUG#4055
      ; - Ha az item-editor plugin {:initial-value ...} tulajdonsággal rendelkező input elemet
      ;   tartalmaz, akkor új elem létrehozása után a szerkesztő elhagyásakor abban az esetben is
      ;   megjelenítenne a changes-discarded értesítés, ha nem történt szerkesztés, mivel
      ;   az {:initial-value ...} tulajdonság módosítja az üres adatot (az új elemet).
      ; - Ha az item-editor plugin olyan felületen jelenik meg, ahol tabok (fülek) vannak,
      ;   akkor egy másik tabra (fülre) kattintva az {:component-did-unmount ...} esemény
      ;   által meghívott [:item-editor/destruct-body! ...] esemény megtörténése miatt megjelenne
      ;   a changes-discarded értesítés, abban az esetben is ha a szerkesztő nem lett elhagyva.
      ; - A backup-current-item! DB függvény is foglalkozik a törlés visszavonhatóságával!
      ;
      ; HA A CHANGES-DISCARDED VISSZAÁLLÍTÁSRA KERÜL:
      ; - El nem mentett változtatásokkal törölt elem törlése utáni kilépéskor NEM szükséges
      ;   kirenderelni changes-discarded-dialog párbeszédablakot.
      ; - A {:item-deleted? true} beállítás használatával az [:item-editor/editor-leaved ...]
      ;   esemény képes megállapítani, hogy szükséges-e kirenderelni a changes-discarded-dialog
      ;   párbeszédablakot.
      ; - Ezt szükséges visszatenni az item-deleted eseménybe:
      ;   (assoc-in db [extension-id :item-editor/meta-items :item-deleted?] true)
      ; - A reset-editor! függvényben szükséges dissoc-olni az {:item-delete? true} beállítást

      ; Az item-editor plugin – az elem törlése nélküli – elhagyásakor, ha az elem
      ; el nem mentett változtatásokat tartalmaz, akkor annak az utolsó állapotáról
      ; másolat készül, ami alapján lehetséges azt visszaállítani a változtatások-elvetése
      ; esemény visszavonásának esetleges megtörténtekor.
      ;(if-let [item-deleted? (r subs/get-meta-item db extension-id item-namespace :item-deleted?)]
      ;(if (r subs/item-changed? db extension-id item-namespace)
      ;    {:db (r events/store-local-changes! db extension-id item-namespace)
      ;     :dispatch [:item-editor/render-changes-discarded-dialog! extension-id item-namespace]})))
