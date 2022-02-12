
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.5.4
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.interacts
    (:require [x.app-core.api       :as a :refer [r]]
              [x.app-ui.api         :as ui]
              [app-plugins.item-editor.dialogs :as dialogs]
              [app-plugins.item-editor.engine  :as engine]
              [app-plugins.item-editor.events  :as events]
              [app-plugins.item-editor.queries :as queries]
              [app-plugins.item-editor.subs    :as subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (as-> db % (r events/backup-current-item! % extension-id item-namespace)
             (r ui/fake-process!            % 25)))

(defn delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ _ _]]
  (r ui/fake-process! db 25))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

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
      ;   miközben az item-editor plugin továbbra is "új elem hozzáadása" módban fut!
      ; - Az elem esetleges törlése utáni – kliens-oldali adatból történő – visszaállításhoz
      ;   szükséges az elem feltételezett szerver-oldali állapotáról másolatot tárolni!
      ; - Az elem szerverre küldésének idejében az elemről másolat készítése feltételezi,
      ;   a mentés sikerességét. Sikertelen mentés esetén a kliens-oldali másolat eltérhet
      ;   a szerver-oldalon tárolt változattól, ami az elem törlése utáni visszaállítás esetén
      ;   pontatlan visszaálltást okozhat!
      {:db (r save-item! db extension-id item-namespace)
       :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                    {:on-success [:item-editor/go-up! extension-id item-namespace]
                                     :on-failure [:ui/blow-bubble! {:body :failed-to-save}]
                                     :query      (r queries/get-save-item-query db extension-id item-namespace)}]}))

(a/reg-event-fx
  :item-editor/delete-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/delete-item! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r delete-item! db extension-id item-namespace)
       :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                    {:on-success [:item-editor/item-deleted extension-id item-namespace]
                                     :on-failure [:ui/blow-bubble! {:body :failed-to-delete}]
                                     :query      (r queries/get-delete-item-query db extension-id item-namespace)}]}))

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
  :item-editor/undo-delete!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      [:sync/send-query! (engine/request-id extension-id item-namespace)
                         {:display-progress? true
                          :on-success [:item-editor/delete-undid extension-id item-namespace item-id]
                          :on-failure [:ui/blow-bubble! {:body {:content :failed-to-undo-delete}}]
                          :query      (r queries/get-undo-delete-query db extension-id item-namespace item-id)}]))

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

(a/reg-event-fx
  :item-editor/item-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db] :as cofx} [_ extension-id item-namespace server-response]]
      (let [copy-id (engine/server-response->copy-id extension-id item-namespace server-response)]
           (r dialogs/render-edit-copy-dialog!  cofx extension-id item-namespace copy-id))))

(a/reg-event-fx
  :item-editor/item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db] :as cofx} [_ extension-id item-namespace]]
      {:db (r events/set-delete-mode! db extension-id item-namespace)
       :dispatch-n [[:item-editor/go-up!                       extension-id item-namespace]
                    (r dialogs/render-undo-delete-dialog! cofx extension-id item-namespace)]}))

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
