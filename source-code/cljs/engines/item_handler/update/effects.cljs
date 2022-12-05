
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.update.effects
    (:require [engines.item-handler.body.subs         :as body.subs]
              [engines.item-handler.core.events       :as core.events]
              [engines.item-handler.core.subs         :as core.subs]
              [engines.item-handler.routes.subs       :as routes.subs]
              [engines.item-handler.transfer.subs     :as transfer.subs]
              [engines.item-handler.update.events     :as update.events]
              [engines.item-handler.update.queries    :as update.queries]
              [engines.item-handler.update.subs       :as update.subs]
              [engines.item-handler.update.validators :as update.validators]
              [engines.item-handler.update.views      :as update.views]
              [re-frame.api                           :as r :refer [r]]
              [x.ui.api                               :as x.ui]))



;; -- Save item effects -------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/save-item!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ; [:item-handler/save-item! :my-handler]
  (fn [{:keys [db]} [_ handler-id]]
      (let [query        (r update.queries/get-save-item-query          db handler-id)
            validator-f #(r update.validators/save-item-response-valid? db handler-id %)]
           {:db       (r x.ui/fake-process! db 15)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db handler-id)
                                           {:display-progress? false
                                            :on-success [:item-handler/item-saved       handler-id]
                                            :on-failure [:item-handler/save-item-failed handler-id]
                                            :query query :validator-f validator-f}]})))

(r/reg-event-fx :item-handler/item-saved
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id server-response]]
      ; Ha az "Elem mentése" művelet sikeres befejeződésekor ...
      ; ... megtörténik a body komponens számára esetlegesen átadott on-saved esemény.
      ; (A) ... a body komponens a React-fába van csatolva, a mentett elem van megnyitva
      ;         kezelésre VAGY új elem mentése történt és az engine útvonal-vezérelt, ...
      ;         ... az [:item-handler/go-up! ...] esemény átirányít a base-route vagy item-route
      ;             útvonalra.
      ;         ... feltételezi, hogy az útvonal használatakor befejeződik a progress-bar elemen
      ;             15%-ig szimulált folyamat.
      ;
      ; (B) ... a body komponens már nincs a React-fába csatolva és az engine útvonal-vezérelt, ...
      ;         ... megjelenít egy értesítést.
      ;
      ; (C) ... az (A) kimenetel feltételei nem teljesülnek ...
      ;         ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;         ... megjelenít egy értesítést.
      (if-let [route-handled? (r routes.subs/route-handled? db handler-id)]
              (let [item-id (r update.subs/get-saved-item-id db handler-id server-response)]
                   ; Új elem mentésekor szükséges eltárolni a szerver által visszaküldött elem-azonosítót,
                   ; az [:item-handler/go-to! ...] esemény számára!
                   (cond ; (A)
                         (r core.subs/handling-item? db handler-id item-id) {:dispatch-n [(r update.subs/get-on-saved-event db handler-id server-response)
                                                                                          [:item-handler/go-up! handler-id]]}
                         (r core.subs/new-item?     db handler-id)          {:db          (r core.events/set-item-id! db handler-id item-id)
                                                                             :dispatch-n [(r update.subs/get-on-saved-event db handler-id server-response)
                                                                                          [:item-handler/go-up! handler-id]]}
                         ; (B)
                         :handler-leaved                                    {:dispatch-n [(r update.subs/get-on-saved-event db handler-id server-response)
                                                                                          [:x.ui/render-bubble! ::item-saved-dialog {:body :saved}]]}))
              ; (C)
              {:dispatch-if [(r x.ui/process-faked? db)
                             [:x.ui/end-fake-process!]]
               :dispatch-n [(r update.subs/get-on-saved-event db handler-id server-response)
                            [:x.ui/render-bubble! ::item-saved-dialog {:body :saved}]]})))

(r/reg-event-fx :item-handler/save-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id _]]
      ; Az "Elem mentése" művelet sikertelen befejeződésekor, ...
      ; ... megjelenít egy értesítést.
      ; ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      {:dispatch    [:x.ui/render-bubble! ::save-item-failed-dialog {:body :failed-to-save}]
       :dispatch-if [(r x.ui/process-faked? db)
                     [:x.ui/end-fake-process!]]}))



;; -- Delete item effects -----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/delete-item!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ; [:item-handler/delete-item! :my-handler]
  (fn [{:keys [db]} [_ handler-id]]
      (let [query        (r update.queries/get-delete-item-query          db handler-id)
            validator-f #(r update.validators/delete-item-response-valid? db handler-id %)]
           {:db       (r update.events/delete-item! db handler-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db handler-id)
                                           {:display-progress? false
                                            :on-success [:item-handler/item-deleted       handler-id]
                                            :on-failure [:item-handler/delete-item-failed handler-id]
                                            :query query :validator-f validator-f}]})))

(r/reg-event-fx :item-handler/item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id server-response]]
      ; (A) Ha az "Elem törlése" művelet sikeres befejeződésekor ...
      ;     ... a body komponens a React-fába van csatolva,
      ;     ... a törölt elem van megnyitva kezelésre,
      ;     ... az engine rendelkezik a {:base-route "..."} tulajdonsággal, ...
      ;         ... átirányít a {:base-route "..."} tulajdonságként a kliens-oldali kezelő számára
      ;             elküldött útvonalra.
      ;         ... feltételezi, hogy az útvonal használatakor befejeződik a progress-bar elemen
      ;             15%-ig szimulált folyamat.
      ;
      ; (B) Ha az (A) kimenetel feltételei nem teljesülnek ...
      ;     ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      (let [item-id    (r update.subs/get-deleted-item-id db handler-id server-response)
            base-route (r transfer.subs/get-transfer-item db handler-id :base-route)]
           (if (and base-route (r core.subs/handling-item? db handler-id item-id))
               ; (A)
               {:dispatch-n [[:item-handler/render-item-deleted-dialog! handler-id item-id]
                             [:x.router/go-to! base-route]]}
               ; (B)
               {:dispatch    [:item-handler/render-item-deleted-dialog! handler-id item-id]
                :dispatch-if [(r x.ui/process-faked? db)
                              [:x.ui/end-fake-process!]]}))))

(r/reg-event-fx :item-handler/delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id server-response]]
      ; Az "Elem törlése" művelet sikertelen befejeződésekor, ...
      ; ... megjelenít egy értesítést.
      ; ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      (let [item-id (r update.subs/get-deleted-item-id db handler-id server-response)]
           {:dispatch    [:item-handler/render-delete-item-failed-dialog! handler-id item-id]
            :dispatch-if [(r x.ui/process-faked? db)
                          [:x.ui/end-fake-process!]]})))

(r/reg-event-fx :item-handler/render-item-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ handler-id item-id]]
      [:x.ui/render-bubble! ::item-deleted-dialog
                            {:body [update.views/item-deleted-dialog-body handler-id item-id]}]))

(r/reg-event-fx :item-handler/render-delete-item-failed-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ handler-id item-id]]
      [:x.ui/render-bubble! ::delete-item-failed-dialog
                            {:body :failed-to-delete}]))



;; -- Undo delete item effects ------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/undo-delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ handler-id item-id]]
      ; Az [:item-handler/undo-delete-item! ...] esemény ...
      ; ...
      (let [query        (r update.queries/get-undo-delete-item-query          db handler-id item-id)
            validator-f #(r update.validators/undo-delete-item-response-valid? db handler-id %)]
           {:db       (r x.ui/fake-process! db 15)
            :dispatch-n [[:x.ui/remove-bubble! ::item-deleted-dialog]
                         [:pathom/send-query! (r core.subs/get-request-id db handler-id)
                                              {:display-progress? false
                                               :on-success [:item-handler/delete-item-undid       handler-id item-id]
                                               :on-failure [:item-handler/undo-delete-item-failed handler-id]
                                               :query query :validator-f validator-f}]]})))

(r/reg-event-fx :item-handler/delete-item-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id item-id _]]
      ; (A) Ha a "Törölt elem visszaállítása" művelet sikeres befejeződésekor az engine rendelkezik
      ;     az elem útvonalának elkészítéséhez szükséges tulajdonságokkal ...
      ;     ... elkészíti az elemhez tartozó útvonalat és átírányít arra.
      ;     ... az útvonal használatakor befejeződik a progress-bar elemen 15%-ig szimulált folyamat.

      ; (B) Ha a "Törölt elem visszaállítása" művelet sikeres befejeződésekor az engine NEM rendelkezik
      ;     az útvonal elkészítéséhez szükséges tulajdonságokkal ...
      ;     ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      (if-let [item-route (r routes.subs/get-item-route db handler-id item-id)]
              ; (A)
              [:x.router/go-to! item-route]
              ; (B)
              {:dispatch-if [(r x.ui/process-faked? db)
                             [:x.ui/end-fake-process!]]})))

(r/reg-event-fx :item-handler/undo-delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id server-response]]
      ; A "Törölt elem visszaállítása" művelet sikertelen befejeződésekor, ...
      ; ... megjelenít egy értesítést.
      ; ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      (let [item-id (r update.subs/get-deleted-item-id db handler-id server-response)]
           {:dispatch    [:item-handler/render-undo-delete-item-failed-dialog! handler-id item-id]
            :dispatch-if [(r x.ui/process-faked? db)
                          [:x.ui/end-fake-process!]]})))

(r/reg-event-fx :item-handler/render-undo-delete-item-failed-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (string) item-id
  (fn [_ [_ handler-id item-id]]
      [:x.ui/render-bubble! ::undo-delete-item-failed-dialog
                            {:body [update.views/undo-delete-item-failed-dialog-body handler-id item-id]}]))



;; -- Duplicate item effects --------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/duplicate-item!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ; [:item-handler/duplicate-item! :my-handler]
  (fn [{:keys [db]} [_ handler-id]]
      (let [display-progress? (r body.subs/get-body-prop                          db handler-id :display-progress?)
            query             (r update.queries/get-duplicate-item-query          db handler-id)
            validator-f      #(r update.validators/duplicate-item-response-valid? db handler-id %)]
           [:pathom/send-query! (r core.subs/get-request-id db handler-id)
                                {:display-progress? display-progress?
                                 :on-success [:item-handler/item-duplicated       handler-id]
                                 :on-failure [:item-handler/duplicate-item-failed handler-id]
                                 :query query :validator-f validator-f}])))

(r/reg-event-fx :item-handler/item-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id server-response]]
      ; Ha az "Elem duplikálása" művelet sikeres volt, ...
      ; ... megjelenít egy értesítést.
      (let [copy-id (r update.subs/get-duplicated-item-id db handler-id server-response)]
           [:item-handler/render-item-duplicated-dialog! handler-id copy-id])))

(r/reg-event-fx :item-handler/duplicate-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  (fn [_ [_ _ _]]
      ; Ha az "Elem duplikálása" művelet sikertelen volt, ...
      ; ... megjelenít egy értesítést.
      [:x.ui/render-bubble! ::duplicate-item-failed-dialog
                            {:body :failed-to-duplicate}]))

(r/reg-event-fx :item-handler/render-item-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (string) copy-id
  (fn [_ [_ handler-id copy-id]]
      [:x.ui/render-bubble! ::item-duplicated-dialog
                            {:body [update.views/item-duplicated-dialog-body handler-id copy-id]}]))

(r/reg-event-fx :item-handler/handle-duplicated-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (string) copy-id
  (fn [_ [_ handler-id copy-id]]
      {:dispatch-n [[:x.ui/remove-bubble! ::item-duplicated-dialog]
                    [:item-handler/handle-item! handler-id copy-id]]}))
