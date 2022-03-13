
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.download.events
    (:require [mid-fruits.map                    :refer [dissoc-in]]
              [mid-fruits.vector                 :as vector]
              [plugins.item-lister.download.subs :as download.subs]
              [plugins.item-lister.items.events  :as items.events]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-db.api                      :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (let [resolver-id (r download.subs/get-resolver-id db extension-id item-namespace :get)
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
  (let [resolver-id    (r download.subs/get-resolver-id db extension-id item-namespace :get)
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
  (let [resolver-id (r download.subs/get-resolver-id db extension-id item-namespace :get)
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
             (r items.events/enable-all-items! % extension-id item-namespace)
             (r items.events/reset-selections! % extension-id item-namespace)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/receive-reloaded-items! receive-reloaded-items!)
