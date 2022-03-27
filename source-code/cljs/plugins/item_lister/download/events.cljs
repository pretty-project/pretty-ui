
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.download.events
    (:require [mid-fruits.map                    :refer [dissoc-in]]
              [mid-fruits.vector                 :as vector]
              [plugins.item-lister.download.subs :as download.subs]
              [plugins.item-lister.items.events  :as items.events]
              [plugins.item-lister.mount.subs    :as mount.subs]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-db.api                      :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (let [items-path (r mount.subs/get-body-prop db lister-id :items-path)]
       (-> db (dissoc-in items-path)
              (dissoc-in [:plugins :plugin-handler/meta-items lister-id :document-count])
              (dissoc-in [:plugins :plugin-handler/meta-items lister-id :received-count])
              (dissoc-in [:plugins :plugin-handler/meta-items lister-id :items-received?]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  ; XXX#0499
  ; Szükséges eltárolni, hogy megtörtént-e az első kommunikáció a szerverrel!
  (assoc-in db [:plugins :plugin-handler/meta-items lister-id :items-received?] true))

(defn store-received-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ lister-id server-response]]
  (let [resolver-id (r download.subs/get-resolver-id db lister-id :get-items)
        items-path  (r mount.subs/get-body-prop      db lister-id :items-path)
        documents   (get-in server-response [resolver-id :documents])
        ; XXX#3907
        ; Az item-lister a dokumentumokat névtér nélkül tárolja, így
        ; a lista-elemek felsorolásakor és egyes Re-Frame feliratkozásokban
        ; a dokumentumok egyes értékeinek olvasása kevesebb erőforrást igényel,
        ; ha nem szükséges az értékek kulcsaihoz az aktuális névteret hozzáfűzni.
        documents (db/collection->non-namespaced-collection documents)]
       (update-in db items-path vector/concat-items documents)))

(defn store-received-document-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ lister-id server-response]]
  (let [resolver-id    (r download.subs/get-resolver-id db lister-id :get-items)
        document-count (get-in server-response [resolver-id :document-count])
        documents      (get-in server-response [resolver-id :documents])
        received-count (count documents)]
      (-> db ; A document-count értéke NEM a fogadott dokumentumok mennyiségére utal, hanem
             ; hány dokumentum van a szerveren, ami megfelel a letöltési feltételeknek.
             (assoc-in [:plugins :plugin-handler/meta-items lister-id :document-count] document-count)
             ; BUG#7009
             ; Ha a legutoljára letöltött dokumentumok száma 0, de a letöltött dokumentumok
             ; száma kevesebb, mint a szerverről érkezett document-count érték, akkor
             ; az elemek letöltésével kapcsolatban valamilyen hiba történt.
             ; Az ilyen típusú hibák megállapításához szükséges a received-count
             ; értéket eltárolni, ami a fogadott dokumentumok mennyiségére utal.
             (assoc-in [:plugins :plugin-handler/meta-items lister-id :received-count] received-count))))

(defn receive-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ lister-id server-response]]
  (as-> db % (r store-received-items!          % lister-id server-response)
             (r store-received-document-count! % lister-id server-response)
             (r items-received                 % lister-id)))

(defn store-reloaded-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ lister-id server-response]]
  (let [resolver-id (r download.subs/get-resolver-id db lister-id :get-items)
        items-path  (r mount.subs/get-body-prop      db lister-id :items-path)
        documents   (get-in server-response [resolver-id :documents])
        ; XXX#3907
        documents (db/collection->non-namespaced-collection documents)]
       (assoc-in db items-path documents)))

(defn receive-reloaded-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ lister-id server-response]]
  (as-> db % (r store-reloaded-items!          % lister-id server-response)
             (r store-received-document-count! % lister-id server-response)
             ; A listaelemeken végzett műveletek közben esetlegesen egyes listaelemek
             ; {:disabled? true} állapotba lépnek (pl. törlés), amíg a művelet befejeződik.
             ; A művelet akkor tekinthető befejezettnek, amikor a lista állapota frissült
             ; a kliens-oldalon, ezért a receive-reloaded-items! függvény szünteti meg a listaelemek
             ; {:disabled? true} állapotát!
             (r items.events/enable-all-items! % lister-id)
             (r items.events/reset-selections! % lister-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/receive-reloaded-items! receive-reloaded-items!)
