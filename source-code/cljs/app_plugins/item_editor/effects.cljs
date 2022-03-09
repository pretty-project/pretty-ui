
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.effects
    (:require [app-plugins.item-editor.engine     :as engine]
              [app-plugins.item-editor.events     :as events]
              [app-plugins.item-editor.queries    :as queries]
              [app-plugins.item-editor.subs       :as subs]
              [app-plugins.item-editor.validators :as validators]
              [app-plugins.item-editor.views      :as views]
              [x.app-core.api                     :as a :refer [r]]
              [x.app-ui.api                       :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [query        (r queries/get-request-item-query          db extension-id item-namespace)
            validator-f #(r validators/request-item-response-valid? db extension-id item-namespace %)]
           [:sync/send-query! (engine/request-id extension-id item-namespace)
                              {:display-progress? true
                               ; XXX#4057
                               ; Az on-stalled időzítéssel a UI változásai egyszerre történnek
                               ; meg a lekérés okozta {:editor-disabled? true} állapot megszűnésével
                               :on-stalled [:item-editor/receive-item!   extension-id item-namespace]
                               :on-failure [:item-editor/set-error-mode! extension-id item-namespace]
                               :query query :validator-f validator-f}])))

(a/reg-event-fx
  :item-editor/load-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (if (r subs/get-meta-item db extension-id item-namespace :recovery-mode?)
          {:db (r events/recover-item! db extension-id item-namespace)
           :dispatch [:ui/simulate-process!]}
          {:dispatch [:ui/simulate-process!]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/save-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/save-item! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      ; - Az új elemek hozzáadása (mentése), azért nem különálló [:item-editor/add-item! ...] eseménnyel
      ;   történik, mert az új elem szerver-oldali hozzáadása (kliens-oldali első mentése) utáni,
      ;   az aktuális szerkesztés közbeni további mentések, már nem számítának elem-hozzáadásnak,
      ;   miközben az item-editor plugin továbbra is "új elem hozzáadása" módban fut, ezért
      ;   nem tudná megkülönbözetni a további mentéseket a hozzáadástól (első mentés)!
      ; - Az elem esetleges törlése utáni – kliens-oldali adatból történő – visszaállításhoz
      ;   szükséges az elem feltételezett szerver-oldali állapotáról másolatot tárolni!
      ; - Az elem szerverre küldésének idejében az elemről másolat készítése feltételezi,
      ;   a mentés sikerességét. Sikertelen mentés esetén a kliens-oldali másolat eltérhet
      ;   a szerver-oldalon tárolt változattól, ami az elem törlése utáni visszaállítás esetén
      ;   pontatlan visszaálltást okozhat!
      (let [query        (r queries/get-save-item-query          db extension-id item-namespace)
            validator-f #(r validators/save-item-response-valid? db extension-id item-namespace %)]
           {:db (r events/save-item! db extension-id item-namespace)
            :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                         {:on-success [:item-editor/item-saved       extension-id item-namespace]
                                          :on-failure [:item-editor/save-item-failed extension-id item-namespace]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-editor/item-saved
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      (if-let [base-route (r subs/get-meta-item db extension-id item-namespace :base-route)]
              [:router/go-to! base-route]
              [:ui/end-fake-process!])))

(a/reg-event-fx
  :item-editor/save-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      ; Ha az elem mentése sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-save}]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/delete-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/delete-item! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [query        (r queries/get-delete-item-query          db extension-id item-namespace)
            validator-f #(r validators/delete-item-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                         {:on-success [:item-editor/item-deleted       extension-id item-namespace]
                                          :on-failure [:item-editor/delete-item-failed extension-id item-namespace]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-editor/item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r events/item-deleted db extension-id item-namespace)
       :dispatch-n [[:item-editor/render-item-deleted-dialog! extension-id item-namespace]
                    (if-let [base-route (r subs/get-meta-item db extension-id item-namespace :base-route)]
                            [:router/go-to! base-route]
                            [:ui/end-fake-process!])]}))

(a/reg-event-fx
  :item-editor/delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      ; Ha az elem törlése sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-delete}]]}))

(a/reg-event-fx
  :item-editor/undo-delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      (let [query        (r queries/get-undo-delete-item-query          db extension-id item-namespace item-id)
            validator-f #(r validators/undo-delete-item-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                         {:on-success [:item-editor/delete-item-undid       extension-id item-namespace item-id]
                                          :on-failure [:item-editor/undo-delete-item-failed extension-id item-namespace]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-editor/delete-item-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace item-id _]]
      {:db (r events/set-recovery-mode! db extension-id item-namespace)
       :dispatch-n [(if-let [item-route (r subs/get-item-route db extension-id item-namespace item-id)]
                            [:router/go-to! item-route]
                            [:ui/end-fake-process!])]}))

(a/reg-event-fx
  :item-editor/undo-delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      ; Ha az elem törlésének visszaállítása sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-undo-delete}]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/duplicate-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/duplicate-item! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [query        (r queries/get-duplicate-item-query          db extension-id item-namespace)
            validator-f #(r validators/duplicate-item-response-valid? db extension-id item-namespace %)]
           [:sync/send-query! (engine/request-id extension-id item-namespace)
                              {:display-progress? true
                               :on-success [:item-editor/item-duplicated       extension-id item-namespace]
                               :on-failure [:item-editor/duplicate-item-failed extension-id item-namespace]
                               :query query :validator-f validator-f}])))

(a/reg-event-fx
  :item-editor/item-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [copy-id (engine/server-response->copy-id extension-id item-namespace server-response)]
           [:item-editor/render-item-duplicated-dialog! extension-id item-namespace copy-id])))

(a/reg-event-fx
  :item-editor/duplicate-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      ; Ha az elem duplikálása sikertelen volt ...
      ; ... megjelenít egy értesítést
      [:ui/blow-bubble! {:body :failed-to-duplicate}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/undo-discard-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      {:db (r events/set-recovery-mode! db extension-id item-namespace)
       :dispatch [:item-editor/edit-item! extension-id item-namespace item-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/render-color-picker-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [_ [_ extension-id item-namespace]]
      [:ui/add-popup! (engine/dialog-id extension-id item-namespace :color-picker)
                      {:body [views/color-picker-dialog-body extension-id item-namespace]
                      ;:header #'ui/close-popup-header
                       :min-width :none}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/init-body!
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
      (let [db (r events/init-body! db extension-id item-namespace body-props)]
           {:db db :dispatch-n [(if (r subs/set-auto-title? db extension-id item-namespace)
                                    (if-let [auto-title (r subs/get-auto-title db extension-id item-namespace)]
                                            [:ui/set-title! auto-title]))
                                (if (r subs/download-data? db extension-id item-namespace)
                                    [:item-editor/request-item! extension-id item-namespace]
                                    [:item-editor/load-item!    extension-id item-namespace])]})))

(a/reg-event-fx
  :item-editor/init-header!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  (fn [{:keys [db]} [_ extension-id item-namespace header-props]]
      {:db (r events/init-header! db extension-id item-namespace header-props)}))

(a/reg-event-fx
  :item-editor/destruct-body!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r events/destruct-body! db extension-id item-namespace)}))

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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  :item-editor/load-editor!
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [route-title (r subs/get-meta-item db extension-id item-namespace :route-title)
            on-load     (r subs/get-meta-item db extension-id item-namespace :on-load)]
           {:db (if-not route-title db (r ui/set-header-title! db route-title))
            :dispatch-n [on-load (if route-title [:ui/set-window-title! route-title])]})))
