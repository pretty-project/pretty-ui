
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.download.events
    (:require [mid-fruits.vector                      :as vector]
              [plugins.item-lister.body.subs          :as body.subs]
              [plugins.item-lister.core.events        :as core.events]
              [plugins.item-lister.download.subs      :as download.subs]
              [plugins.item-lister.items.events       :as items.events]
              [plugins.item-lister.items.subs         :as items.subs]
              [plugins.plugin-handler.download.events :as download.events]
              [x.app-core.api                         :as a :refer [r]]
              [x.app-db.api                           :as db]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.download.events
(def data-received download.events/data-received)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-received-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ lister-id server-response]]
  ; XXX#3907
  ; Az item-lister plugin a dokumentumokat névtér nélkül tárolja, így a lista-elemek
  ; felsorolásakor és egyes Re-Frame feliratkozásokban a dokumentumok egyes értékeinek
  ; olvasása kevesebb erőforrást igényel, mivel nem szükséges az értékek kulcsaihoz
  ; az aktuális névteret hozzáfűzni/eltávolítani.
  (let [resolver-id (r download.subs/get-resolver-id db lister-id :get-items)
        items-path  (r body.subs/get-body-prop       db lister-id :items-path)
        documents   (get-in server-response [resolver-id :documents])
        documents   (db/collection->non-namespaced-collection documents)]
       (update-in db items-path vector/concat-items documents)))

(defn store-received-document-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ lister-id server-response]]
  ; - A document-count értéke NEM a fogadott dokumentumok mennyiségére utal, hanem
  ;   arra, hogy hány dokumentum van a szerveren, ami megfelel a letöltési feltételeknek.
  ;
  ; - BUG#7009
  ;   Ha a legutoljára letöltött dokumentumok száma 0, de a letöltött dokumentumok
  ;   száma kevesebb, mint a szerverről érkezett document-count érték, akkor
  ;   az elemek letöltésével kapcsolatban valamilyen hiba történt.
  ;   Az ilyen típusú hibák megállapításához szükséges a received-count
  ;   értéket eltárolni, ami a fogadott dokumentumok mennyiségére utal.
  (let [resolver-id    (r download.subs/get-resolver-id db lister-id :get-items)
        document-count (get-in server-response [resolver-id :document-count])
        documents      (get-in server-response [resolver-id :documents])
        received-count (count documents)]
      (-> db (assoc-in [:plugins :plugin-handler/meta-items lister-id :document-count] document-count)
             (assoc-in [:plugins :plugin-handler/meta-items lister-id :received-count] received-count))))

(defn select-received-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ lister-id server-response]]
  ; ...
  (if-let [selected-item-ids (r body.subs/get-body-prop db lister-id :selected-items)]
          (let [selected-item-dexes (r items.subs/get-item-dexes db lister-id selected-item-ids)]
               db)
          db))

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
             (r select-received-items!         % lister-id)
             (r data-received                  % lister-id)

             ; TEMP#4681
             ; A plugin kilép a {:data-received? true} állapotból, amikor a listaelemek törlődnek
             ; (pl. kereséskor, stb.)
             ; Egyes komponensek ezért nem iratkozhatnak fel a {:data-received? ...} tulajdonság
             ; értékére, hogy eldöntsék mikor jelenjenek meg, mert kereséskor vagy az elemek újratöltésekor
             ; eltűnnének az letöltés befejeződéséig.
             (assoc-in % [:plugins :plugin-handler/meta-items lister-id :first-data-received?] true)))

(defn store-reloaded-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ lister-id server-response]]
  ; XXX#3907
  (let [resolver-id (r download.subs/get-resolver-id db lister-id :get-items)
        items-path  (r body.subs/get-body-prop       db lister-id :items-path)
        documents   (get-in server-response [resolver-id :documents])
        documents   (db/collection->non-namespaced-collection documents)]
       (assoc-in db items-path documents)))

(defn receive-reloaded-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ lister-id server-response]]
  ; - A listaelemeken végzett műveletek közben esetlegesen egyes listaelemek
  ;   {:disabled? true} állapotba lépnek (pl. törlés), amíg a művelet befejeződik.
  ;   A művelet akkor tekinthető befejezettnek, amikor a lista állapota frissült
  ;   a kliens-oldalon, ezért a receive-reloaded-items! függvényben alkalmazott
  ;   items.events/enable-all-items! függvény szünteti meg a listaelemek {:disabled? true} állapotát!
  ;
  ; - A kijelölt elemeken végzett műveletek sikeres befejezése után a listaelemek újratöltődnek.
  ;
  ; - Az újra letöltött elemek fogadásakor is szükséges {:data-received? true} állapotba léptetni
  ;   a plugint az data-received függvény alkalmazásával!
  ;   Pl. Lassú internetkapcsolat mellett, ha a felhasználó duplikálja a kiválasztott elemeket
  ;        és a folyamat közben elhagyja a plugint, majd ismét megnyitja azt, akkor az újból megnyitott
  ;        plugin nem kezdi el letölteni az elemeket, mivel az elemek duplikálása vagy az azt követően
  ;        indított elemek újratöltése még folyamatban van.
  ;        A plugin nem indítja el az elemek letöltését, amíg bármelyik lekérés folyamatban van így
  ;        előfordulhat, hogy a megnyitás után a plugin nem az [:item-lister/request-items! ...]
  ;        esemény által indított lekéréssel tölti le az első elemeket, hanem a sikeres duplikálás
  ;        követetkezményeként megtörténő [:item-lister/reload-items! ...] esemény által indított
  ;        lekérés tölti le megnyitás után az első elemeket.
  ;        XXX#5476 HA A KÜLÖNBÖZŐ LEKÉRÉSEK EGYMÁSTÓL ELTÉRŐ AZONOSÍTÓT KAPNÁNAK, EZT A VISELKEDÉST
  ;                 SZÜKSÉGES LESZ FELÜLVIZSGÁLNI!
  (as-> db % (r store-reloaded-items!          % lister-id server-response)
             (r store-received-document-count! % lister-id server-response)
             (r items.events/enable-all-items! % lister-id)
             (r data-received                  % lister-id)

             ; TEMP#4681
             (assoc-in % [:plugins :plugin-handler/meta-items lister-id :first-data-received?] true)))
