
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
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (update-in db [:plugins :plugin-handler/meta-items lister-id :search-mode?] not))

(defn toggle-select-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (as-> db % (update-in % [:plugins :plugin-handler/meta-items lister-id :select-mode?] not)
             (dissoc-in % [:plugins :plugin-handler/meta-items lister-id :selected-items])))

(defn toggle-reorder-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (update-in db [:plugins :plugin-handler/meta-items lister-id :reorder-mode?] not))

(defn toggle-reload-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (update-in db [:plugins :plugin-handler/meta-items lister-id :reload-mode?] not))

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items lister-id :error-mode?] true))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  ; Az item-lister plugin ...
  ; ... az első betöltődésekor letölti az elemeket az alapbeállításokkal.
  ; ... a további betöltődésekkor letölti az elemeket a legutóbb használt keresési
  ;     és rendezési beállításokkal, így a felhasználó az egyes elemek megtekintése/szerkesztése/...
  ;     után visszatérhet a lista legutóbbi állapotához.
  (as-> db % (dissoc-in % [:plugins :plugin-handler/meta-items lister-id])
             (assoc-in  % [:plugins :plugin-handler/meta-items lister-id]
                          (select-keys (get-in db [:plugins :plugin-handler/meta-items lister-id])
                                       [:order-by :search-term]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-default-order-by!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  ; - Az item-lister plugin betöltésekor ha az {:order-by ...} beállítás nem elérhető, akkor beállítja
  ;   az order-by-options vektor első elemét order-by beállításként.
  ; - Ha még nem volt a felhasználó által kiválasztva order-by érték, akkor ...
  ; ... a sort-items-select kirenderelésekor nem lenne a select-options felsorolásban aktív listaelem!
  ; ... az elemek letöltésekor a szerver nem kapná meg az order-by értékét!
  (if-let [order-by (r core.subs/get-meta-item db lister-id :order-by)]
          (return db)
          (let [order-by-options (r mount.subs/get-body-prop db lister-id :order-by-options)]
               (assoc-in db [:plugins :plugin-handler/meta-items lister-id :order-by] (first order-by-options)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-filter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) filter-pattern
  ;
  ; @return (map)
  [db [_ lister-id filter-pattern]]
  (as-> db % (r download.events/reset-downloads! % lister-id)
             (r items.events/reset-selections!   % lister-id)
             (assoc-in % [:plugins :plugin-handler/meta-items lister-id :active-filter] filter-pattern)))



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
