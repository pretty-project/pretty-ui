
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.0
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.events
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [app-plugins.item-lister.engine :as engine]
              [app-plugins.item-lister.subs   :as subs]))



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
  ; Az item-lister betöltésekor felülírás nélkül aláfűzi a lister-props térképet a lister-meta
  ; térképnek, így a legutóbbi beállítások elérhetők maradnak.
  (update-in db [extension-id :lister-meta] map/reverse-merge lister-props))

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (-> db (dissoc-in [extension-id :lister-data])
         (dissoc-in [extension-id :lister-meta :document-count])
         (dissoc-in [extension-id :lister-meta :received-count])
         (dissoc-in [extension-id :lister-meta :synchronized?])))

(defn reset-search!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (dissoc-in db [extension-id :lister-meta :search-term]))

(defn reset-selections!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (dissoc-in db [extension-id :lister-meta :selected-items]))

(defn quit-filter-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; WARNING#4569
  ; A {:filter-mode? true} az {:actions-mode? true} beállítással párhuzamosan fut, ezért
  ; az {:actions-mode? true} módból bármelyik másik módba átlépéskor szükséges a {:filter-mode? true}
  ; beállításból kilépni!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (-> db (dissoc-in [extension-id :lister-meta :filter-mode?])
         (dissoc-in [extension-id :lister-meta :filter])))

(defn toggle-filter-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (-> db (update-in [extension-id :lister-meta :filter-mode?] not)
         (dissoc-in [extension-id :lister-meta :filter])))

(a/reg-event-db :item-lister/toggle-filter-mode! toggle-filter-mode!)

(defn toggle-search-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (as-> db % (update-in % [extension-id :lister-meta :search-mode?] not)
             (r quit-filter-mode! % extension-id)))

(a/reg-event-db :item-lister/toggle-search-mode! toggle-search-mode!)

(defn toggle-select-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (as-> db % (update-in % [extension-id :lister-meta :select-mode?] not)
             (dissoc-in % [extension-id :lister-meta :selected-items])))

(a/reg-event-db :item-lister/toggle-select-mode! toggle-select-mode!)

(defn toggle-reorder-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (as-> db % (update-in % [extension-id :lister-meta :reorder-mode?] not)
             (r quit-filter-mode! % extension-id)))

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
       (assoc-in db [extension-id :lister-meta :selected-items] item-selections)))

(a/reg-event-db :item-lister/select-all-items! select-all-items!)

(defn unselect-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (dissoc-in db [extension-id :lister-meta :selected-items]))

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
      (update-in db [extension-id :lister-meta :selected-items] vector/remove-item item-dex)
      (update-in db [extension-id :lister-meta :selected-items] vector/conj-item   item-dex)))

(a/reg-event-db :item-lister/toggle-item-selection! toggle-item-selection!)

(defn mark-items-as-disabled!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ;  (r events/mark-items-as-disabled! db :my-extension [0 1 4])
  ;
  ; @return (map)
  [db [_ extension-id _ item-dexes]]
  (update-in db [extension-id :lister-meta :disabled-items]
             vector/concat-items item-dexes))

(defn unmark-items-as-disabled!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ;  (r events/unmark-items-as-disabled! db :my-extension [0 1 4])
  ;
  ; @return (map)
  [db [_ extension-id _ item-dexes]]
  (update-in db [extension-id :lister-meta :disabled-items]
             vector/remove-items item-dexes))

(defn disable-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [selected-items (r subs/get-selected-items db extension-id item-namespace)]
       (r mark-items-as-disabled! db extension-id item-namespace selected-items)))

(defn use-filter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) filter-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace filter-id]]
  (assoc-in db [extension-id :lister-meta :filter] filter-id))

(defn discard-filter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (dissoc-in db [extension-id :lister-meta :filter]))

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
       (as-> db % (r db/store-collection! % [extension-id :lister-data] documents {:remove-namespace? true})
                  ; A névtér nélkül tárolt dokumentumokon végzett műveletkhez egyes külső
                  ; moduloknak szüksége lehet a dokumentumok névterének ismeretére!
                  (assoc-in % [extension-id :lister-meta :item-namespace] item-namespace)
                  ; Szükséges frissíteni a keresési feltételeknek megfelelő
                  ; dokumentumok számát, mert megváltozhat az értéke!
                  (assoc-in % [extension-id :lister-meta :document-count] document-count)
                  ; Szükséges eltárolni, hogy megtörtént-e az első kommunikáció a szerverrel!
                  (assoc-in % [extension-id :lister-meta :synchronized?] true)
                  ; BUG#7009
                  ; Ha a legutoljára letöltött dokumentumok száma 0, de a letöltött dokumentumok
                  ; száma kevesebb, mint a szerverről érkezett document-count érték, akkor
                  ; az elemek letöltésével kapcsolatban valamilyen hiba történt.
                  ; Az ilyen típusú hibák megállapításához szükséges a received-count
                  ; értéket eltárolni.
                  (assoc-in % [extension-id :lister-meta :received-count] received-count))))

(defn load-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace lister-props]]
  (as-> db % (r reset-downloads!    % extension-id)
             (r reset-search!       % extension-id)
             (r store-lister-props! % extension-id item-namespace lister-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/duplicate-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]))

(a/reg-event-fx
  :item-lister/delete-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r disable-selected-items! db extension-id item-namespace)}))
      
      ; TODO
      ; Ameddig a kommunikáció történik a szerverrel, addig az egész lista legyen disabled állapotban
      ; és az új elemek letöltése szüneteljen addig? (item-lister pause function!)


      ; ARchivált elemet csak archiveált elemek lsitájába ehlyezz vissza és vica versa

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
  :item-lister/discard-filter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (as-> db % (r reset-downloads!  % extension-id)
                      (r reset-selections! % extension-id)
                      (r discard-filter!   % extension-id))
       :dispatch [:tools/reload-infinite-loader! extension-id]}))

(a/reg-event-fx
  :item-lister/use-filter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) filter-id
  (fn [{:keys [db]} [_ extension-id item-namespace filter-id]]
      {:db (as-> db % (r reset-downloads!  % extension-id)
                      (r reset-selections! % extension-id)
                      (r use-filter!       % extension-id item-namespace filter-id))
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
       ; hogy az a viewport területén van-e még.
       :dispatch [:tools/reload-infinite-loader! extension-id]}))

(a/reg-event-fx
  :item-lister/request-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; A letöltött dokumentumok on-success helyett on-stalled időpontban kerülnek tárolásra
  ; a Re-Frame adatbázisba, így elkerülhető, hogy a request idle-timeout ideje alatt
  ; az újonnan letöltött dokumentumok már kirenderelésre kerüljenek, amíg a letöltést jelző
  ; felirat még megjelenik a lista végén.
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      ; Ha az infinite-loader komponens ismételten megjelenik a viewport területén, csak abban
      ; az esetben próbáljon újabb elemeket letölteni, ha még nincs az összes letöltve.
      (if (r subs/download-more-items? db extension-id item-namespace)
          (let [resolver-id    (engine/resolver-id           extension-id item-namespace)
                resolver-props (r subs/get-resolver-props db extension-id item-namespace)]
               [:sync/send-query! (engine/request-id extension-id item-namespace)
                                  {:on-stalled [:item-lister/receive-items! extension-id item-namespace]
                                   :query      [`(~resolver-id ~resolver-props)]}]))))

(a/reg-event-fx
  :item-lister/load-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;  {:download-limit (integer)
  ;   :handle-archived-items? (boolean)
  ;   :handle-favorite-items? (boolean)}
  (fn [{:keys [db]} [_ extension-id item-namespace lister-props]]
      {:db (r load-lister! db extension-id item-namespace lister-props)
       :dispatch-n [[:ui/listen-to-process! (engine/request-id extension-id item-namespace)]
                    [:ui/set-header-title!  (param             extension-id)]
                    [:ui/set-window-title!  (param             extension-id)]
                    (engine/render-event extension-id item-namespace)]}))
