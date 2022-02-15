
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v1.3.0
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.effects
    (:require [x.app-core.api :as a :refer [r]]
              [app-plugins.item-editor.engine  :as engine]
              [app-plugins.item-editor.events  :as events]
              [app-plugins.item-editor.queries :as queries]
              [app-plugins.item-editor.subs    :as subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/load-editor!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) editor-props
  ;  {:item-id (string)(opt)}
  ;
  ; @usage
  ;  [:item-editor/load-editor! :my-extension :my-type]
  ;
  ; @usage
  ;  [:item-editor/load-editor! :my-extension :my-type {...}]
  ;
  ; @usage
  ;  [:item-editor/load-editor! :my-extension :my-type {:item-id "my-item"}]
  (fn [{:keys [db]} [_ extension-id item-namespace editor-props]]
      (let [; Az events/load-editor! függvény által meghívott events/reset-editor! függvény
            ; lépteti ki az item-editor plugint a {:recovery-mode? true} állapotból,
            ; ami miatt szükséges az events/load-editor! függvényt a subs/download-data? függvény
            ; lefutása előtt meghívni!
            db           (r events/load-editor!   db extension-id item-namespace editor-props)
            editor-label (r subs/get-editor-label db extension-id item-namespace)]
           {:db db :dispatch-n [; XXX#3237
                                (if (r subs/set-title? db extension-id item-namespace)
                                    [:ui/set-title! editor-label])
                                (if (r subs/download-data? db extension-id item-namespace)
                                    [:item-editor/request-item! extension-id item-namespace]
                                    [:item-editor/load-item!    extension-id item-namespace])
                                (engine/load-extension-event extension-id item-namespace)]})))

(a/reg-event-fx
  :item-editor/edit-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-editor/edit-item! :my-extension :my-type "my-item"]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      ; Ha az item-editor plugin útvonala létezik, akkor az [:item-editor/edit-item! ...] esemény
      ; az útvonalra irányít, abban az esetben is, ha az NEM az aktuális útvonal, mert
      ; az [:item-editor/edit-item! ...] esemény meghívása a legtöbb esetben NEM az item-editor
      ; plugin használata közben történik!
      (if (r subs/get-meta-item db extension-id item-namespace :routed?)
          (let [editor-uri (engine/editor-uri extension-id item-namespace item-id)]
               [:router/go-to! editor-uri])
          [:item-editor/load-editor! extension-id item-namespace {:item-id item-id}])))

(a/reg-event-fx
  :item-editor/go-up!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/go-up! :my-extension :my-type]
  (fn [_ [_ extension-id item-namespace]]
      (let [parent-uri (engine/parent-uri extension-id item-namespace)]
           [:router/go-to! parent-uri])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/unload-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      ; Az elem sikeres törlése után az item-editor plugin elhagyásakor az elem utolsó
      ; állapotáról másolat készül, ami alapján lehetséges visszaállítani az elemet
      ; annak törlésének visszavonása esemény esetleges megtörténtekor.
      (if-let [item-deleted? (r subs/get-meta-item db extension-id item-namespace :item-deleted?)]
              {:db (r events/store-local-changes! db extension-id item-namespace)}
              ; Az item-editor plugin – az elem törlése nélküli – elhagyásakor, ha az elem
              ; el nem mentett változtatásokat tartalmaz, akkor annak az utolsó állapotáról
              ; másolat készül, ami alapján lehetséges azt visszaállítani a változtatások-elvetése
              ; esemény visszavonásának esetleges megtörténtekor.
              (if (r subs/item-changed? db extension-id item-namespace)
                  {:db (r events/store-local-changes! db extension-id item-namespace)
                   :dispatch [:item-editor/render-changes-discarded-dialog! extension-id item-namespace]}))))

(a/reg-event-fx
  :item-editor/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      [:sync/send-query! (engine/request-id extension-id item-namespace)
                         {:display-progress? true
                          ; XXX#4057
                          ; Az on-stalled időzítéssel a UI változásai egyszerre történnek
                          ; meg a lekérés okozta {:editor-disabled? true} állapot megszűnésével
                          :on-stalled [:item-editor/receive-item! extension-id item-namespace]
                          :query      (r queries/get-request-item-query db extension-id item-namespace)}]))
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
      {:db (r events/save-item! db extension-id item-namespace)
       :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                    {:on-success [:item-editor/go-up! extension-id item-namespace]
                                     :on-failure [:ui/blow-bubble! {:body :failed-to-save}]
                                     :query      (r queries/get-save-item-query db extension-id item-namespace)}]}))



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
      {:db (r events/delete-item! db extension-id item-namespace)
       :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                    {:on-success [:item-editor/item-deleted extension-id item-namespace]
                                     :on-failure [:ui/blow-bubble! {:body :failed-to-delete}]
                                     :query      (r queries/get-delete-item-query db extension-id item-namespace)}]}))

(a/reg-event-fx
  :item-editor/undo-delete!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      {:db (r events/undo-delete! db extension-id item-namespace)
       :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                    {:on-success [:item-editor/delete-undid extension-id item-namespace item-id]
                                     :on-failure [:ui/blow-bubble! {:body {:content :failed-to-undo-delete}}]
                                     :query      (r queries/get-undo-delete-query db extension-id item-namespace item-id)}]}))

(a/reg-event-fx
  :item-editor/delete-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      {:db (r events/set-recovery-mode! db extension-id item-namespace)
       :dispatch [:item-editor/edit-item! extension-id item-namespace item-id]}))

(a/reg-event-fx
  :item-editor/item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r events/item-deleted db extension-id item-namespace)
       :dispatch-n [[:item-editor/go-up!                     extension-id item-namespace]
                    [:item-editor/render-undo-delete-dialog! extension-id item-namespace]]}))



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
      [:sync/send-query! (engine/request-id extension-id item-namespace)
                         {:display-progress? true
                          :on-success [:item-editor/item-duplicated extension-id item-namespace]
                          :on-failure [:ui/blow-bubble! {:body :failed-to-copy}]
                          :query      (r queries/get-duplicate-item-query db extension-id item-namespace)}]))

(a/reg-event-fx
  :item-editor/item-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [copy-id (engine/server-response->copy-id extension-id item-namespace server-response)]
           [:item-editor/render-edit-copy-dialog! extension-id item-namespace copy-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/undo-discard!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      {:db (r events/set-recovery-mode! db extension-id item-namespace)
       :dispatch [:item-editor/edit-item! extension-id item-namespace item-id]}))