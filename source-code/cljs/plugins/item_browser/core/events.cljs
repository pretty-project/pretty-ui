
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.events
    (:require [mid-fruits.candy                   :refer [return]]
              [mid-fruits.map                     :refer [dissoc-in]]
              [plugins.item-browser.items.events  :as items.events]
              [plugins.item-browser.mount.subs    :as mount.subs]
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
(def use-filter!       plugins.item-lister.core.events/use-filter!)

; plugins.plugin-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-current-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  ; Az aktuálisan böngészett elem azonosítójának eltárolásakor az item-lister plugin :default-query-params
  ; térképében is szükséges eltárolni az elem azonosítóját, így az item-lister plugin által küldött
  ; Pathom lekérések tartalmazni fogják az {:item-id "..."} tulajdonságot, amiből a szerver-oldali mutation
  ; és resolver függvények hozzáférhetnek.
  (-> db (assoc-in [:plugins :plugin-handler/meta-items browser-id                       :item-id] item-id)
         (assoc-in [:plugins :plugin-handler/meta-items browser-id :default-query-params :item-id] item-id)))

(defn use-root-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  ; XXX#5006
  ;
  ; Ha az [:item-browser/body-did-mount ...] esemény megtörténtekor az aktuálisan böngészett
  ; elem azonosítója ...
  ;
  ; A) ... már eltárolásra került, akkor NEM használja a body komponens {:root-item-id "..."}
  ;        paraméterének értékét.
  ;
  ; B) ... még NEM került eltárolásra és a body komponens paraméterként megkapta a {:root-item-id "..."}
  ;        tulajdonságot, akkor azt eltárolja az aktuálisan böngészett elem azonosítójaként.
  (if-let [item-id (get-in db [:plugins :plugin-handler/meta-items browser-id :item-id])]
          ; A)
          (return db)
          (if-let [root-item-id (r mount.subs/get-body-prop db browser-id :root-item-id)]
                  ; B)
                  (r set-current-item-id! db browser-id root-item-id)
                  (return db))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browse-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  ; - XXX#1329
  ;   Ha az aktuálisan böngészett elem megváltozásakor az item-browser plugin {:disabled? true} állapotban
  ;   tart egyes listaelemeket (pl. folyamatban lévő törlés miatt), akkor a browse-item! függvény feloldja
  ;   az összes listaelem {:disabled? true} állapotát, mert a listaelemek az indexük és nem pedig az azonosítójuk
  ;   alapján vannak {:disabled? true} állapotban, ezért ha megváltozik az aktuálisan böngészett elem,
  ;   akkor a letöltött listaelemek lecserélődése után az egyes indexekhez más listaelemek fognak tartozni.
  ;
  ; - BUG#1329
  ;   Ha a felhasználó egy folyamat közben elhagyja az aktuálisan böngészett elemet, akkor nem valószínű,
  ;   hogy vissza tud térni mielőtt a folyamat befejeződne, így elméletileg nem jelent problémát,
  ;   hogy az ismételten böngészett elemben a {:disabled? true} állapot nem kerül vissza az elhagyás előtt
  ;   {:disabled? true} állapotban lévő listaelemekre.
  ;
  ; - XXX#0701
  ;   A handle-route! függvény a browse-item! függvény működését valósítja meg, amikor az item-browser
  ;   plugint egy útvonal megnyitása indítja el és az aktuálisan böngészett elem azonosítóját
  ;   az aktuális útvonalból kell származtatni a browse-item! függvénnyel ellentétben, ami paraméterként
  ;   kapja meg az aktuálisan böngészett elem azonosítóját.
  (as-> db % (r set-current-item-id!           % browser-id item-id)
             (r reset-downloads!               % browser-id)
             (r quit-search-mode!              % browser-id)
             (r quit-select-mode!              % browser-id)
             (r items.events/enable-all-items! % browser-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-browser/set-error-mode! set-error-mode!)
