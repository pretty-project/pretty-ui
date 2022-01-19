
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.9.4
; Compatibility: x4.5.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.events
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-ui.api      :as ui]
              [x.app-environment.api           :as environment]
              [app-plugins.item-lister.engine  :as engine]
              [app-plugins.item-lister.queries :as queries]
              [app-plugins.item-lister.subs    :as subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  ; Az item-lister plugin betöltésekor gondoskodni kell, arról hogy az előző betöltéskor
  ; esetlegesen beállított {:error-mode? true} beállítás törlődjön!
  (assoc-in db [extension-id :item-lister/meta-items :error-mode?] true))

(defn store-lister-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace lister-props]]
         ; XXX#8705
         ; Az item-lister betöltésekor felülírás nélkül aláfűzi a lister-props térképet
         ; az item-lister/meta-items térképnek, így az item-lister plugin legutóbbi
         ; beállításai elérhetők maradnak.
  (-> db (update-in [extension-id :item-lister/meta-items] map/reverse-merge lister-props)
         ; XXX#8706
         ; A névtér nélkül tárolt dokumentumokon végzett műveletkhez egyes külső
         ; moduloknak szüksége lehet a dokumentumok névterének ismeretére!
         (assoc-in  [extension-id :item-lister/meta-items :item-namespace] item-namespace)))

(defn reset-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (-> db (dissoc-in [extension-id :item-lister/meta-items :error-mode?])
         (dissoc-in [extension-id :item-lister/meta-items :select-mode?])
         (dissoc-in [extension-id :item-lister/meta-items :selected-items])))

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
         (dissoc-in [extension-id :item-lister/meta-items :items-received?])))

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

(defn toggle-reload-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (update-in db [extension-id :item-lister/meta-items :reload-mode?] not))

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
      (-> db (assoc-in  [extension-id :item-lister/meta-items :select-mode?] true)
             (update-in [extension-id :item-lister/meta-items :selected-items] vector/remove-item item-dex))
      (-> db (assoc-in  [extension-id :item-lister/meta-items :select-mode?] true)
             (update-in [extension-id :item-lister/meta-items :selected-items] vector/conj-item   item-dex))))

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

(defn ->items-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (as-> db % ; XXX#0499
             ; Szükséges eltárolni, hogy megtörtént-e az első kommunikáció a szerverrel!
             (assoc-in % [extension-id :item-lister/meta-items :items-received?] true)
             (if-let [reload-mode? (r subs/get-meta-item db extension-id item-namespace :reload-mode?)]
                     ; A {:reload-mode? true} beállítással indított letöltés befejezésekor kilép
                     ; a {:reload-mode? true} beállításból
                     (r toggle-reload-mode! % extension-id)
                     (return                %))))

(defn store-received-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (let [resolver-id (engine/resolver-id extension-id item-namespace :get)
        documents   (get-in server-response [resolver-id :documents])
        ; XXX#3907
        ; Az item-lister a dokumentumokat névtér nélkül tárolja, így
        ; a lista-elemek felsorolásakor és egyes Re-Frame feliratkozásokban
        ; a dokumentumok egyes értékeinek olvasása kevesebb erőforrást igényel,
        ; ha nem szükséges az értékek kulcsaihoz az aktuális névteret hozzáfűzni.
        documents (db/collection->non-namespaced-collection documents)]
       (if-let [reload-mode? (r subs/get-meta-item db extension-id item-namespace :reload-mode?)]
               (assoc-in  db [extension-id :item-lister/data-items]                     documents)
               (update-in db [extension-id :item-lister/data-items] vector/concat-items documents))))

(defn receive-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (let [resolver-id    (engine/resolver-id extension-id item-namespace :get)
        documents      (get-in server-response [resolver-id :documents])
        document-count (get-in server-response [resolver-id :document-count])
        received-count (count documents)]
       (as-> db % (r store-received-items! % extension-id item-namespace server-response)
                  ; Szükséges frissíteni a keresési feltételeknek megfelelő dokumentumok számát,
                  ; mert annak megváltozhat az értéke!
                  (assoc-in % [extension-id :item-lister/meta-items :document-count] document-count)
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
  (let [request-id (engine/request-id extension-id item-namespace)]
       (as-> db % (r ui/listen-to-process! % request-id)
                  (r reset-lister!         % extension-id)
                  (r reset-downloads!      % extension-id)
                  (r reset-search!         % extension-id)
                  (r store-lister-props!   % extension-id item-namespace lister-props))))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/reload-lister!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-lister/reload-lister! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r toggle-reload-mode! db extension-id)
       :dispatch [:item-lister/request-items! extension-id item-namespace]}))

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
                         {:on-stalled [:item-lister/->delete-items-undid extension-id item-namespace]
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
      {:db (as-> db % (r receive-items!   % extension-id item-namespace server-response)
                      (r ->items-received % extension-id item-namespace))
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
      (if (r subs/request-items? db extension-id item-namespace)
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
       :dispatch-n [[:environment/reg-keypress-listener! :item-lister/keypress-listener]
                    [:ui/set-header-title! label]
                    [:ui/set-window-title! label]
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
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      [:item-lister/refresh-item-list! extension-id item-namespace]))

(a/reg-event-fx
  :item-lister/->lister-leaved
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [:environment/remove-keypress-listener! :item-lister/keypress-listener])

(a/reg-event-fx
  :item-lister/->item-clicked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) event-props
  ;  {:on-click (metamorphic-event)}
  ; @param (integer) item-dex
  ; @param (map) item
  (fn [{:keys [db]} [_ extension-id item-named {:keys [on-click]} item-dex item]]
      (if (or (r environment/key-pressed? db 16)
              (r environment/key-pressed? db 91))
          [:item-lister/toggle-item-selection! extension-id item-named item-dex]
          (let [on-click (a/metamorphic-event<-params on-click item-dex item)]
               (return on-click)))))
