
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.8.2
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.events
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.map       :as map :refer [dissoc-in]]
              [mid-fruits.validator :as validator]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [app-plugins.item-editor.engine  :as engine]
              [app-plugins.item-editor.queries :as queries]
              [app-plugins.item-editor.subs    :as subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (if-let [recovery-mode? (r subs/get-meta-value db extension-id item-namespace :recovery-mode?)]
          ; If recovery-mode is enabled ...
          (-> db (dissoc-in [extension-id :item-editor/data-item])
                 (update-in [extension-id :item-editor/meta-items] select-keys [:recovery-mode? :local-changes]))
          ; If recovery-mode is NOT enabled ...
          (-> db (dissoc-in [extension-id :item-editor/data-item])
                 (dissoc-in [extension-id :item-editor/meta-items]))))

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

(defn mark-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;  {:marker-key (keyword)
  ;   :toggle-f (function)}
  ;
  ; @return (map)
  [db [_ extension-id _ {:keys [marker-key toggle-f]}]]
  (update-in db [extension-id :item-editor/data-item marker-key] toggle-f))

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
       (assoc-in db [extension-id :item-editor/meta-items :local-changes current-item-id] local-changes)))

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
          (-> db (dissoc-in [extension-id :item-editor/backup-items item-id])
                 (dissoc-in [extension-id :item-editor/meta-items :local-changes item-id]))
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
  (let [suggestions (get server-response :item-editor/get-item-suggestions)
        suggestions (validator/clean-validated-data suggestions)]
       (assoc-in db [extension-id :item-editor/meta-items :suggestions] suggestions)))

(defn store-downloaded-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (let [current-item-entity (r subs/get-current-item-entity db extension-id item-namespace)
        document            (get server-response current-item-entity)
        ; XXX#3907
        ; Az item-lister pluginnal megegyezően az item-editor is névtér nélkül tárolja
        ; a letöltött dokumentumot
        document (-> document validator/clean-validated-data db/document->non-namespaced-document)]
       (as-> db % (assoc-in % [extension-id :item-editor/data-item] document)
                  (r backup-current-item! % extension-id))))

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
  (let [recovered-item (r subs/get-recovered-item db extension-id item-namespace)]
       (-> db (assoc-in  [extension-id :item-editor/data-item] recovered-item)
              (dissoc-in [extension-id :item-editor/meta-items :recovery-mode?])
              (dissoc-in [extension-id :item-editor/meta-items :local-changes]))))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [event-id extension-id item-namespace server-response]]
  (if-let [error-occured? (validator/data-structure-invalid? server-response)]
          ; If document or suggestions are NOT valid ...
          (assoc-in db [extension-id :item-editor/meta-items :error-mode?] true)
          ; If document and suggestions are valid ...
          (cond-> db (r subs/download-item?                db extension-id item-namespace)
                     (store-downloaded-item!        [event-id extension-id item-namespace server-response])
                     (r subs/download-suggestions?         db extension-id item-namespace)
                     (store-downloaded-suggestions! [event-id extension-id item-namespace server-response])
                     (r subs/get-meta-value                db extension-id item-namespace :recovery-mode?)
                     (recover-item!                 [event-id extension-id item-namespace]))))

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
  (as-> db % (r reset-editor!          % extension-id item-namespace)
             (r store-current-item-id! % extension-id)
             (r store-editor-props!    % extension-id item-namespace editor-props)))



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
  :item-editor/mark-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;  {:marked-message (metamorphic-content)
  ;   :marker-key (keyword)
  ;   :toggle-f (function)}
  ;
  ; @usage
  ;  [:item-editor/mark-item! :my-extension :my-type {:marked-message :added-to-favorites
  ;                                                   :marker-key     :favorite?
  ;                                                   :toggle-f       not}]
  (fn [{:keys [db]} [_ extension-id item-namespace mark-props]]
       ; Megjelöli az elem kliens-oldalon tárolt változatát a marker-key kulccsal
      {:db (r mark-item! db extension-id item-namespace mark-props)
       ; Elküldi az elem jelölést tartalmazó kivonatát a szervernek
       :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                    {:on-success [:item-editor/->item-marked extension-id item-namespace mark-props]
                                     :on-failure [:ui/blow-bubble! {:body {:content :network-error}}]
                                     :query      (r queries/get-mark-item-query db extension-id item-namespace mark-props)}]}))

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
                             {:on-success [:item-editor/receive-item!   extension-id item-namespace]
                              :query      (r queries/get-download-query db extension-id item-namespace)}])))
(a/reg-event-fx
  :item-editor/load-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:handle-archived-items? (boolean)
  ;   :handle-archived-items? (boolean)
  ;   :label (metamorphic-content)(opt)
  ;   :suggestion-keys (keywords in vector)(opt)}
  (fn [{:keys [db]} [_ extension-id item-namespace editor-props]]
      (let [editor-label (r subs/get-editor-label db extension-id item-namespace editor-props)]
           {:db (r load-editor! db extension-id item-namespace editor-props)
            :dispatch-n [[:ui/listen-to-process! (engine/request-id extension-id item-namespace)]
                         [:ui/set-header-title!  (param editor-label)]
                         [:ui/set-window-title!  (param editor-label)]
                         [:item-editor/request-item!  extension-id item-namespace]
                         (engine/load-extension-event extension-id item-namespace)]})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/->item-marked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;  {:marked-message (metamorphic-content)}
  (fn [_ [_ extension-id item-namespace {:keys [marked-message]}]]
      (let [dialog-id (engine/dialog-id extension-id item-namespace :marked)]
           [:ui/blow-bubble! dialog-id {:body {:content marked-message}}])))

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
      (if-let [delete-mode? (r subs/get-meta-value db extension-id item-namespace :delete-mode?)]
              {:db (r store-local-changes! db extension-id item-namespace)}
              ; Az item-editor plugin – az elem törlése nélküli – elhagyásakor, ha az elem
              ; el nem mentett változtatásokat tartalmaz, akkor annak az utolsó állapotáról
              ; másolat készül, ami alapján lehetséges azt visszaállítani a változtatások-elvetése
              ; esemény visszavonásának esetleges megtörténtekor.
              (if (r subs/item-changed? db extension-id item-namespace)
                  {:db       (r store-local-changes! db extension-id item-namespace)
                   :dispatch [:item-editor/render-changes-discarded-dialog! extension-id item-namespace]}))))
