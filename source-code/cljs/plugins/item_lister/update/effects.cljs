
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.effects
    (:require [plugins.item-lister.core.subs         :as core.subs]
              [plugins.item-lister.items.subs        :as items.subs]
              [plugins.item-lister.mount.subs        :as mount.subs]
              [plugins.item-lister.update.events     :as update.events]
              [plugins.item-lister.update.queries    :as update.queries]
              [plugins.item-lister.update.subs       :as update.subs]
              [plugins.item-lister.update.validators :as update.validators]
              [plugins.item-lister.update.views      :as update.views]
              [x.app-core.api                        :as a :refer [r]]
              [x.app-ui.api                          :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/render-items-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  (fn [_ [_ lister-id item-ids]]
      [:ui/blow-bubble! :plugins.item-lister/items-deleted-dialog
                        {:body       [update.views/items-deleted-dialog-body lister-id item-ids]
                         :destructor [:item-lister/clean-backup-items!       lister-id item-ids]}]))

(a/reg-event-fx
  :item-lister/render-items-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) copy-ids
  (fn [_ [_ lister-id copy-ids]]
      [:ui/blow-bubble! :plugins.item-lister/items-duplicated-dialog
                        {:body [update.views/items-duplicated-dialog-body lister-id copy-ids]}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/delete-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      (let [item-ids     (r items.subs/get-selected-item-ids               db lister-id)
            query        (r update.queries/get-delete-items-query          db lister-id item-ids)
            validator-f #(r update.validators/delete-items-response-valid? db lister-id %)]
           {:db       (r update.events/delete-selected-items! db lister-id)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db lister-id)
                                         {:on-success [:item-lister/items-deleted       lister-id]
                                          :on-failure [:item-lister/delete-items-failed lister-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-lister/items-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id server-response]]
      ; A) Ha az "Kijelölt elemek törlése" művelet sikeres befejeződésekor a body komponens
      ;    a React-fába van csatolva, ...
      ;    ... újratölti a listaelemeket, majd megjelenít egy értesítést..
      ;    ... a listaelemek újratöltésekor befejeződik a progress-bar elemen 15%-ig szimulált folyamat.
      ;
      ; B) Ha az "Kijelölt elemek törlése" művelet sikeres befejeződésekor a body komponens
      ;    NINCS a React-fába csatolva, ...
      ;    ... megjelenít egy értesítést.
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (let [item-ids (r update.subs/get-deleted-item-ids db lister-id server-response)]
           (if (r mount.subs/body-did-mount? db lister-id)
               ; A)
               (let [on-reload [:item-lister/render-items-deleted-dialog! lister-id item-ids]]
                    [:item-lister/reload-items! lister-id {:on-reload on-reload}])
               ; B)
               [:item-lister/render-items-deleted-dialog! lister-id item-ids]))))

(a/reg-event-fx
  :item-lister/delete-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id _]]
      ; XXX#0439
      ;
      ; A) Ha az "Kijelölt elemek törlése" művelet sikertelen befejeződésekor a body komponens
      ;    a React-fába van csatolva, ...
      ;    ... kilépteti a plugint a {:select-mode? true} állapotból.
      ;    ... engedélyezi az ideiglenesen letiltott elemeket.
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;    ... megjelenít egy értesítést.
      ;
      ; B) Ha az "Kijelölt elemek törlése" művelet sikertelen befejeződésekor a body komponens
      ;    NINCS a React-fába csatolva, ...
      ;    ... megjelenít egy értesítést.
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (if (r mount.subs/body-did-mount? db lister-id)
          ; A)
          {:db         (r update.events/delete-items-failed db lister-id)
           :dispatch-n [[:ui/end-fake-process!]
                        [:ui/blow-bubble! {:body :failed-to-delete}]]}
          ; B)
          [:ui/blow-bubble! {:body :failed-to-delete}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/undo-delete-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  (fn [{:keys [db]} [_ lister-id item-ids]]
      (let [query        (r update.queries/get-undo-delete-items-query          db lister-id item-ids)
            validator-f #(r update.validators/undo-delete-items-response-valid? db lister-id %)]
           {:db       (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db lister-id)
                                         {:on-success [:item-lister/delete-items-undid       lister-id]
                                          :on-failure [:item-lister/undo-delete-items-failed lister-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-lister/delete-items-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id _]]
      ; A) Ha a "Törölt elemek visszaállítása" művelet sikeres befejeződésekor a body komponens
      ;    a React-fába van csatolva, ...
      ;    ... újratölti a listaelemeket.
      ;    ... a listaelemek újratöltésekor befejeződik a progress-bar elemen 15%-ig szimulált folyamat.
      ;
      ; B) Ha a "Törölt elemek visszaállítása" művelet sikeres befejeződésekor a body komponens
      ;    NINCS a React-fába csatolva, ...
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (if (r mount.subs/body-did-mount? db lister-id)
          ; A)
          [:item-lister/reload-items! lister-id])))

(a/reg-event-fx
  :item-lister/undo-delete-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id _]]
      ; XXX#0439
      ;
      ; A) Ha a "Törölt elemek visszaállítása" művelet sikertelen befejeződésekor a body komponens
      ;    a React-fába van csatolva, ...
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;    ... megjelenít egy értesítést.
      ;
      ; B) Ha a "Törölt elemek visszaállítása" művelet sikertelen befejeződésekor a body komponens
      ;    NINCS a React-fába csatolva, ...
      ;    ... megjelenít egy értesítést.
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (if (r mount.subs/body-did-mount? db lister-id)
          ; A)
          {:dispatch-n [[:ui/end-fake-process!]
                        [:ui/blow-bubble! {:body :failed-to-delete}]]}
          ; B)
          [:ui/blow-bubble! {:body :failed-to-delete}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/duplicate-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      (let [item-ids     (r items.subs/get-selected-item-ids                  db lister-id)
            query        (r update.queries/get-duplicate-items-query          db lister-id item-ids)
            validator-f #(r update.validators/duplicate-items-response-valid? db lister-id %)]
           {:db       (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db lister-id)
                                         {:on-success [:item-lister/items-duplicated       lister-id]
                                          :on-failure [:item-lister/duplicate-items-failed lister-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-lister/items-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id server-response]]
      ; A) Ha a "Kijelölt elemek duplikálása" művelet sikeres befejeződésekor a body komponens
      ;    a React-fába van csatolva, ...
      ;    ... megjelenít egy értesítést.
      ;    ... újratölti a listaelemeket.
      ;    ... a listaelemek újratöltésekor befejeződik a progress-bar elemen 15%-ig szimulált folyamat.
      ;
      ; B) Ha a "Kijelölt elemek duplikálása" művelet sikeres befejeződésekor a body komponens
      ;    NINCS a React-fába csatolva, ...
      ;    ... megjelenít egy értesítést.
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (let [copy-ids (r update.subs/get-duplicated-item-ids db lister-id server-response)]
           (if (r mount.subs/body-did-mount? db lister-id)
               ; A)
               (let [on-reload [:item-lister/render-items-duplicated-dialog! lister-id copy-ids]]
                    [:item-lister/reload-items! lister-id {:on-reload on-reload}])
               ; B)
               [:item-lister/render-items-duplicated-dialog! lister-id copy-ids]))))

(a/reg-event-fx
  :item-lister/duplicate-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id _]]
      ; XXX#0439
      ;
      ; A) Ha a "Kijelölt elemek duplikálása" művelet sikertelen befejeződésekor a body komponens
      ;    a React-fába van csatolva, ...
      ;    ... kilépteti a plugint a {:select-mode? true} állapotból.
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;    ... megjelenít egy értesítést.
      ;
      ; B) Ha a "Kijelölt elemek duplikálása" művelet sikertelen befejeződésekor a body komponens
      ;    NINCS a React-fába csatolva, ...
      ;    ... megjelenít egy értesítést.
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (if (r mount.subs/body-did-mount? db lister-id)
          ; A)
          {:db         (r update.events/duplicate-items-failed db lister-id)
           :dispatch-n [[:ui/end-fake-process!]
                        [:ui/blow-bubble! {:body :failed-to-duplicate}]]}
          ; B)
          [:ui/blow-bubble! {:body :failed-to-duplicate}])))
