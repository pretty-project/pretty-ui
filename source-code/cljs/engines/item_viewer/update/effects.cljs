
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.update.effects
    (:require [engines.item-viewer.body.subs         :as body.subs]
              [engines.item-viewer.core.subs         :as core.subs]
              [engines.item-viewer.routes.subs       :as routes.subs]
              [engines.item-viewer.transfer.subs     :as transfer.subs]
              [engines.item-viewer.update.events     :as update.events]
              [engines.item-viewer.update.queries    :as update.queries]
              [engines.item-viewer.update.subs       :as update.subs]
              [engines.item-viewer.update.validators :as update.validators]
              [engines.item-viewer.update.views      :as update.views]
              [re-frame.api                          :as r :refer [r]]
              [x.ui.api                              :as x.ui]))



;; -- Delete item effects -----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-viewer/delete-item!
  ; @param (keyword) viewer-id
  ;
  ; @usage
  ;  [:item-viewer/delete-item! :my-viewer]
  (fn [{:keys [db]} [_ viewer-id]]
      (let [query        (r update.queries/get-delete-item-query          db viewer-id)
            validator-f #(r update.validators/delete-item-response-valid? db viewer-id %)]
           {:db       (r update.events/delete-item! db viewer-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db viewer-id)
                                           {:on-success [:item-viewer/item-deleted       viewer-id]
                                            :on-failure [:item-viewer/delete-item-failed viewer-id]
                                            :query query :validator-f validator-f}]})))

(r/reg-event-fx :item-viewer/item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ viewer-id server-response]]
      ; (A) Ha az "Elem törlése" művelet sikeres befejeződésekor ...
      ;     ... a body komponens a React-fába van csatolva,
      ;     ... a törölt elem van megnyitva megtekintésre,
      ;     ... az engine rendelkezik a {:base-route "..."} tulajdonsággal, ...
      ;         ... átirányít a {:base-route "..."} tulajdonságként a kliens-oldali kezelő számára
      ;             elküldött útvonalra.
      ;         ... feltételezi, hogy az útvonal használatakor befejeződik a progress-bar elemen
      ;             15%-ig szimulált folyamat.
      ;
      ; (B) Ha az (A) kimenetel feltételei nem teljesülnek ...
      ;     ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      (let [item-id    (r update.subs/get-deleted-item-id db viewer-id server-response)
            base-route (r transfer.subs/get-transfer-item db viewer-id :base-route)]
           (if (and base-route (r core.subs/viewing-item? db viewer-id item-id))
               ; (A)
               {:dispatch-n [[:item-viewer/render-item-deleted-dialog! viewer-id item-id]
                             [:x.router/go-to! base-route]]}
               ; (B)
               {:dispatch    [:item-viewer/render-item-deleted-dialog! viewer-id item-id]
                :dispatch-if [(r x.ui/process-faked? db)
                              [:x.ui/end-fake-process!]]}))))

(r/reg-event-fx :item-viewer/delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ viewer-id server-response]]
      ; Az "Elem törlése" művelet sikertelen befejeződésekor, ...
      ; ... megjelenít egy értesítést.
      ; ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      (let [item-id (r update.subs/get-deleted-item-id db viewer-id server-response)]
           {:dispatch    [:item-viewer/render-delete-item-failed-dialog! viewer-id item-id]
            :dispatch-if [(r x.ui/process-faked? db)
                          [:x.ui/end-fake-process!]]})))

(r/reg-event-fx :item-viewer/render-item-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ viewer-id item-id]]
      [:x.ui/render-bubble! ::item-deleted-dialog
                            {:body [update.views/item-deleted-dialog-body viewer-id item-id]}]))

(r/reg-event-fx :item-viewer/render-delete-item-failed-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ viewer-id item-id]]
      [:x.ui/render-bubble! ::delete-item-failed-dialog
                            {:body :failed-to-delete}]))



;; -- Undo delete item effects ------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-viewer/undo-delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ viewer-id item-id]]
      ; Az [:item-viewer/undo-delete-item! ...] esemény ...
      ; ...
      (let [query        (r update.queries/get-undo-delete-item-query          db viewer-id item-id)
            validator-f #(r update.validators/undo-delete-item-response-valid? db viewer-id %)]
           {:db       (r x.ui/fake-process! db 15)
            :dispatch-n [[:x.ui/remove-bubble! ::item-deleted-dialog]
                         [:pathom/send-query! (r core.subs/get-request-id db viewer-id)
                                              {:on-success [:item-viewer/delete-item-undid       viewer-id item-id]
                                               :on-failure [:item-viewer/undo-delete-item-failed viewer-id]
                                               :query query :validator-f validator-f}]]})))

(r/reg-event-fx :item-viewer/delete-item-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ viewer-id item-id _]]
      ; (A) Ha a "Törölt elem visszaállítása" művelet sikeres befejeződésekor az engine rendelkezik
      ;     az elem útvonalának elkészítéséhez szükséges tulajdonságokkal ...
      ;     ... elkészíti az elemhez tartozó útvonalat és átírányít arra.
      ;     ... az útvonal használatakor befejeződik a progress-bar elemen 15%-ig szimulált folyamat.

      ; (B) Ha a "Törölt elem visszaállítása" művelet sikeres befejeződésekor az engine NEM rendelkezik
      ;     az útvonal elkészítéséhez szükséges tulajdonságokkal ...
      ;     ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      (if-let [item-route (r routes.subs/get-item-route db viewer-id item-id)]
              ; (A)
              [:x.router/go-to! item-route]
              ; (B)
              {:dispatch-if [(r x.ui/process-faked? db)
                             [:x.ui/end-fake-process!]]})))

(r/reg-event-fx :item-viewer/undo-delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ viewer-id server-response]]
      ; A "Törölt elem visszaállítása" művelet sikertelen befejeződésekor, ...
      ; ... megjelenít egy értesítést.
      ; ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      (let [item-id (r update.subs/get-deleted-item-id db viewer-id server-response)]
           {:dispatch    [:item-viewer/render-undo-delete-item-failed-dialog! viewer-id item-id]
            :dispatch-if [(r x.ui/process-faked? db)
                          [:x.ui/end-fake-process!]]})))

(r/reg-event-fx :item-viewer/render-undo-delete-item-failed-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  (fn [_ [_ viewer-id item-id]]
      [:x.ui/render-bubble! ::undo-delete-item-failed-dialog
                            {:body [update.views/undo-delete-item-failed-dialog-body viewer-id item-id]}]))



;; -- Duplicate item effects --------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-viewer/duplicate-item!
  ; @param (keyword) viewer-id
  ;
  ; @usage
  ;  [:item-viewer/duplicate-item! :my-viewer]
  (fn [{:keys [db]} [_ viewer-id]]
      (let [query        (r update.queries/get-duplicate-item-query          db viewer-id)
            validator-f #(r update.validators/duplicate-item-response-valid? db viewer-id %)]
           [:pathom/send-query! (r core.subs/get-request-id db viewer-id)
                                {:display-progress? true
                                 :on-success [:item-viewer/item-duplicated       viewer-id]
                                 :on-failure [:item-viewer/duplicate-item-failed viewer-id]
                                 :query query :validator-f validator-f}])))

(r/reg-event-fx :item-viewer/item-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ viewer-id server-response]]
      ; Ha az "Elem duplikálása" művelet sikeres volt, ...
      ; ... megjelenít egy értesítést.
      (let [copy-id (r update.subs/get-duplicated-item-id db viewer-id server-response)]
           [:item-viewer/render-item-duplicated-dialog! viewer-id copy-id])))

(r/reg-event-fx :item-viewer/duplicate-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  (fn [_ [_ _ _]]
      ; Ha az "Elem duplikálása" művelet sikertelen volt, ...
      ; ... megjelenít egy értesítést.
      [:x.ui/render-bubble! ::duplicate-item-failed-dialog
                            {:body :failed-to-duplicate}]))

(r/reg-event-fx :item-viewer/render-item-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) copy-id
  (fn [_ [_ viewer-id copy-id]]
      [:x.ui/render-bubble! ::item-duplicated-dialog
                            {:body [update.views/item-duplicated-dialog-body viewer-id copy-id]}]))

(r/reg-event-fx :item-viewer/view-duplicated-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) copy-id
  (fn [_ [_ viewer-id copy-id]]
      {:dispatch-n [[:x.ui/remove-bubble! ::item-duplicated-dialog]
                    [:item-viewer/view-item! viewer-id copy-id]]}))
