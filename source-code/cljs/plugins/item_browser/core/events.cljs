
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.events
    (:require [mid-fruits.candy                     :refer [return]]
              [mid-fruits.map                       :refer [dissoc-in]]
              [plugins.item-browser.core.subs       :as core.subs]
              [plugins.item-browser.download.events :as download.events]
              [plugins.item-browser.mount.subs      :as mount.subs]
              [plugins.item-browser.routes.events   :as routes.events]
              [plugins.item-browser.transfer.subs   :as transfer.subs]
              [plugins.item-lister.core.events      :as plugins.item-lister.core.events]
              [plugins.plugin-handler.core.events   :as core.events]
              [x.app-core.api                       :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.core.events
(def set-error-mode! plugins.item-lister.core.events/set-error-mode!)
(def use-filter!     plugins.item-lister.core.events/use-filter!)

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

(defn store-derived-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  ; A) Az aktuális útvonalból származtatott :item-id útvonal-paraméter értékét eltárolja az aktuálisan
  ;    böngészett elem azonosítójaként, ...
  ;    ... ha az aktuális útvonal tartalmazza az :item-id paramétert (az útvonal nem a base-route).
  ;
  ; B) A body komponens {:root-item-id "..."} paraméterének értékét eltárolja az aktuálisan böngészett
  ;    elem azonosítójaként, ...
  ;    ... ha az aktuális útvonal NEM tartalmazza az :item-id útvonal-paramétért.
  ;    ... a body komponens a React-fába van csatolva és megkapta a {:root-item-id "..."} paramétert.
  ;    Pl.: Ha az item-browser böngésző használata közben a felhasználó visszatér a base-route útvonalra,
  ;         ami NEM tartalmazza az :item-id útvonal-paramétert (pl. a böngésző "Vissza" gombjának használatával).
  ;         Ilyenkor az útvonalból nem származtatható az :item-id útvonal-paraméter és a React-fába
  ;         csatolt body komponens :component-did-mount életciklusa sem fog megtörténni, ami felhasználná
  ;         a {:root-item-id "..."} paramétert.
  (if-let [derived-item-id (r core.subs/get-derived-item-id db browser-id)]
          ; A)
          (r set-current-item-id! db browser-id derived-item-id)
          (if-let [root-item-id (r mount.subs/get-body-prop db browser-id :root-item-id)]
                  ; B)
                  (r set-current-item-id! db browser-id root-item-id)
                  (return db))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) browser-props
  ;
  ; @return (map)
  [db [_ browser-id browser-props]]
  (as-> db % (r store-derived-item-id!           % browser-id)
             (r download.events/reset-downloads! % browser-id)
             (r routes.events/set-parent-route!  % browser-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-browser/set-error-mode! set-error-mode!)
