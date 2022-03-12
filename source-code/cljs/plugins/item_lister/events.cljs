
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.events
    (:require [app-plugins.item-lister.engine :as engine]
              [app-plugins.item-lister.subs   :as subs]
              [mid-fruits.candy               :refer [param return]]
              [mid-fruits.map                 :as map :refer [dissoc-in]]
              [mid-fruits.vector              :as vector]
              [x.app-core.api                 :as a :refer [r]]
              [x.app-db.api                   :as db]
              [x.app-ui.api                   :as ui]))



;; ----------------------------------------------------------------------------
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

(defn toggle-reload-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (update-in db [extension-id :item-lister/meta-items :reload-mode?] not))

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (assoc-in db [extension-id :item-lister/meta-items :error-mode?] true))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [inherited-props (r subs/get-inherited-props db extension-id item-namespace)]
       (-> db (dissoc-in [extension-id :item-lister/meta-items])
              (assoc-in  [extension-id :item-lister/meta-items] inherited-props))))

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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-default-order-by!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; - Az item-lister plugin betöltésekor ha az {:order-by ...} beállítás nem elérhető, akkor beállítja
  ;   az order-by-options vektor első elemét order-by beállításként.
  ; - Ha még nem volt a felhasználó által kiválasztva order-by érték, akkor ...
  ; ... a sort-items-select kirenderelésekor nem lenne a select-options felsorolásban aktív listaelem!
  ; ... az elemek letöltésekor a szerver nem kapná meg az order-by értékét!
  (if-let [order-by (r subs/get-meta-item db extension-id item-namespace :order-by)]
          (return db)
          (let [order-by-options (r subs/get-meta-item db extension-id item-namespace :order-by-options)]
               (assoc-in db [extension-id :item-lister/meta-items :order-by] (first order-by-options)))))

(defn store-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace body-props]]
  (r db/apply-item! db [extension-id :item-lister/meta-items] merge body-props))

(defn init-header!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace header-props]]
  (r db/apply-item! db [extension-id :item-lister/meta-items] merge header-props))

(defn init-body!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace body-props]]
  (as-> db % (r store-body-props!     % extension-id item-namespace body-props)
             (r set-default-order-by! % extension-id item-namespace)))

(defn destruct-body!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; Az item-lister plugin elhagyásakor visszaállítja a plugin állapotát, így a következő betöltéskor
  ; az init-body! függvény lefutása előtt nem villan fel a legutóbbi állapot!
  (as-> db % (r reset-lister!    % extension-id item-namespace)
             (r reset-downloads! % extension-id item-namespace)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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

(defn toggle-item-selection!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r item-lister/toggle-item-selection! :my-extension :my-type 42)
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-dex]]
  (if (r subs/item-selected? db extension-id item-namespace item-dex)
      (-> db (assoc-in  [extension-id :item-lister/meta-items :select-mode?] true)
             (update-in [extension-id :item-lister/meta-items :selected-items] vector/remove-item item-dex))
      (-> db (assoc-in  [extension-id :item-lister/meta-items :select-mode?] true)
             (update-in [extension-id :item-lister/meta-items :selected-items] vector/conj-item   item-dex))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
             (r ui/fake-process!        % 15)))

(defn delete-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (as-> db % (r reset-selections! % extension-id item-namespace)
             (r enable-all-items! % extension-id item-namespace)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (let [resolver-id (r subs/get-resolver-id db extension-id item-namespace :get)
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
  (let [resolver-id    (r subs/get-resolver-id db extension-id item-namespace :get)
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
  (let [resolver-id (r subs/get-resolver-id db extension-id item-namespace :get)
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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/toggle-reorder-mode! toggle-reorder-mode!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/toggle-search-mode! toggle-search-mode!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/toggle-select-mode! toggle-select-mode!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/set-error-mode! set-error-mode!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/reset-selections! reset-selections!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/clean-backup-items! clean-backup-items!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/select-all-items! select-all-items!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/toggle-item-selection! toggle-item-selection!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/receive-reloaded-items! receive-reloaded-items!)
