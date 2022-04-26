
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.update.effects
    (:require [plugins.item-viewer.body.subs         :as body.subs]
              [plugins.item-viewer.core.subs         :as core.subs]
              [plugins.item-viewer.routes.subs       :as routes.subs]
              [plugins.item-viewer.transfer.subs     :as transfer.subs]
              [plugins.item-viewer.update.events     :as update.events]
              [plugins.item-viewer.update.queries    :as update.queries]
              [plugins.item-viewer.update.subs       :as update.subs]
              [plugins.item-viewer.update.validators :as update.validators]
              [plugins.item-viewer.update.views      :as update.views]
              [x.app-core.api                        :as a :refer [r]]
              [x.app-ui.api                          :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-viewer/render-item-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ viewer-id item-id]]
      [:ui/blow-bubble! :plugins.item-viewer/item-deleted-dialog
                        {:body       [update.views/item-deleted-dialog-body viewer-id item-id]
                         :destructor [:item-viewer/clean-recovery-data!     viewer-id item-id]}]))

(a/reg-event-fx
  :item-viewer/render-item-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) copy-id
  (fn [_ [_ viewer-id copy-id]]
      [:ui/blow-bubble! :plugins.item-viewer/item-duplicated-dialog
                        {:body [update.views/item-duplicated-dialog-body viewer-id copy-id]}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-viewer/delete-item!
  ; @param (keyword) viewer-id
  ;
  ; @usage
  ;  [:item-viewer/delete-item! :my-viewer]
  (fn [{:keys [db]} [_ viewer-id]]
      (let [query        (r update.queries/get-delete-item-query          db viewer-id)
            validator-f #(r update.validators/delete-item-response-valid? db viewer-id %)]
           {:db       (r update.events/delete-item! db viewer-id)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db viewer-id)
                                         {:on-success [:item-viewer/item-deleted       viewer-id]
                                          :on-failure [:item-viewer/delete-item-failed viewer-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-viewer/item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ viewer-id server-response]]
      ; A) Ha az "Elem törlése" művelet sikeres befejeződésekor ...
      ;    ... a body komponens a React-fába van csatolva,
      ;    ... a törölt elem van megnyitva megtekintésre,
      ;    ... a plugin rendelkezik a {:base-route "..."} tulajdonsággal, ...
      ;        ... átirányít a {:base-route "..."} tulajdonságként a kliens-oldali kezelő számára
      ;            elküldött útvonalra.
      ;        ... feltételezi, hogy az útvonal használatakor befejeződik a progress-bar elemen
      ;            15%-ig szimulált folyamat.
      ;
      ; B) Ha az A) kimenetel feltételei nem teljesülnek ...
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      (let [item-id    (r update.subs/get-deleted-item-id db viewer-id server-response)
            base-route (r transfer.subs/get-transfer-item db viewer-id :base-route)]
           {:dispatch-n [[:item-viewer/render-item-deleted-dialog! viewer-id item-id]
                         (if (and base-route (r core.subs/viewing-item? db viewer-id item-id))
                             ; A)
                             [:router/go-to! base-route]
                             ; B)
                             [:ui/end-fake-process!])]})))

(a/reg-event-fx
  :item-viewer/delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ viewer-id _]]
      ; A) Ha az "Elem törlése" művelet sikertelen befejeződésekor a body komponens
      ;    a React-fába van csatolva, ...
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;    ... megjelenít egy értesítést.
      ;
      ; B) Ha az "Elem törlése" művelet sikertelen befejeződésekor a body komponens
      ;    NINCS a React-fába csatolva, ...
      ;    ... megjelenít egy értesítést.
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (if (r body.subs/body-did-mount? db viewer-id)
          ; A)
          {:dispatch-n [[:ui/end-fake-process!]
                        [:ui/blow-bubble! {:body :failed-to-delete}]]}
          ; B)
          [:ui/blow-bubble! {:body :failed-to-delete}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-viewer/undo-delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ viewer-id item-id]]
      ; Az [:item-viewer/undo-delete-item! ...] esemény ...
      ; ...
      (let [dialog-id    (update.helpers/dialog-id viewer-id :delete item-id)
            query        (r update.queries/get-undo-delete-item-query          db viewer-id item-id)
            validator-f #(r update.validators/undo-delete-item-response-valid? db viewer-id %)]
           {:db         (r ui/fake-process! db 15)
            :dispatch-n [[:sync/send-query! (r core.subs/get-request-id db viewer-id)
                                            {:on-success [:item-viewer/delete-item-undid       viewer-id item-id]
                                             :on-failure [:item-viewer/undo-delete-item-failed viewer-id]
                                             :query query :validator-f validator-f}]
                         [:ui/blow-bubble! :plugins.item-viewer/item-deleted-dialog
                                           {:body [update.views/undo-delete-progress-dialog-body viewer-id item-id]}]]})))

(a/reg-event-fx
  :item-viewer/delete-item-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ viewer-id item-id _]]
      ; A) Ha a "Törölt elem visszaállítása" művelet sikeres befejeződésekor a plugin rendelkezik
      ;    az elem útvonalának elkészítéséhez szükséges tulajdonságokkal ...
      ;    ... elkészíti az elemhez tartozó útvonalat és átírányít arra.
      ;    ... az útvonal használatakor befejeződik a progress-bar elemen 15%-ig szimulált folyamat.

      ; B) Ha a "Törölt elem visszaállítása" művelet sikeres befejeződésekor a plugin NEM rendelkezik
      ;    az útvonal elkészítéséhez szükséges tulajdonságokkal ...
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      {:dispatch-n [(if-let [item-route (r routes.subs/get-item-route db viewer-id item-id)]
                            ; A)
                            [:router/go-to! item-route]
                            ; B)
                            [:ui/end-fake-process!])]}))

(a/reg-event-fx
  :item-viewer/undo-delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ viewer-id _]]
      ; A) Ha a "Törölt elem visszaállítása" művelet sikertelen befejeződésekor a body komponens
      ;    a React-fába van csatolva, ...
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;    ... megjelenít egy értesítést.
      ;
      ; B) Ha a "Törölt elem visszaállítása" művelet sikertelen befejeződésekor a body komponens
      ;    NINCS a React-fába csatolva, ...
      ;    ... megjelenít egy értesítést.
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (if (r body.subs/body-did-mount? db viewer-id)
          ; A)
          {:dispatch-n [[:ui/end-fake-process!]
                        [:ui/blow-bubble! {:body :failed-to-undo-delete}]]}
          ; B)
          [:ui/blow-bubble! {:body :failed-to-undo-delete}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-viewer/duplicate-item!
  ; @param (keyword) viewer-id
  ;
  ; @usage
  ;  [:item-viewer/duplicate-item! :my-viewer]
  (fn [{:keys [db]} [_ viewer-id]]
      (let [query        (r update.queries/get-duplicate-item-query          db viewer-id)
            validator-f #(r update.validators/duplicate-item-response-valid? db viewer-id %)]
           [:sync/send-query! (r core.subs/get-request-id db viewer-id)
                              {:display-progress? true
                               :on-success [:item-viewer/item-duplicated       viewer-id]
                               :on-failure [:item-viewer/duplicate-item-failed viewer-id]
                               :query query :validator-f validator-f}])))

(a/reg-event-fx
  :item-viewer/item-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ viewer-id server-response]]
      ; Ha az "Elem duplikálása" művelet sikeres volt, ...
      ; ... megjelenít egy értesítést.
      (let [copy-id (r update.subs/get-duplicated-item-id db viewer-id server-response)]
           [:item-viewer/render-item-duplicated-dialog! viewer-id copy-id])))

(a/reg-event-fx
  :item-viewer/duplicate-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  (fn [_ [_ _ _]]
      ; Ha az "Elem duplikálása" művelet sikertelen volt, ...
      ; ... megjelenít egy értesítést.
      [:ui/blow-bubble! {:body :failed-to-duplicate}]))
