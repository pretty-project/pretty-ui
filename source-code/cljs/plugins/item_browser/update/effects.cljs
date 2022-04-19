
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.update.effects
    (:require [plugins.item-browser.core.subs         :as core.subs]
              [plugins.item-browser.mount.subs        :as mount.subs]
              [plugins.item-browser.items.subs        :as items.subs]
              [plugins.item-browser.update.events     :as update.events]
              [plugins.item-browser.update.queries    :as update.queries]
              [plugins.item-browser.update.subs       :as update.subs]
              [plugins.item-browser.update.validators :as update.validators]
              [plugins.item-browser.update.views      :as update.views]
              [x.app-core.api                         :as a :refer [r]]
              [x.app-ui.api                           :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/render-item-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  (fn [_ [_ browser-id item-id]]
      [:ui/blow-bubble! :plugins.item-browser/item-deleted-dialog
                        {:body [update.views/item-deleted-dialog-body browser-id item-id]}]))
                         ;:destructor [:item-browser/clean-backup-items! browser-id item-id]}]))

(a/reg-event-fx
  :item-browser/render-item-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) copy-id
  (fn [_ [_ browser-id copy-id]]
      [:ui/blow-bubble! :plugins.item-browser/item-duplicated-dialog
                        {:body [update.views/item-duplicated-dialog-body browser-id copy-id]}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/update-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) item-changes
  ;
  ; @usage
  ;  [:item-browser/update-item! :my-browser "my-item" {...}]
  (fn [{:keys [db]} [_ browser-id item-id item-changes]]
      ; - Az [:item-browser/update-item! ...] esemény az item-changes paraméterként átadott változásokat
      ;   azonnal végrahajta az elemen.
      ;
      ; - Ha az elem szerver-oldali változatának felülírása sikertelen volt, akkor a kliens-oldali
      ;   változat a tárolt biztonsági mentésből helyreállítódik.
      ;
      ; - Egy időben egy változtatást lehetséges az elemen végrehajtani, mert egy darab biztonsági
      ;   mentéssel nem lehetséges az időben átfedésbe kerülő változtatásokat kezelni, ezért a szerver
      ;   válaszának megérkezéséig az elem {:disabled? true} állapotban van.
      (let [db           (r update.events/update-item!                    db browser-id item-id item-changes)
            query        (r update.queries/get-update-item-query          db browser-id item-id)
            validator-f #(r update.validators/update-item-response-valid? db browser-id %)]
           {:db db :dispatch [:sync/send-query! :storage.media-browser/update-item!
                                                {:display-progress? true
                                                 :on-success [:item-browser/item-updated       browser-id item-id]
                                                 :on-failure [:item-browser/update-item-failed browser-id item-id]
                                                 :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-browser/item-updated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id item-id _]]
      ; Ha az "Elem felülírása" művelet sikeres befejeződésekor a felülírt elem
      ; a megjelenített listaelemek között van, ...
      ; ... engedélyezi az ideiglenesen letiltott elemet.
      (if (r items.subs/item-downloaded? db browser-id item-id)
          {:db (r update.events/item-updated db browser-id item-id)})))

(a/reg-event-fx
  :item-browser/update-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id item-id _]]
      ; A) Ha az "Elem felülírása" művelet sikertelen befejeződésekor a felülírt elem
      ;    a megjelenített listaelemek között van, ...
      ;    ... engedélyezi az ideiglenesen letiltott elemet.
      ;    ... visszaállítja a felülírt elem kliens-oldali változatatát a változtatás előtti állapotra.
      ;    ... megjelenít egy értesítést.
      ;
      ; B) Ha az "Elem felülírása" művelet sikertelen befejeződésekor a felülírt elem
      ;    NINCS a megjelenített listaelemek között, ...
      ;    ... megjelenít egy értesítést.
      (if (r items.subs/item-downloaded? db browser-id item-id)
          ; A)
          {:db       (r update.events/update-item-failed db browser-id item-id)
           :dispatch [:ui/blow-bubble! {:body :failed-to-update}]}
          ; B)
          [:ui/blow-bubble! {:body :failed-to-update}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/delete-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/delete-item! :my-browser "my-item"]
  (fn [{:keys [db]} [_ browser-id item-id]]
      (let [query        (r update.queries/get-delete-item-query          db browser-id item-id)
            validator-f #(r update.validators/delete-item-response-valid? db browser-id %)]
           {:db       (r update.events/delete-item! db browser-id item-id)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db browser-id)
                                         {:on-success [:item-browser/item-deleted       browser-id item-id]
                                          :on-failure [:item-browser/delete-item-failed browser-id item-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-browser/item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id item-id _]]
      ; A) Ha az "Elem törlése" művelet sikeres befejeződésekor a törölt elem
      ;    a megjelenített listaelemek között van, ...
      ;    ... újratölti a listaelemeket, majd megjelenít egy értesítést.
      ;    ... a listaelemek újratöltésekor befejeződik a progress-bar elemen 15%-ig szimulált folyamat.
      ;
      ; B) Ha az "Elem törlése" művelet sikeres befejeződésekor a törölt elem
      ;    NINCS a megjelenített listaelemek között, ...
      ;    ... megjelenít egy értesítést.
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (if (r items.subs/item-downloaded? db browser-id item-id)
          ; A)
          (let [on-reload [:item-browser/render-item-deleted-dialog! browser-id item-id]]
               [:item-browser/reload-items! browser-id {:on-reload on-reload}])
          ; B)
          [:item-browser/render-item-deleted-dialog! browser-id item-id])))

(a/reg-event-fx
  :item-browser/delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id item-id _]]
      ; XXX#0439
      ;
      ; A) Ha az "Elem törlése" művelet sikertelen befejeződésekor a törölt elem
      ;    a megjelenített listaelemek között van, ...
      ;    ... engedélyezi az ideiglenesen letiltott elemet.
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;    ... megjelenít egy értesítést.
      ;
      ; B) Ha az "Elem törlése" művelet sikertelen befejeződésekor a törölt elem
      ;    NINCS a megjelenített listaelemek között, ...
      ;    ... megjelenít egy értesítést.
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (if (r items.subs/item-downloaded? db browser-id item-id)
          ; A)
          {:db         (r update.events/delete-item-failed db browser-id item-id)
           :dispatch-n [[:ui/end-fake-process!]
                        [:ui/blow-bubble! {:body :failed-to-delete}]]}
          ; B)
          [:ui/blow-bubble! {:body :failed-to-delete}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/undo-delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ browser-id item-id]]
      (let [query        (r update.queries/get-undo-delete-item-query          db browser-id item-id)
            validator-f #(r update.validators/undo-delete-item-response-valid? db browser-id %)]
           {:db       (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db browser-id)
                                         {:on-success [:item-browser/delete-item-undid       browser-id]
                                          :on-failure [:item-browser/undo-delete-item-failed browser-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-browser/delete-item-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id server-response]]
      ; A) Ha a "Törölt elem visszaállítása" művelet sikeres befejeződésekor az aktuálisan böngészett
      ;    elem a visszaállított elem szülő-eleme, ...
      ;    ... újratölti a listaelemeket.
      ;    ... a listaelemek újratöltésekor befejeződik a progress-bar elemen 15%-ig szimulált folyamat.
      ;
      ; B) Ha a "Törölt elem visszaállítása" művelet sikeres befejeződésekor az aktuálisan böngészett
      ;    elem NEM a visszaállított elem szülő-eleme, ...
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (if (r update.subs/parent-item-browsed? db browser-id :undo-delete-item! server-response)
          ; A)
          [:item-browser/reload-items! browser-id])))

(a/reg-event-fx
  :item-browser/undo-delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id _]]
      ; XXX#0439
      ;
      ; A) Ha a "Törölt elem visszaállítása" művelet sikertelen befejeződésekor a body komponens
      ;    a React-fába van csatolva, ...
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;    ... megjelenít egy értesítést.
      ;
      ; B) Ha a "Törölt elem visszaállítása" művelet sikertelen befejeződésekor a body komponens
      ;    NINCS a React-fába csatolva ...
      ;    ... megjelenít egy értesítést.
      (if (r mount.subs/body-did-mount? db browser-id)
          ; A)
          {:dispatch-n [[:ui/end-fake-process!]
                        [:ui/blow-bubble! {:body :failed-to-undo-delete}]]}
          ; B)
          [:ui/blow-bubble! {:body :failed-to-undo-delete}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/duplicate-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/duplicate-item! :my-browser "my-item"]
  (fn [{:keys [db]} [_ browser-id item-id]]
      (let [query        (r update.queries/get-duplicate-item-query          db browser-id item-id)
            validator-f #(r update.validators/duplicate-item-response-valid? db browser-id %)]
           {:db       (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db browser-id)
                                         {:on-success [:item-browser/item-duplicated       browser-id]
                                          :on-failure [:item-browser/duplicate-item-failed browser-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-browser/item-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id server-response]]
      ; A) Ha az "Elem duplikálása" művelet sikeres befejeződésekor az aktuálisan böngészett
      ;    elem a duplikált elem szülő-eleme, ...
      ;    ... újratölti a listaelemeket, majd megjelenít egy értesítést.
      ;    ... a listaelemek újratöltésekor befejeződik a progress-bar elemen 15%-ig szimulált folyamat.
      ;
      ; B) Ha az "Elem duplikálása" művelet sikeres befejeződésekor az aktuálisan böngészett
      ;    elem NEM a duplikált elem szülő-eleme, ...
      ;    ... megjelenít egy értesítést.
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (let [copy-id (r update.subs/get-copy-id db browser-id server-response)]
           (if (r update.subs/parent-item-browsed? db browser-id :duplicate-item! server-response)
               ; A)
               (let [on-reload [:item-browser/render-item-duplicated-dialog! browser-id copy-id]]
                    [:item-browser/reload-items! browser-id {:on-reload on-reload}])
               ; B)
               [:item-browser/render-item-duplicated-dialog! browser-id copy-id]))))

(a/reg-event-fx
  :item-browser/duplicate-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      ; XXX#0439
      ;
      ; A) Ha az "Elem duplikálása" művelet sikertelen befejeződésekor a body komponens
      ;    a React-fába van csatolva, ...
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;    ... megjelenít egy értesítést.
      ;
      ; B) Ha az "Elem duplikálása" művelet sikertelen befejeződésekor a body komponens
      ;    NINCS a React-fába csatolva ...
      ;    ... megjelenít egy értesítést.
      (if (r mount.subs/body-did-mount? db browser-id)
          ; A)
          {:dispatch-n [[:ui/end-fake-process!]
                        [:ui/blow-bubble! {:body :failed-to-duplicate}]]}
          ; B)
          [:ui/blow-bubble! {:body :failed-to-duplicate}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/move-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (string) destination-id
  ;
  ; @usage
  ;  [:item-browser/move-item! :my-browser "my-item" "your-item"]
  (fn [{:keys [db]} [_ browser-id item-id destination-id]]))
