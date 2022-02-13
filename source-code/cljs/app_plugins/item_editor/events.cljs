
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v1.3.4
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.events
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.map       :as map :refer [dissoc-in]]
              [mid-fruits.validator :as validator]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-ui.api         :as ui]
              [app-plugins.item-editor.engine :as engine]
              [app-plugins.item-editor.subs   :as subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
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
  (as-> db % (dissoc-in % [extension-id :item-editor/data-items])
             (dissoc-in % [extension-id :item-editor/meta-items :item-id])
             (dissoc-in % [extension-id :item-editor/meta-items :data-received?])
             (dissoc-in % [extension-id :item-editor/meta-items :error-mode?])
             (dissoc-in % [extension-id :item-editor/meta-items :item-deleted?])
             ; Ha az item-editor plugin {:recovery-mode? true} állapotban indul, de az elem
             ; visszaállítása már megtörtént, akkor kilép a {:recovery-mode? true} állapotból,
             ; mert az már nem érvényes!
             (if (and (r subs/get-meta-item db extension-id item-namespace :recovery-mode?)
                      (r subs/get-meta-item db extension-id item-namespace :item-recovered?))
                 (-> % (dissoc-in [extension-id :item-editor/meta-items :recovery-mode?])
                       (dissoc-in [extension-id :item-editor/meta-items :item-recovered?]))
                 (return %))))

(defn backup-current-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; - Az egyes elemek aktuális változatáról készített másolatok az elem azonosítójával vannak
  ;   tárolva. Így egy időben több elemről is lehetséges másolatot tárolni.
  ; - A gyors egymás utánban kitörölt elemek törlésének visszavonhatósága időbeni átfedésbe
  ;   kerülhet egymással, amiért szükséges az egyes elemekről készült másolatokat azonosítóval
  ;   megkülönböztetve kezelni és tárolni.
  ; - A gyors egymás utánban elvetett szerkesztett elemek elvetésének visszavonhatósága időbeni
  ;   átfedésbe kerülhet egymással, ...
  (let [current-item-id (r subs/get-current-item-id db extension-id item-namespace)
        current-item    (r subs/get-current-item    db extension-id item-namespace)]
       (assoc-in db [extension-id :item-editor/backup-items current-item-id] current-item)))

(defn store-local-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r subs/get-current-item-id db extension-id item-namespace)
        current-item    (r subs/get-current-item    db extension-id item-namespace)
        backup-item     (r subs/get-backup-item     db extension-id item-namespace current-item-id)
        local-changes   (map/difference current-item backup-item)]
       (assoc-in db [extension-id :item-editor/local-changes current-item-id] local-changes)))

(defn save-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (as-> db % (r backup-current-item! % extension-id item-namespace)
             (r ui/fake-process!     % 25)))

(defn delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ _ _]]
  (r ui/fake-process! db 25))

(defn undo-delete!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ _ _]]
  (r ui/fake-process! db 25))

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

(defn set-recovery-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  ; A {:recovery-mode? true} beállítással elindítitott item-editor plugin, visszaállítja az elem
  ; eltárolt változtatásait
  (assoc-in db [extension-id :item-editor/meta-items :recovery-mode?] true))

(defn item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  ; XXX#5610
  ; - El nem mentett változtatásokkal törölt elem törlése utáni kilépéskor NEM szükséges
  ;   kirenderelni changes-discarded-dialog párbeszédablakot.
  ; - A {:item-deleted? true} beállítás használatával az [:item-editor/editor-leaved ...]
  ;   esemény képes megállapítani, hogy szükséges-e kirenderelni a changes-discarded-dialog
  ;   párbeszédablakot.
  (assoc-in db [extension-id :item-editor/meta-items :item-deleted?] true))

(defn store-downloaded-suggestions!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (let [suggestions (get server-response :item-editor/get-item-suggestions)]
       (if (validator/data-valid? suggestions)
           ; If the received suggestions is valid ...
           (let [suggestions (validator/clean-validated-data suggestions)]
                (assoc-in db [extension-id :item-editor/meta-items :suggestions] suggestions))
           ; If the received suggestions is NOT valid ...
           (r set-error-mode! db extension-id item-namespace))))

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
                           (r backup-current-item! % extension-id item-namespace)))
           ; If the received document is NOT valid ...
           (assoc-in db [extension-id :item-editor/meta-items :error-mode?] true))))

(defn store-derived-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [derived-item-id (r subs/get-derived-item-id db extension-id item-namespace)]
       (assoc-in db [extension-id :item-editor/meta-items :item-id] derived-item-id)))

(defn set-current-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (assoc-in db [extension-id :item-editor/meta-items :item-id] item-id))

(defn store-current-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:item-id (string)(opt)}
  ;
  ; @return (map)
  [db [_ extension-id item-namespace {:keys [item-id]}]]
  (if (r subs/route-handled? db extension-id item-namespace)
      (r store-derived-item-id! db extension-id item-namespace)
      (r   set-current-item-id! db extension-id item-namespace item-id)))

(defn recover-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r subs/get-current-item-id db extension-id item-namespace)
        recovered-item  (r subs/get-recovered-item  db extension-id item-namespace)]
       (-> db (assoc-in  [extension-id :item-editor/data-items] recovered-item)
              (assoc-in  [extension-id :item-editor/meta-items :item-recovered?] true)
              (dissoc-in [extension-id :item-editor/local-changes current-item-id]))))

(defn data-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (assoc-in db [extension-id :item-editor/meta-items :data-received?] true))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [event-id extension-id item-namespace server-response]]
  (cond-> db (r subs/download-item?                 db extension-id item-namespace)
             (store-downloaded-item!         [event-id extension-id item-namespace server-response])
             (r subs/download-suggestions?          db extension-id item-namespace)
             (store-downloaded-suggestions!  [event-id extension-id item-namespace server-response])
             (r subs/get-meta-item                  db extension-id item-namespace :recovery-mode?)
             (recover-item!                  [event-id extension-id item-namespace])
             :data-received   (data-received [event-id extension-id item-namespace])))

(defn load-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:item-id (string)(opt)}
  ;
  ; @return (map)
  [db [_ extension-id item-namespace editor-props]]
  (as-> db % (r reset-editor!           % extension-id item-namespace)
             (r store-current-item-id!  % extension-id item-namespace editor-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-editor/clean-recovery-data! clean-recovery-data!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-editor/receive-item! receive-item!)
