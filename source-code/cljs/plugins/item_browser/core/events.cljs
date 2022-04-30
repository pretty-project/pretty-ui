
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.events
    (:require [mid-fruits.candy                   :refer [return]]
              [mid-fruits.map                     :refer [dissoc-in]]
              [plugins.item-browser.body.subs     :as body.subs]
              [plugins.item-browser.core.subs     :as core.subs]
              [plugins.item-browser.items.events  :as items.events]
              [plugins.item-lister.core.events    :as plugins.item-lister.core.events]
              [plugins.plugin-handler.core.events :as core.events]
              [x.app-core.api                     :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.core.events
(def set-error-mode!   plugins.item-lister.core.events/set-error-mode!)
(def quit-search-mode! plugins.item-lister.core.events/quit-search-mode!)
(def quit-select-mode! plugins.item-lister.core.events/quit-select-mode!)
(def reset-downloads!  plugins.item-lister.core.events/reset-downloads!)
(def filter-items!     plugins.item-lister.core.events/filter-items!)

; plugins.plugin-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-query-param!   core.events/set-query-param!)
(def set-item-id!       core.events/set-item-id!)
(def update-item-id!    core.events/update-item-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  ; Az aktuálisan böngészett elem azonosítójának eltárolásakor a plugin :query-params
  ; térképében is szükséges eltárolni az elem azonosítóját, mert az item-browser plugin az
  ; item-lister plugin letöltő funkciójával tölti le az elemeket, de az item-lister plugin
  ; nem használ :item-id paramétert az elemek letöltésekor ezért az item-browser plugin
  ; a :query-params használatával teszi bele az aktuálisan böngészett elem azonosíját
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
  ; - XXX#1329
  ;   Ha az aktuálisan böngészett elem megváltozásakor az item-browser plugin {:disabled? true} állapotban
  ;   tart egyes listaelemeket (pl. folyamatban lévő törlés miatt), akkor a load-browser! függvény feloldja
  ;   az összes listaelem {:disabled? true} állapotát, mert a listaelemek az indexük és nem pedig az azonosítójuk
  ;   alapján vannak {:disabled? true} állapotban, ezért ha megváltozik az aktuálisan böngészett elem,
  ;   akkor a letöltött listaelemek lecserélődése után az egyes indexekhez más listaelemek fognak tartozni.
  ;
  ; - BUG#1329
  ;   Ha a felhasználó egy folyamat közben elhagyja az aktuálisan böngészett elemet, akkor nem valószínű,
  ;   hogy vissza tud térni mielőtt a folyamat befejeződne, így elméletileg nem jelent problémát,
  ;   hogy az ismételten böngészett elemben a {:disabled? true} állapot nem kerül vissza az elhagyás előtt
  ;   {:disabled? true} állapotban lévő listaelemekre.
  (as-> db % (r reset-downloads!               % browser-id)
             (r reset-downloaded-item!         % browser-id)
             (r quit-search-mode!              % browser-id)
             (r quit-select-mode!              % browser-id)
             (r items.events/enable-all-items! % browser-id)
             (r update-item-id!                % browser-id)
             (r use-item-id!                   % browser-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-browser/set-error-mode! set-error-mode!)
