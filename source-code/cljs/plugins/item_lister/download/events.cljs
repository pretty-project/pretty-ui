
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.download.events
    (:require [mid-fruits.candy                       :refer [return]]
              [mid-fruits.map                         :as map]
              [mid-fruits.vector                      :as vector]
              [plugins.item-lister.body.subs          :as body.subs]
              [plugins.item-lister.core.events        :as core.events]
              [plugins.item-lister.download.subs      :as download.subs]
              [plugins.item-lister.items.events       :as items.events]
              [plugins.item-lister.items.subs         :as items.subs]
              [plugins.item-lister.selection.subs     :as selection.subs]
              [plugins.item-lister.selection.events   :as selection.events]
              [plugins.plugin-handler.download.events :as download.events]
              [re-frame.api                           :as r :refer [r]]))



;; ----------------------------------------------------------------------------
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
  ; A többi pluginnal megegyezően az item-editor plugin is névtér nélkül
  ; tárolja a letöltött dokumentumokat.
  (let [resolver-answer (r download.subs/get-resolver-answer db lister-id :get-items server-response)
        items-path      (r body.subs/get-body-prop           db lister-id :items-path)
        received-items  (:items resolver-answer)]
       (update-in db items-path vector/concat-items (vector/->items received-items map/remove-namespace))))

(defn store-received-item-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ lister-id server-response]]
  ; Az all-item-count értéke NEM a fogadott dokumentumok mennyiségére utal, hanem
  ; arra, hogy hány dokumentum van a szerveren, ami megfelel a letöltési feltételeknek.
  ;
  ; BUG#7009
  ; Ha a legutoljára letöltött dokumentumok száma 0, de a letöltött dokumentumok
  ; száma kevesebb, mint a szerverről érkezett all-item-count érték, akkor
  ; az elemek letöltésével kapcsolatban valamilyen hiba történt.
  ; Az ilyen típusú hibák megállapításához szükséges a received-item-count
  ; értéket eltárolni, ami a fogadott dokumentumok mennyiségére utal.
  (let [resolver-answer     (r download.subs/get-resolver-answer db lister-id :get-items server-response)
        all-item-count      (:all-item-count resolver-answer)
        received-items      (:items          resolver-answer)
        received-item-count (count received-items)]
      (-> db (assoc-in [:plugins :plugin-handler/meta-items lister-id :all-item-count]      all-item-count)
             (assoc-in [:plugins :plugin-handler/meta-items lister-id :received-item-count] received-item-count))))

(defn receive-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ lister-id server-response]]
  (as-> db % (r store-received-items!      % lister-id server-response)
             (r store-received-item-count! % lister-id server-response)
             (r data-received              % lister-id)

             ; TEMP#4681
             ; A {:first-data-received? true} állapotot szükséges megkülönböztetni
             ; a {:data-received? true} állapottól, mert bizonyos esetekben, amikor
             ; a listaelemek törlődnek (pl. kereséskor), a plugin kilép
             ; a {:data-received? true} állapotból.
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
  (let [resolver-answer (r download.subs/get-resolver-answer db lister-id :get-items server-response)
        items-path      (r body.subs/get-body-prop           db lister-id :items-path)
        received-items  (:items resolver-answer)]
       (assoc-in db items-path (vector/->items received-items map/remove-namespace))))

(defn receive-reloaded-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ lister-id server-response]]
  ; A listaelemeken végzett műveletek közben esetlegesen egyes listaelemek
  ; {:disabled? true} állapotba lépnek (pl. törlés), amíg a művelet befejeződik.
  ; A művelet akkor tekinthető befejezettnek, amikor a lista állapota frissült
  ; a kliens-oldalon, ezért a receive-reloaded-items! függvényben alkalmazott
  ; items.events/enable-all-items! függvény szünteti meg a listaelemek {:disabled? true} állapotát!
  ;
  ; A kijelölt elemeken végzett műveletek sikeres befejezése után a listaelemek újratöltődnek.
  ;
  ; Az újra letöltött elemek fogadásakor is szükséges {:data-received? true} állapotba léptetni
  ; a plugint az data-received függvény alkalmazásával!
  ; Pl.: Lassú internetkapcsolat mellett, ha a felhasználó duplikálja a kiválasztott elemeket
  ;      és a folyamat közben elhagyja a plugint, majd ismét megnyitja azt, akkor az újból megnyitott
  ;      plugin nem kezdi el letölteni az elemeket, mivel az elemek duplikálása vagy az azt követően
  ;      indított elemek újratöltése még folyamatban van.
  ;      A plugin nem indítja el az elemek letöltését, amíg bármelyik lekérés folyamatban van így
  ;      előfordulhat, hogy a megnyitás után a plugin nem az [:item-lister/request-items! ...]
  ;      esemény által indított lekéréssel tölti le az első elemeket, hanem a sikeres duplikálás
  ;      követetkezményeként megtörténő [:item-lister/reload-items! ...] esemény által indított
  ;      lekérés tölti le megnyitás után az első elemeket.
  ;      XXX#5476 HA A KÜLÖNBÖZŐ LEKÉRÉSEK EGYMÁSTÓL ELTÉRŐ AZONOSÍTÓT KAPNÁNAK, EZT A VISELKEDÉST
  ;               SZÜKSÉGES LESZ FELÜLVIZSGÁLNI!
  (as-> db % (r store-reloaded-items!          % lister-id server-response)
             (r store-received-item-count!     % lister-id server-response)
             (r items.events/enable-all-items! % lister-id)
             (r data-received                  % lister-id)

             ; TEMP#4681
             (assoc-in % [:plugins :plugin-handler/meta-items lister-id :first-data-received?] true)))
