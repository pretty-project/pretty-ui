
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.core.events
    (:require [candy.api                          :refer [return]]
              [engines.engine-handler.core.events :as core.events]
              [engines.item-browser.body.subs     :as body.subs]
              [engines.item-browser.core.subs     :as core.subs]
              [engines.item-browser.items.events  :as items.events]
              [map.api                            :refer [dissoc-in]]
              [engines.item-lister.core.events    :as engines.item-lister.core.events]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-lister.core.events
(def set-meta-item!        engines.item-lister.core.events/set-meta-item!)
(def remove-meta-items!    engines.item-lister.core.events/remove-meta-items!)
(def set-engine-error!     engines.item-lister.core.events/set-engine-error!)
(def set-items!            engines.item-lister.core.events/set-items!)
(def reset-downloads!      engines.item-lister.core.events/reset-downloads!)
(def use-default-order-by! engines.item-lister.core.events/use-default-order-by!)
(def filter-items!         engines.item-lister.core.events/filter-items!)

; engines.engine-handler.core.events
(def set-query-param! core.events/set-query-param!)
(def set-item-id!     core.events/set-item-id!)
(def update-item-id!  core.events/update-item-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  ; Az aktuálisan böngészett elem azonosítójának eltárolásakor az engine query-params
  ; térképében is szükséges eltárolni az elem azonosítóját, mert az item-browser engine az
  ; item-lister engine letöltő funkciójával tölti le az elemeket, de az item-lister engine
  ; nem használ :item-id paramétert az elemek letöltésekor ezért az item-browser engine
  ; a query-params használatával teszi bele az aktuálisan böngészett elem azonosíját
  ; a Pathom lekéresekkel elküldött adatok közé.
  (let [current-item-id (r core.subs/get-current-item-id db browser-id)]
       (r set-query-param! db browser-id :item-id current-item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-downloaded-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  (let [item-path (r body.subs/get-body-prop db browser-id :item-path)]
       (dissoc-in db item-path)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  ; XXX#1329
  ; Ha az aktuálisan böngészett elem megváltozásakor az item-browser engine {:disabled? true} állapotban
  ; tart egyes listaelemeket (pl. folyamatban lévő törlés miatt), akkor a load-browser! függvény feloldja
  ; az összes listaelem {:disabled? true} állapotát, mert a listaelemek az indexük és nem pedig az azonosítójuk
  ; alapján vannak {:disabled? true} állapotban, ezért ha megváltozik az aktuálisan böngészett elem,
  ; akkor a letöltött listaelemek lecserélődése után az egyes indexekhez más listaelemek fognak tartozni.
  ;
  ; BUG#1329
  ; Ha a felhasználó egy folyamat közben elhagyja az aktuálisan böngészett elemet, akkor nem valószínű,
  ; hogy vissza tud térni mielőtt a folyamat befejeződne, így elméletileg nem jelent problémát,
  ; hogy az ismételten böngészett elemben a {:disabled? true} állapot nem kerül vissza az elhagyás előtt
  ; {:disabled? true} állapotban lévő listaelemekre.
  (as-> db % (r reset-downloads!               % browser-id)
             (r reset-downloaded-item!         % browser-id)
             (r items.events/enable-all-items! % browser-id)
             (r update-item-id!                % browser-id)
             (r use-item-id!                   % browser-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-browser/set-engine-error! set-engine-error!)
