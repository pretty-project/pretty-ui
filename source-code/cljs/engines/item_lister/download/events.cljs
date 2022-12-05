
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.download.events
    (:require [candy.api                              :refer [return]]
              [engines.engine-handler.download.events :as download.events]
              [engines.item-lister.body.subs          :as body.subs]
              [engines.item-lister.core.events        :as core.events]
              [engines.item-lister.download.subs      :as download.subs]
              [engines.item-lister.items.events       :as items.events]
              [engines.item-lister.items.subs         :as items.subs]
              [engines.item-lister.selection.subs     :as selection.subs]
              [engines.item-lister.selection.events   :as selection.events]
              [map.api                                :as map]
              [re-frame.api                           :as r :refer [r]]
              [vector.api                             :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.download.events
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
  ; A többi engine-nel megegyezően az item-editor engine is névtér nélkül
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
      (-> db (assoc-in [:engines :engine-handler/meta-items lister-id :all-item-count]      all-item-count)
             (assoc-in [:engines :engine-handler/meta-items lister-id :received-item-count] received-item-count))))

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
             ; a listaelemek törlődnek (pl. kereséskor), az engine kilép
             ; a {:data-received? true} állapotból.
             (assoc-in % [:engines :engine-handler/meta-items lister-id :first-data-received?] true)))

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
  ; a engine-t az data-received függvény alkalmazásával!
  ; Pl.: Lassú internetkapcsolat mellett, ha a felhasználó duplikálja a kiválasztott elemeket
  ;     és a folyamat közben elhagyja a engine-t, majd ismét megnyitja azt, akkor az újból megnyitott
  ;     engine nem kezdi el letölteni az elemeket, mivel az elemek duplikálása vagy az azt követően
  ;     indított elemek újratöltése még folyamatban van.
  ;     Az engine nem indítja el az elemek letöltését, amíg bármelyik lekérés folyamatban van így
  ;     előfordulhat, hogy a megnyitás után az engine nem az [:item-lister/request-items! ...]
  ;     esemény által indított lekéréssel tölti le az első elemeket, hanem a sikeres duplikálás
  ;     követetkezményeként megtörténő [:item-lister/reload-items! ...] esemény által indított
  ;     lekérés tölti le megnyitás után az első elemeket.
  ;     XXX#5476 (source-code/cljs/engines/engine_handler/core/subs.cljs)
  ;     HA A KÜLÖNBÖZŐ LEKÉRÉSEK EGYMÁSTÓL ELTÉRŐ AZONOSÍTÓT KAPNÁNAK, EZT A VISELKEDÉST
  ;     SZÜKSÉGES LESZ FELÜLVIZSGÁLNI!
  (as-> db % (r store-reloaded-items!          % lister-id server-response)
             (r store-received-item-count!     % lister-id server-response)
             (r items.events/enable-all-items! % lister-id)
             (r data-received                  % lister-id)

             ; TEMP#4681
             (assoc-in % [:engines :engine-handler/meta-items lister-id :first-data-received?] true)))
