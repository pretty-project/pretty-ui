
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.download.events
    (:require [engines.engine-handler.download.events :as download.events]
              [engines.item-lister.items.events       :as items.events]
              [re-frame.api                           :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.download.events
(def data-received              download.events/data-received)
(def store-received-item-count! download.events/store-received-item-count!)
(def store-received-items!      download.events/store-received-items!)
(def replace-item-order!        download.events/replace-item-order!)
(def update-item-order!         download.events/update-item-order!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
             (r update-item-order!         % lister-id server-response)
             (r data-received              % lister-id)

             ; TEMP#4681
             ; A {:first-data-received? true} állapotot szükséges megkülönböztetni
             ; a {:data-received? true} állapottól, mert bizonyos esetekben, amikor
             ; a listaelemek törlődnek (pl. kereséskor), az engine kilép
             ; a {:data-received? true} állapotból.
             (assoc-in % [:engines :engine-handler/meta-items lister-id :first-data-received?] true)))

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
  ;      és a folyamat közben elhagyja a engine-t, majd ismét megnyitja azt, akkor az újból megnyitott
  ;      engine nem kezdi el letölteni az elemeket, mivel az elemek duplikálása vagy az azt követően
  ;      indított elemek újratöltése még folyamatban van.
  ;      Az engine nem indítja el az elemek letöltését, amíg bármelyik lekérés folyamatban van így
  ;      előfordulhat, hogy a megnyitás után az engine nem az [:item-lister/request-items! ...]
  ;      esemény által indított lekéréssel tölti le az első elemeket, hanem a sikeres duplikálás
  ;      követetkezményeként megtörténő [:item-lister/reload-items! ...] esemény által indított
  ;      lekérés tölti le megnyitás után az első elemeket.
  ;      XXX#5476 (source-code/cljs/engines/engine_handler/core/subs.cljs)
  ;      HA A KÜLÖNBÖZŐ LEKÉRÉSEK EGYMÁSTÓL ELTÉRŐ AZONOSÍTÓT KAPNÁNAK, EZT A VISELKEDÉST
  ;      SZÜKSÉGES LESZ FELÜLVIZSGÁLNI!
  (as-> db % (r store-received-items!          % lister-id server-response)
             (r store-received-item-count!     % lister-id server-response)
             (r items.events/enable-all-items! % lister-id)
             (r data-received                  % lister-id)

             ; TEMP#4681
             (assoc-in % [:engines :engine-handler/meta-items lister-id :first-data-received?] true)))
