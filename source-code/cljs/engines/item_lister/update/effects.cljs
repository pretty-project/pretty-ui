
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.update.effects
    (:require [engines.item-lister.body.subs         :as body.subs]
              [engines.item-lister.core.subs         :as core.subs]
              [engines.item-lister.selection.subs    :as selection.subs]
              [engines.item-lister.update.events     :as update.events]
              [engines.item-lister.update.queries    :as update.queries]
              [engines.item-lister.update.subs       :as update.subs]
              [engines.item-lister.update.validators :as update.validators]
              [engines.item-lister.update.views      :as update.views]
              [re-frame.api                          :as r :refer [r]]
              [x.ui.api                              :as x.ui]))



;; -- Reorder items effects ---------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/reorder-items!
  ; @param (keyword) lister-id
  ; @param (maps in vector) reordered-items
  ;
  ; @usage
  ;  [:item-lister/reorder-items! :my-lister [{...} {...}]]
  (fn [{:keys [db]} [_ lister-id reordered-items]]
      (let [db                (r update.events/reorder-items!                    db lister-id reordered-items)
            display-progress? (r body.subs/get-body-prop                         db lister-id :display-progress?)
            query             (r update.queries/get-reorder-items-query          db lister-id)
            validator-f      #(r update.validators/reorder-items-response-valid? db lister-id %)]
           {:db db :dispatch [:pathom/send-query! (r core.subs/get-request-id db lister-id)
                                                  {:display-progress? display-progress?
                                                   :on-success [:item-lister/items-reordered      lister-id]
                                                   :on-failure [:item-lister/reorder-items-failed lister-id]
                                                   :query query :validator-f validator-f}]})))

(r/reg-event-fx :item-lister/items-reordered
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [_ [_ lister-id _]]
      [:item-lister/render-items-reordered-dialog! lister-id]))

(r/reg-event-fx :item-lister/reorder-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [_ [_ lister-id _]]
      [:item-lister/render-reorder-items-failed-dialog! lister-id]))

(r/reg-event-fx :item-lister/render-items-reordered-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [_ [_ lister-id]]
      [:x.ui/render-bubble! ::items-reordered-dialog
                            {:body [update.views/items-reordered-dialog-body lister-id]}]))

(r/reg-event-fx :item-lister/render-reorder-items-failed-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [_ [_ lister-id]]
      [:x.ui/render-bubble! ::reorder-items-failed-dialog
                            {:body [update.views/reorder-items-failed-dialog-body lister-id]}]))



;; -- Delete items effects ----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/delete-selected-items!
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [:item-lister/delete-selected-items! :my-lister]
  (fn [{:keys [db]} [_ lister-id]]
      (let [item-ids     (r selection.subs/export-selection                db lister-id)
            query        (r update.queries/get-delete-items-query          db lister-id item-ids)
            validator-f #(r update.validators/delete-items-response-valid? db lister-id %)]
           {:db       (r update.events/delete-selected-items! db lister-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db lister-id)
                                           {:display-progress? false
                                            :on-success [:item-lister/items-deleted       lister-id]
                                            :on-failure [:item-lister/delete-items-failed lister-id]
                                            :query query :validator-f validator-f}]})))

(r/reg-event-fx :item-lister/items-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id server-response]]
      ; (A) Ha az "Kijelölt elemek törlése" művelet sikeres befejeződésekor a body komponens
      ;     a React-fába van csatolva, ...
      ;     ... újratölti a listaelemeket, majd megjelenít egy értesítést..
      ;     ... a listaelemek újratöltésekor befejeződik a progress-bar elemen 15%-ig szimulált folyamat.
      ;
      ; (B) Ha az "Kijelölt elemek törlése" művelet sikeres befejeződésekor a body komponens
      ;     NINCS a React-fába csatolva, ...
      ;     ... megjelenít egy értesítést.
      ;     ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (let [item-ids (r update.subs/get-deleted-item-ids db lister-id server-response)]
           (if (r body.subs/body-did-mount? db lister-id)
               ; (A)
               (let [on-reload [:item-lister/render-items-deleted-dialog! lister-id item-ids]]
                    [:item-lister/reload-items! lister-id {:on-reload on-reload}])
               ; (B)
               [:item-lister/render-items-deleted-dialog! lister-id item-ids]))))

(r/reg-event-fx :item-lister/delete-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id _]]
      ; XXX#0439
      ;
      ; (A) Ha az "Kijelölt elemek törlése" művelet sikertelen befejeződésekor a body komponens
      ;     a React-fába van csatolva, ...
      ;     ... engedélyezi az ideiglenesen letiltott elemeket.
      ;     ... megjelenít egy értesítést.
      ;     ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;
      ; (B) Ha az "Kijelölt elemek törlése" művelet sikertelen befejeződésekor a body komponens
      ;     NINCS a React-fába csatolva, ...
      ;     ... megjelenít egy értesítést.
      ;     ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      (if (r body.subs/body-did-mount? db lister-id)
          ; (A)
          {:db          (r update.events/delete-items-failed db lister-id)
           :dispatch    [:x.ui/render-bubble! {:body :failed-to-delete}]
           :dispatch-if [(r x.ui/process-faked? db)
                         [:x.ui/end-fake-process!]]}
          ; (B)
          {:dispatch    [:x.ui/render-bubble! {:body :failed-to-delete}]
           :dispatch-if [(r x.ui/process-faked? db)
                         [:x.ui/end-fake-process!]]})))

(r/reg-event-fx :item-lister/render-items-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  (fn [_ [_ lister-id item-ids]]
      [:x.ui/render-bubble! ::items-deleted-dialog
                            {:body             [update.views/items-deleted-dialog-body lister-id item-ids]
                             :on-bubble-closed [:item-lister/clean-backup-items!       lister-id item-ids]}]))



;; -- Undo delete items effects -----------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/undo-delete-items!
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  ;
  ; @usage
  ;  [:item-lister/undo-delete-items! :my-lister ["my-item" "your-item"]]
  (fn [{:keys [db]} [_ lister-id item-ids]]
      (let [query        (r update.queries/get-undo-delete-items-query          db lister-id item-ids)
            validator-f #(r update.validators/undo-delete-items-response-valid? db lister-id %)]
           {:db         (r x.ui/fake-process! db 15)
            :dispatch-n [[:x.ui/remove-bubble! ::items-deleted-dialog]
                         [:pathom/send-query! (r core.subs/get-request-id db lister-id)
                                              {:display-progress? false
                                               :on-success [:item-lister/delete-items-undid       lister-id]
                                               :on-failure [:item-lister/undo-delete-items-failed lister-id]
                                               :query query :validator-f validator-f}]]})))

(r/reg-event-fx :item-lister/delete-items-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id _]]
      ; (A) Ha a "Törölt elemek visszaállítása" művelet sikeres befejeződésekor a body komponens
      ;     a React-fába van csatolva, ...
      ;     ... újratölti a listaelemeket.
      ;     ... a listaelemek újratöltésekor befejeződik a progress-bar elemen 15%-ig szimulált folyamat.
      ;
      ; (B) Ha a "Törölt elemek visszaállítása" művelet sikeres befejeződésekor a body komponens
      ;     NINCS a React-fába csatolva, ...
      ;     ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      (if (r body.subs/body-did-mount? db lister-id)
          ; (A)
          [:item-lister/reload-items! lister-id]
          ; (B)
          {:dispatch-if [(r x.ui/process-faked? db)
                         [:x.ui/end-fake-process!]]})))

(r/reg-event-fx :item-lister/undo-delete-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id _]]
      ; XXX#0439
      ;
      ; A "Törölt elemek visszaállítása" művelet sikertelen befejeződésekor, ...
      ; ... megjelenít egy értesítést.
      ; ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      {:dispatch    [:x.ui/render-bubble! {:body :failed-to-undo-delete}]
       :dispatch-if [(r x.ui/process-faked? db)
                     [:x.ui/end-fake-process!]]}))



;; -- Duplicate items effects -------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/duplicate-selected-items!
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [:item-lister/duplicate-selected-items! :my-lister]
  (fn [{:keys [db]} [_ lister-id]]
      (let [item-ids     (r selection.subs/export-selection                   db lister-id)
            query        (r update.queries/get-duplicate-items-query          db lister-id item-ids)
            validator-f #(r update.validators/duplicate-items-response-valid? db lister-id %)]
           {:db       (r x.ui/fake-process! db 15)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db lister-id)
                                           {:display-progress? false
                                            :on-success [:item-lister/items-duplicated       lister-id]
                                            :on-failure [:item-lister/duplicate-items-failed lister-id]
                                            :query query :validator-f validator-f}]})))

(r/reg-event-fx :item-lister/items-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id server-response]]
      ; (A) Ha a "Kijelölt elemek duplikálása" művelet sikeres befejeződésekor a body komponens
      ;     a React-fába van csatolva, ...
      ;     ... megjelenít egy értesítést.
      ;     ... újratölti a listaelemeket.
      ;     ... a listaelemek újratöltésekor befejeződik a progress-bar elemen 15%-ig szimulált folyamat.
      ;
      ; (B) Ha a "Kijelölt elemek duplikálása" művelet sikeres befejeződésekor a body komponens
      ;     NINCS a React-fába csatolva, ...
      ;     ... megjelenít egy értesítést.
      ;     ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (let [copy-ids (r update.subs/get-duplicated-item-ids db lister-id server-response)]
           (if (r body.subs/body-did-mount? db lister-id)
               ; (A)
               (let [on-reload [:item-lister/render-items-duplicated-dialog! lister-id copy-ids]]
                    [:item-lister/reload-items! lister-id {:on-reload on-reload}])
               ; (B)
               [:item-lister/render-items-duplicated-dialog! lister-id copy-ids]))))

(r/reg-event-fx :item-lister/duplicate-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id _]]
      ; XXX#0439
      ;
      ; (A) Ha a "Kijelölt elemek duplikálása" művelet sikertelen befejeződésekor a body komponens
      ;     a React-fába van csatolva, ...
      ;     ... megjelenít egy értesítést.
      ;     ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;
      ; (B) Ha a "Kijelölt elemek duplikálása" művelet sikertelen befejeződésekor a body komponens
      ;     NINCS a React-fába csatolva, ...
      ;     ... megjelenít egy értesítést.
      ;     ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      (if (r body.subs/body-did-mount? db lister-id)
          ; (A)
          {:db          (r update.events/duplicate-items-failed db lister-id)
           :dispatch    [:x.ui/render-bubble! {:body :failed-to-duplicate}]
           :dispatch-if [(r x.ui/process-faked? db)
                         [:x.ui/end-fake-process!]]}
          ; (B)
          {:dispatch    [:x.ui/render-bubble! {:body :failed-to-duplicate}]
           :dispatch-if [(r x.ui/process-faked? db)
                         [:x.ui/end-fake-process!]]})))

(r/reg-event-fx :item-lister/render-items-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) copy-ids
  (fn [_ [_ lister-id copy-ids]]
      [:x.ui/render-bubble! ::items-duplicated-dialog
                            {:body [update.views/items-duplicated-dialog-body lister-id copy-ids]}]))
