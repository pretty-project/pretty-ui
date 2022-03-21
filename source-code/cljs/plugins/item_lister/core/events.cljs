
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.events
    (:require [mid-fruits.candy                    :refer [return]]
              [mid-fruits.map                      :refer [dissoc-in]]
              [plugins.item-lister.core.subs       :as core.subs]
              [plugins.item-lister.download.events :as download.events]
              [plugins.item-lister.items.events    :as items.events]
              [plugins.item-lister.mount.subs      :as mount.subs]
              [x.app-core.api                      :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-search-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (update-in db [:plugins :item-lister/meta-items extension-id :search-mode?] not))

(defn toggle-select-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (as-> db % (update-in % [:plugins :item-lister/meta-items extension-id :select-mode?] not)
             (dissoc-in % [:plugins :item-lister/meta-items extension-id :selected-items])))

(defn toggle-reorder-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (update-in db [:plugins :item-lister/meta-items extension-id :reorder-mode?] not))

(defn toggle-reload-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (update-in db [:plugins :item-lister/meta-items extension-id :reload-mode?] not))

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (assoc-in db [:plugins :item-lister/meta-items extension-id :error-mode?] true))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; Az item-lister plugin ...
  ; ... az első betöltődésekor letölti az elemeket az alapbeállításokkal.
  ; ... a további betöltődésekkor letölti az elemeket a legutóbb használt keresési
  ;     és rendezési beállításokkal, így a felhasználó az egyes elemek megtekintése/szerkesztése/...
  ;     után visszatérhet a lista legutóbbi állapotához.
  (as-> db % (dissoc-in % [:plugins :item-lister/meta-items extension-id])
             (assoc-in  % [:plugins :item-lister/meta-items extension-id]
                          (select-keys (get-in db [:plugins :item-lister/meta-items extension-id])
                                       [:order-by :search-term]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-default-order-by!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; - Az item-lister plugin betöltésekor ha az {:order-by ...} beállítás nem elérhető, akkor beállítja
  ;   az order-by-options vektor első elemét order-by beállításként.
  ; - Ha még nem volt a felhasználó által kiválasztva order-by érték, akkor ...
  ; ... a sort-items-select kirenderelésekor nem lenne a select-options felsorolásban aktív listaelem!
  ; ... az elemek letöltésekor a szerver nem kapná meg az order-by értékét!
  (if-let [order-by (r core.subs/get-meta-item db extension-id item-namespace :order-by)]
          (return db)
          (let [order-by-options (r mount.subs/get-body-prop db extension-id item-namespace :order-by-options)]
               (assoc-in db [:plugins :item-lister/meta-items extension-id :order-by] (first order-by-options)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-filter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) filter-pattern
  ;
  ; @return (map)
  [db [_ extension-id item-namespace filter-pattern]]
  (as-> db % (r download.events/reset-downloads! % extension-id item-namespace)
             (r items.events/reset-selections!   % extension-id item-namespace)
             (assoc-in % [:plugins :item-lister/meta-items extension-id :active-filter] filter-pattern)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/toggle-reorder-mode! toggle-reorder-mode!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/toggle-search-mode! toggle-search-mode!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/toggle-select-mode! toggle-select-mode!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/set-error-mode! set-error-mode!)
