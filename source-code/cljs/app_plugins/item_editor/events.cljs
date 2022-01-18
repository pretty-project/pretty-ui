
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.9.8
; Compatibility: x4.5.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.events
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.map       :as map :refer [dissoc-in]]
              [mid-fruits.validator :as validator]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-ui.api         :as ui]
              [app-plugins.item-editor.engine  :as engine]
              [app-plugins.item-editor.queries :as queries]
              [app-plugins.item-editor.subs    :as subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  ; Az item-editor plugin betöltésekor gondoskodni kell, arról hogy az előző betöltéskor
  ; esetlegesen beállított {:error-mode? true} beállítás törlődjön!
  (assoc-in db [extension-id :item-editor/meta-items :error-mode?] true))

(defn reset-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (-> db (dissoc-in [extension-id :item-editor/data-items])
         ; A {:recovery-mode? ...} tulajdonság kivételével törli az :item-editor/meta-items térképet
         (update-in [extension-id :item-editor/meta-items] select-keys [:recovery-mode?])))

(defn store-editor-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;
  ; @return (map)
  [db [_ extension-id _ editor-props]]
  ; XXX#8705
  (update-in db [extension-id :item-editor/meta-items] map/reverse-merge editor-props))

(defn backup-current-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  ; - Az egyes elemek aktuális változatáról készített másolatok az elem azonosítójával vannak
  ;   tárolva. Így egy időben több elemről is lehetséges másolatot tárolni.
  ; - A gyors egymás utánban kitörölt elemek törlésének visszavonhatósága időbeni átfedésbe
  ;   kerülhet egymással, amiért szükséges az egyes elemekről készült másolatokat azonosítóval
  ;   megkülönböztetve kezelni és tárolni.
  ; - A gyors egymás utánban elvetett szerkesztett elemek elvetésének visszavonhatósága időbeni
  ;   átfedésbe kerülhet egymással, ...
  (let [current-item-id (r subs/get-current-item-id db extension-id)
        current-item    (r subs/get-current-item    db extension-id)]
       (assoc-in db [extension-id :item-editor/backup-items current-item-id] current-item)))

(defn store-local-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r subs/get-current-item-id db extension-id)
        current-item    (r subs/get-current-item    db extension-id)
        backup-item     (r subs/get-backup-item     db extension-id item-namespace current-item-id)
        local-changes   (map/difference current-item backup-item)]
       (assoc-in db [extension-id :item-editor/local-changes current-item-id] local-changes)))

(defn clean-recovery-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  ; Az egyes elemek törlésének vagy az elem nem mentett változtatásainak elvetésének lehetősége
  ; megszűnésekor a visszaállításhoz szükséges másolatok szükségtelenné válnak és törlődnek.
  (if-not (r subs/editing-item? db extension-id item-namespace item-id)
          ; Ha clean-recovery-data! függvény lefutásának pillanatában ismételten az item-id
          ; azonosítójú elem van megnyitva a szerkesztőben, akkor a másolatok eltávolítása szükségtelen!
          (-> db (dissoc-in [extension-id :item-editor/backup-items  item-id])
                 (dissoc-in [extension-id :item-editor/local-changes item-id]))
          (return db)))

(a/reg-event-db :item-editor/clean-recovery-data! clean-recovery-data!)

(defn set-recovery-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  ; A {:recovery-mode? true} beállítással elindítitott item-editor plugin, visszaállítja az elem
  ; eltárolt változtatásait
  (assoc-in db [extension-id :item-editor/meta-items :recovery-mode?] true))

(defn set-delete-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  ; XXX#5610
  ; - El nem mentett változtatásokkal törölt elem törlése utáni kilépéskor NEM szükséges
  ;   kirenderelni changes-discarded-dialog párbeszédablakot.
  ; - A {:delete-mode? true} beállítás használatával az [:item-editor/->editor-leaved ...]
  ;   esemény képes megállapítani, hogy szükséges-e kirenderelni a changes-discarded-dialog
  ;   párbeszédablakot.
  (assoc-in db [extension-id :item-editor/meta-items :delete-mode?] true))

(defn store-downloaded-suggestions!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id _ server-response]]
  (let [suggestions (get server-response :item-editor/get-item-suggestions)]
       (if (validator/data-valid? suggestions)
           ; If the received suggestions is valid ...
           (let [suggestions (validator/clean-validated-data suggestions)]
                (assoc-in db [extension-id :item-editor/meta-items :suggestions] suggestions))
           ; If the received suggestions is NOT valid ...
           (r set-error-mode! db extension-id))))

(defn store-downloaded-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (let [resolver-id (engine/resolver-id extension-id item-namespace :get)
        document    (get server-response resolver-id)]
       (if (validator/data-valid? document)
           ; XXX#3907
           ; Az item-lister pluginnal megegyezően az item-editor plugin is névtér nélkül tárolja
           ; a letöltött dokumentumot
           (let [document (-> document validator/clean-validated-data db/document->non-namespaced-document)]
                (as-> db % (assoc-in % [extension-id :item-editor/data-items] document)
                           (r backup-current-item! % extension-id)))
           ; If the received document is NOT valid ...
           (assoc-in db [extension-id :item-editor/meta-items :error-mode?] true))))

(defn store-current-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (let [derived-item-id (r subs/get-derived-item-id db extension-id)]
       (assoc-in db [extension-id :item-editor/meta-items :item-id] derived-item-id)))

(defn recover-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r subs/get-current-item-id db extension-id)
        recovered-item  (r subs/get-recovered-item  db extension-id item-namespace)]
       (-> db (assoc-in  [extension-id :item-editor/data-items] recovered-item)
              (dissoc-in [extension-id :item-editor/meta-items :recovery-mode?])
              (dissoc-in [extension-id :item-editor/local-changes current-item-id]))))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [event-id extension-id item-namespace server-response]]
  (cond-> db (r subs/download-item?                db extension-id item-namespace)
             (store-downloaded-item!        [event-id extension-id item-namespace server-response])
             (r subs/download-suggestions?         db extension-id item-namespace)
             (store-downloaded-suggestions! [event-id extension-id item-namespace server-response])
             (r subs/get-meta-item                 db extension-id item-namespace :recovery-mode?)
             (recover-item!                 [event-id extension-id item-namespace])
             :->item-received (assoc-in [extension-id :item-editor/meta-items :item-received?] true)))

(a/reg-event-db :item-editor/receive-item! receive-item!)

(defn load-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace editor-props]]
  (let [request-id (engine/request-id extension-id item-namespace)]
       (as-> db % (r ui/listen-to-process!  % request-id)
                  (r reset-editor!          % extension-id item-namespace)
                  (r store-current-item-id! % extension-id)
                  (r store-editor-props!    % extension-id item-namespace editor-props))))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn edit-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  [_ [_ extension-id item-namespace item-id]]
  (let [editor-uri (engine/editor-uri extension-id item-namespace item-id)]
       [:router/go-to! editor-uri]))

(a/reg-event-fx :item-editor/edit-item! edit-item!)

(defn go-up!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  [_ [_ extension-id]]
  (let [parent-uri (engine/parent-uri extension-id)]
       [:router/go-to! parent-uri]))

(a/reg-event-fx
  :item-editor/save-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db] :as cofx} [_ extension-id item-namespace]]
      ; - Az új elemek hozzáadása (mentése), azért nem külön [:item-editor/add-item! ...] eseménnyel
      ;   történik, mert az új elem hozzáadása (első mentése) utáni, az aktuális szerkesztés közbeni
      ;   további mentések, már nem számítának elem-hozzáadásnak, de a címsorból kiolvasott útvonal
      ;   alapján az item-editor plugin "új elem hozzáadása" módban fut!
      ; - Az elem esetleges törlése utáni – kliens-oldali adatból történő – visszaállításhoz
      ;   szükséges az elem feltételezett szerver-oldali állapotáról másolatot tárolni!
      ; - Az elem szerverre küldésének idejében az elemről másolat készítése feltételezi,
      ;   a mentés sikerességét. Sikertelen mentés esetén a kliens-oldali másolat eltérhet
      ;   a szerver-oldalon tárolt változattól, ami az elem törlése utáni visszaállítás esetén
      ;   pontatlan visszaálltást okozhat!
      {:db       (r backup-current-item! db extension-id)
       :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                    ; XXX#3701
                                    ; Az on-success helyett on-stalled időzítés használatával elkerülhető,
                                    ; hogy a felhasználói felület változásai túlságosan gyorsan kövessék
                                    ; egymást, megnehezítve a felhasználó számára a események megértését
                                   {:on-stalled (r go-up! cofx extension-id)
                                    :on-failure [:ui/blow-bubble! {:body {:content :failed-to-save}}]
                                    :query      (r queries/get-save-item-query db extension-id item-namespace)}]}))

(a/reg-event-fx
  :item-editor/delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      [:sync/send-query! (engine/request-id extension-id item-namespace)
                          ; XXX#3701
                         {:on-stalled [:item-editor/->item-deleted extension-id item-namespace]
                          :on-failure [:ui/blow-bubble! {:body {:content :failed-to-delete}}]
                          :query      (r queries/get-delete-item-query db extension-id item-namespace)}]))

(a/reg-event-fx
  :item-editor/undo-delete!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      [:sync/send-query! (engine/request-id extension-id item-namespace)
                         {:on-success [:item-editor/->delete-undid extension-id item-namespace item-id]
                          :on-failure [:ui/blow-bubble! {:body {:content :failed-to-undo-delete}}]
                          :query      (r queries/get-undo-delete-query db extension-id item-namespace item-id)}]))

(a/reg-event-fx
  :item-editor/duplicate-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      [:sync/send-query! (engine/request-id extension-id item-namespace)
                         {:on-success [:item-editor/->item-duplicated extension-id item-namespace]
                          :on-failure [:ui/blow-bubble! {:body {:content :failed-to-copy}}]
                          :query      (r queries/get-duplicate-item-query db extension-id item-namespace)}]))

(a/reg-event-fx
  :item-editor/undo-discard!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db] :as cofx} [_ extension-id item-namespace item-id]]
      {:db       (r set-recovery-mode! db   extension-id)
       :dispatch (r edit-item!         cofx extension-id item-namespace item-id)}))

(a/reg-event-fx
  :item-editor/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (if (r subs/download-data? db extension-id item-namespace)
          [:sync/send-query! (engine/request-id extension-id item-namespace)
                             {:on-success [:item-editor/receive-item!          extension-id item-namespace]
                              :query      (r queries/get-request-item-query db extension-id item-namespace)}]
          ; Ha az elem szerkesztéséhez nincs szükség adatok letöltéséhez, akkor is szükséges
          ; a szerkesztőt {:item-received? true} állapotba léptetni!
          {:db (assoc-in db [extension-id :item-editor/meta-items :item-received?] true)})))

(a/reg-event-fx
  :item-editor/load-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:label (metamorphic-content)(opt)
  ;   :suggestion-keys (keywords in vector)(opt)}
  (fn [{:keys [db]} [_ extension-id item-namespace editor-props]]
      (let [editor-label (r subs/get-editor-label db extension-id item-namespace editor-props)]
           {:db (r load-editor! db extension-id item-namespace editor-props)
            :dispatch-n [[:ui/set-header-title! editor-label]
                         [:ui/set-window-title! editor-label]
                         [:item-editor/request-item!  extension-id item-namespace]
                         (engine/load-extension-event extension-id item-namespace)]})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/->item-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @usage
  ;  [:item-editor/->item-duplicated :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [copy-id (engine/server-response->copy-id extension-id item-namespace server-response)]
           [:item-editor/render-edit-copy-dialog! extension-id item-namespace copy-id])))

(a/reg-event-fx
  :item-editor/->item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db] :as cofx} [_ extension-id item-namespace]]
      {:db (r set-delete-mode! db extension-id)
       :dispatch-n [(r go-up! cofx extension-id item-namespace)
                    [:item-editor/render-undo-delete-dialog! extension-id item-namespace]]}))

(a/reg-event-fx
  :item-editor/->delete-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db] :as cofx} [_ extension-id item-namespace item-id]]
      {:db       (r set-recovery-mode! db   extension-id)
       :dispatch (r edit-item!         cofx extension-id item-namespace item-id)}))

(a/reg-event-fx
  :item-editor/->editor-leaved
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      ; Az elem sikeres törlése után az item-editor plugin elhagyásakor az elem utolsó
      ; állapotáról másolat készül, ami alapján lehetséges visszaállítani az elemet
      ; annak törlésének visszavonása esemény esetleges megtörténtekor.
      (if-let [delete-mode? (r subs/get-meta-item db extension-id item-namespace :delete-mode?)]
              {:db (r store-local-changes! db extension-id item-namespace)}
              ; Az item-editor plugin – az elem törlése nélküli – elhagyásakor, ha az elem
              ; el nem mentett változtatásokat tartalmaz, akkor annak az utolsó állapotáról
              ; másolat készül, ami alapján lehetséges azt visszaállítani a változtatások-elvetése
              ; esemény visszavonásának esetleges megtörténtekor.
              (if (r subs/item-changed? db extension-id item-namespace)
                  {:db       (r store-local-changes! db extension-id item-namespace)
                   :dispatch [:item-editor/render-changes-discarded-dialog! extension-id item-namespace]}))))
