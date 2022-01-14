
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.8.0
; Compatibility: x4.5.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.events
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [app-plugins.item-lister.engine  :as engine]
              [app-plugins.item-lister.queries :as queries]
              [app-plugins.item-lister.subs    :as subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-lister-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;
  ; @return (map)
  [db [_ extension-id _ lister-props]]
  ; XXX#8705
  ; Az item-lister betöltésekor felülírás nélkül aláfűzi a lister-props térképet
  ; az item-lister/meta-items térképnek, így az item-lister plugin legutóbbi
  ; beállításai elérhetők maradnak.
  (update-in db [extension-id :item-lister/meta-items] map/reverse-merge lister-props))

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (-> db (dissoc-in [extension-id :item-lister/data-items])
         (dissoc-in [extension-id :item-lister/meta-items :document-count])
         (dissoc-in [extension-id :item-lister/meta-items :received-count])
         (dissoc-in [extension-id :item-lister/meta-items :synchronized?])))

(defn reset-search!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (dissoc-in db [extension-id :item-lister/meta-items :search-term]))

(defn reset-selections!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (dissoc-in db [extension-id :item-lister/meta-items :selected-items]))

(defn toggle-search-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (update-in db [extension-id :item-lister/meta-items :search-mode?] not))

(a/reg-event-db :item-lister/toggle-search-mode! toggle-search-mode!)

(defn toggle-select-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (as-> db % (update-in % [extension-id :item-lister/meta-items :select-mode?] not)
             (dissoc-in % [extension-id :item-lister/meta-items :selected-items])))

(a/reg-event-db :item-lister/toggle-select-mode! toggle-select-mode!)

(defn toggle-reorder-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (update-in db [extension-id :item-lister/meta-items :reorder-mode?] not))

(a/reg-event-db :item-lister/toggle-reorder-mode! toggle-reorder-mode!)

(defn select-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (let [downloaded-item-count (r subs/get-downloaded-item-count db extension-id)
        item-selections       (vec (range 0 downloaded-item-count))]
       (assoc-in db [extension-id :item-lister/meta-items :selected-items] item-selections)))

(a/reg-event-db :item-lister/select-all-items! select-all-items!)

(defn unselect-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (dissoc-in db [extension-id :item-lister/meta-items :selected-items]))

(a/reg-event-db :item-lister/unselect-all-items! unselect-all-items!)

(defn toggle-item-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r events/toggle-item-selection! :my-extension :my-type 0)
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-dex]]
  (if (r subs/item-selected? db extension-id item-namespace item-dex)
      (update-in db [extension-id :item-lister/meta-items :selected-items] vector/remove-item item-dex)
      (update-in db [extension-id :item-lister/meta-items :selected-items] vector/conj-item   item-dex)))

(a/reg-event-db :item-lister/toggle-item-selection! toggle-item-selection!)

(defn disable-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ;  (r events/disable-items! db :my-extension [0 1 4])
  ;
  ; @return (map)
  [db [_ extension-id _ item-dexes]]
  (update-in db [extension-id :item-lister/meta-items :disabled-items]
             vector/concat-items item-dexes))

(defn enable-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ;  (r events/enable-items! db :my-extension [0 1 4])
  ;
  ; @return (map)
  [db [_ extension-id _ item-dexes]]
  (update-in db [extension-id :item-lister/meta-items :disabled-items]
             vector/remove-items item-dexes))

(defn enable-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (dissoc-in db [extension-id :item-lister/meta-items :disabled-items]))

(defn disable-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [selected-item-dexes (r subs/get-selected-item-dexes db extension-id item-namespace)]
       (r disable-items! db extension-id item-namespace selected-item-dexes)))

(defn remove-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [selected-item-dexes (r subs/get-selected-item-dexes db extension-id item-namespace)
        selected-item-count (r subs/get-selected-item-count db extension-id item-namespace)]
       (-> db (update-in [extension-id :item-lister/data-items] vector/remove-nth-items selected-item-dexes)
              (update-in [extension-id :item-lister/meta-items :document-count] - selected-item-count))))

(defn backup-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (reduce (fn [db item-dex]
              (let [item-id (get-in db [extension-id :item-lister/data-items item-dex :id])
                    item    (get-in db [extension-id :item-lister/data-items item-dex])]
                   (assoc-in db [extension-id :item-lister/backup-items item-id] item)))
          (param db)
          (r subs/get-selected-item-dexes db extension-id item-namespace)))

(defn clean-backup-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) item-ids
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-ids]]
  (update-in db [extension-id :item-lister/backup-items] map/remove-items item-ids))

(a/reg-event-db :item-lister/clean-backup-items! clean-backup-items!)

(defn use-filter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) filter-pattern
  ;
  ; @return (map)
  [db [_ extension-id item-namespace filter-pattern]]
  (assoc-in db [extension-id :item-lister/meta-items :filter-pattern] filter-pattern))

(defn receive-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (let [resolver-id    (engine/resolver-id extension-id item-namespace)
        documents      (get-in server-response [resolver-id :documents])
        document-count (get-in server-response [resolver-id :document-count])
        received-count (count documents)]
                  ; XXX#3907
                  ; Az item-lister a dokumentumokat névtér nélkül tárolja, így
                  ; a lista-elemek felsorolásakor és egyes Re-Frame feliratkozásokban
                  ; a dokumentumok egyes értékeinek olvasása kevesebb erőforrást igényel.
       (as-> db % (r db/store-collection! % [extension-id :item-lister/data-items] documents {:remove-namespace? true})
                  ; A névtér nélkül tárolt dokumentumokon végzett műveletkhez egyes külső
                  ; moduloknak szüksége lehet a dokumentumok névterének ismeretére!
                  (assoc-in % [extension-id :item-lister/meta-items :item-namespace] item-namespace)
                  ; Szükséges frissíteni a keresési feltételeknek megfelelő
                  ; dokumentumok számát, mert annak megváltozhat az értéke!
                  (assoc-in % [extension-id :item-lister/meta-items :document-count] document-count)
                  ; Szükséges eltárolni, hogy megtörtént-e az első kommunikáció a szerverrel!
                  (assoc-in % [extension-id :item-lister/meta-items :synchronized?] true)
                  ; BUG#7009
                  ; Ha a legutoljára letöltött dokumentumok száma 0, de a letöltött dokumentumok
                  ; száma kevesebb, mint a szerverről érkezett document-count érték, akkor
                  ; az elemek letöltésével kapcsolatban valamilyen hiba történt.
                  ; Az ilyen típusú hibák megállapításához szükséges a received-count
                  ; értéket eltárolni.
                  (assoc-in % [extension-id :item-lister/meta-items :received-count] received-count))))

(defn load-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace lister-props]]
  ; XXX#8705
  ; Az item-lister plugin ...
  ; ... az első betöltődésekor letölti az elemeket az alapbeállításokkal.
  ; ... a további betöltődésekkor letölti az elemeket a legutóbb használt beállításokkal,
  ;     ezért nem törli ki a beállításokat!
  (as-> db % (r reset-downloads!    % extension-id)
             (r reset-search!       % extension-id)
             (r store-lister-props! % extension-id item-namespace lister-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/use-filter!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) filter-pattern
  ;
  ; @usage
  ;  [:item-lister/use-filter! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace filter-pattern]]
      {:db (as-> db % (r reset-downloads!  % extension-id)
                      (r reset-selections! % extension-id)
                      (r use-filter!       % extension-id item-namespace filter-pattern))
       :dispatch [:tools/reload-infinite-loader! extension-id]}))

(a/reg-event-fx
  :item-lister/delete-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r disable-selected-items! db extension-id item-namespace)
       :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                     ; XXX#3701
                                    {:on-stalled [:item-lister/->selected-items-deleted extension-id item-namespace]
                                     :on-failure [:ui/blow-bubble! {:body {:content :failed-to-delete}}]
                                     :query      (r queries/get-delete-selected-items-query db extension-id item-namespace)}]}))

(a/reg-event-fx
  :item-lister/undo-delete-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  (fn [{:keys [db]} [_ extension-id item-namespace item-ids]]
      [:sync/send-query! (engine/request-id extension-id item-namespace)
                          ; Ha {:on-success ...} időzítéssel történne meg a [.../->delete-items-undid ...]
                          ; esemény, ami újratölti az elemek listáját, akkor az [.../undo-delete-items! ...]
                          ; esemény által küldött request állapotjelző sávjának megjelenítésére nem
                          ; jutna elegendő idő
                         {:on-stalled [:item-lister/->delete-items-undid extension-id]
                          :on-failure [:ui/blow-bubble! {:body {:content :failed-to-undo-delete}}]
                          :query      (r queries/get-undo-delete-items-query db extension-id item-namespace item-ids)}]))

(a/reg-event-fx
  :item-lister/search-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db       (r reset-downloads!          db extension-id)
       :dispatch [:tools/reload-infinite-loader! extension-id]}))

(a/reg-event-fx
  :item-lister/order-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db       (r reset-downloads!          db extension-id)
       :dispatch [:tools/reload-infinite-loader! extension-id]}))

(a/reg-event-fx
  :item-lister/receive-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      {:db (r receive-items! db extension-id item-namespace server-response)
       ; Az elemek letöltődése után újratölti az infinite-loader komponenst, hogy megállapítsa,
       ; hogy az a viewport területén van-e még és szükséges-e további elemeket letölteni.
       :dispatch [:tools/reload-infinite-loader! extension-id]}))

(a/reg-event-fx
  :item-lister/request-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      ; Ha az infinite-loader komponens ismételten megjelenik a viewport területén, csak abban
      ; az esetben próbáljon újabb elemeket letölteni, ha még nincs az összes letöltve.
      (if (r subs/download-more-items? db extension-id item-namespace)
          [:sync/send-query! (engine/request-id extension-id item-namespace)
                              ; A letöltött dokumentumok on-success helyett on-stalled időpontban
                              ; kerülnek tárolásra a Re-Frame adatbázisba, így elkerülhető,
                              ; hogy a request idle-timeout ideje alatt az újonnan letöltött
                              ; dokumentumok már kirenderelésre kerüljenek, amíg a letöltést jelző
                              ; felirat még megjelenik a lista végén.
                             {:on-stalled [:item-lister/receive-items! extension-id item-namespace]
                              :query      (r queries/get-request-items-query db extension-id item-namespace)}])))

(a/reg-event-fx
  :item-lister/load-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;  {:label (metamorphic-content)}
  (fn [{:keys [db]} [_ extension-id item-namespace {:keys [label] :as lister-props}]]
      {:db (r load-lister! db extension-id item-namespace lister-props)
       :dispatch-n [[:ui/listen-to-process! (engine/request-id extension-id item-namespace)]
                    [:ui/set-header-title!  (param label)]
                    [:ui/set-window-title!  (param label)]
                    (engine/load-extension-event extension-id item-namespace)]}))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/->selected-items-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [item-ids (r subs/get-selected-item-ids db extension-id item-namespace)]
           {:db (as-> db % (r backup-selected-items! % extension-id item-namespace)
                           (r remove-selected-items! % extension-id item-namespace)
                           (r reset-selections!      % extension-id item-namespace)
                           (r enable-all-items!      % extension-id item-namespace))
            :dispatch-n [[:item-lister/render-items-deleted-dialog! extension-id item-namespace item-ids]
                         ; XXX#5501
                         ; A törölt lista-elemek listából való eltávolítása után újratölti
                         ; az infinite-loader komponenst, hogy megállapítsa, szükségés-e további
                         ; elemeket letölteni.
                         [:tools/reload-infinite-loader! extension-id]]})))

(a/reg-event-fx
  :item-lister/->delete-items-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  (fn [{:keys [db]} [_ extension-id]]
      ; XXX#5561
      ; A törölt elemek visszaállítása után feleslegesen komplex művelet lenne megállapítani,
      ; hogy a visszaállított elemek megjelenjenek-e a listában és ha igen, milyen pozícióban,
      ; ezért a sikeres visszaállítás után az item-lister plugin újratölti a listát.
      {:db       (r reset-downloads!          db extension-id)
       :dispatch [:tools/reload-infinite-loader! extension-id]}))
