
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v1.3.8
; Compatibility: x4.6.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.events
    (:require [mid-fruits.map    :as map :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-ui.api      :as ui]
              [app-plugins.item-lister.engine :as engine]
              [app-plugins.item-lister.subs   :as subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-search-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (update-in db [extension-id :item-lister/meta-items :search-mode?] not))

(defn toggle-select-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (as-> db % (update-in % [extension-id :item-lister/meta-items :select-mode?] not)
             (dissoc-in % [extension-id :item-lister/meta-items :selected-items])))

(defn toggle-reorder-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (update-in db [extension-id :item-lister/meta-items :reorder-mode?] not))

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (assoc-in db [extension-id :item-lister/meta-items :error-mode?] true))

(defn reset-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (-> db (dissoc-in [extension-id :item-lister/meta-items :error-mode?])
         (dissoc-in [extension-id :item-lister/meta-items :select-mode?])
         (dissoc-in [extension-id :item-lister/meta-items :selected-items])))

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (-> db (dissoc-in [extension-id :item-lister/data-items])
         (dissoc-in [extension-id :item-lister/meta-items :document-count])
         (dissoc-in [extension-id :item-lister/meta-items :received-count])
         (dissoc-in [extension-id :item-lister/meta-items :items-received?])))

(defn reset-search!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (dissoc-in db [extension-id :item-lister/meta-items :search-term]))

(defn reset-selections!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (dissoc-in db [extension-id :item-lister/meta-items :selected-items]))

(defn set-default-order-by!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; Az item-lister plugin betöltésekor beállítja az order-by-options vektor első elemét
  ; order-by beállításként, mert ha még nem volt a felhasználó által kiválasztva order-by
  ; érték, akkor ...
  ; ... a sort-items-select kirenderelésekor nem lenne a select-options felsorolásban aktív listaelem!
  ; ... az elemek letöltésekor a szerver nem kapná meg az order-by értékét!
  (let [order-by-options (r subs/get-meta-item db extension-id item-namespace :order-by-options)]
       (assoc-in db [extension-id :item-lister/meta-items :order-by] (first order-by-options))))

(defn toggle-reload-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (update-in db [extension-id :item-lister/meta-items :reload-mode?] not))

(defn select-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [downloaded-items (r subs/get-downloaded-items db extension-id item-namespace)
        item-selections  (vector/dex-range downloaded-items)]
       (assoc-in db [extension-id :item-lister/meta-items :selected-items] item-selections)))

(defn unselect-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (dissoc-in db [extension-id :item-lister/meta-items :selected-items]))

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
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
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

(defn backup-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [selected-item-dexes (r subs/get-selected-item-dexes db extension-id item-namespace)]
       (letfn [(f [db item-dex]
                  (let [item-id (get-in db [extension-id :item-lister/data-items item-dex :id])
                        item    (get-in db [extension-id :item-lister/data-items item-dex])]
                       (assoc-in db [extension-id :item-lister/backup-items item-id] item)))]
              (reduce f db selected-item-dexes))))

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

(defn delete-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (as-> db % (r backup-selected-items!  % extension-id item-namespace)
             (r disable-selected-items! % extension-id item-namespace)
             (r ui/fake-random-process! %)))

(defn use-filter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) filter-pattern
  ;
  ; @return (map)
  [db [_ extension-id item-namespace filter-pattern]]
  (as-> db % (r reset-downloads!  % extension-id item-namespace)
             (r reset-selections! % extension-id item-namespace)
             (assoc-in % [extension-id :item-lister/meta-items :active-filter] filter-pattern)))

(defn items-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; XXX#0499
  ; Szükséges eltárolni, hogy megtörtént-e az első kommunikáció a szerverrel!
  (assoc-in db [extension-id :item-lister/meta-items :items-received?] true))

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
       (update-in db [extension-id :item-lister/data-items] vector/concat-items documents)))

(defn store-received-document-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (let [resolver-id    (engine/resolver-id extension-id item-namespace :get)
        document-count (get-in server-response [resolver-id :document-count])
        documents      (get-in server-response [resolver-id :documents])
        received-count (count documents)]
      (-> db ; A document-count értéke NEM a fogadott dokumentumok mennyiségére utal, hanem
             ; hány dokumentum van a szerveren, ami megfelel a letöltési feltételeknek.
             (assoc-in [extension-id :item-lister/meta-items :document-count] document-count)
             ; BUG#7009
             ; Ha a legutoljára letöltött dokumentumok száma 0, de a letöltött dokumentumok
             ; száma kevesebb, mint a szerverről érkezett document-count érték, akkor
             ; az elemek letöltésével kapcsolatban valamilyen hiba történt.
             ; Az ilyen típusú hibák megállapításához szükséges a received-count
             ; értéket eltárolni, ami a fogadott dokumentumok mennyiségére utal.
             (assoc-in [extension-id :item-lister/meta-items :received-count] received-count))))

(defn receive-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (as-> db % (r store-received-items!          % extension-id item-namespace server-response)
             (r store-received-document-count! % extension-id item-namespace server-response)
             (r items-received                 % extension-id item-namespace)))

(defn store-reloaded-items!
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
        documents (db/collection->non-namespaced-collection documents)]
       (assoc-in db [extension-id :item-lister/data-items] documents)))

(defn receive-reloaded-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (as-> db % (r store-reloaded-items!          % extension-id item-namespace server-response)
             (r store-received-document-count! % extension-id item-namespace server-response)
             ; A listaelemeken végzett műveletek közben esetlegesen egyes listaelemek
             ; {:disabled? true} állapotba lépnek (pl. törlés), amíg a művelet befejeződik.
             ; A művelet akkor tekinthető befejezettnek, amikor a lista állapota frissült
             ; a kliens-oldalon, ezért a receive-reloaded-items! függvény szünteti meg a listaelemek
             ; {:disabled? true} állapotát!
             (r enable-all-items! % extension-id item-namespace)
             (r reset-selections! % extension-id item-namespace)))

(defn load-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; Az item-lister plugin ...
  ; ... az első betöltődésekor letölti az elemeket az alapbeállításokkal.
  ; ... a további betöltődésekkor letölti az elemeket a legutóbb használt beállításokkal.
  (as-> db % (r reset-lister!         % extension-id item-namespace)
             (r reset-downloads!      % extension-id item-namespace)
             (r reset-search!         % extension-id item-namespace)
             (r set-default-order-by! % extension-id item-namespace)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/clean-backup-items! clean-backup-items!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/receive-reloaded-items! receive-reloaded-items!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/select-all-items! select-all-items!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/unselect-all-items! unselect-all-items!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/set-error-mode! set-error-mode!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/toggle-item-selection! toggle-item-selection!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/toggle-reorder-mode! toggle-reorder-mode!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/toggle-search-mode! toggle-search-mode!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/toggle-select-mode! toggle-select-mode!)
