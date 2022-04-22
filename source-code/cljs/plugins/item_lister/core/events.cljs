
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.events
    (:require [mid-fruits.candy                   :refer [return]]
              [mid-fruits.map                     :refer [dissoc-in]]
              [plugins.item-lister.body.subs      :as body.subs]
              [plugins.item-lister.core.subs      :as core.subs]
              [plugins.plugin-handler.core.events :as core.events]
              [x.app-core.api                     :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-search-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items lister-id :search-mode?] true))

(defn quit-search-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (dissoc-in db [:plugins :plugin-handler/meta-items lister-id :search-mode?]))

(defn set-select-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items lister-id :select-mode?] true))

(defn quit-select-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (as-> db % (dissoc-in % [:plugins :plugin-handler/meta-items lister-id :select-mode?])
             (dissoc-in % [:plugins :plugin-handler/meta-items lister-id :selected-items])))

(defn set-actions-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  ; Az {:actions-mode? true} állapot beállításakor az item-lister plugin {:select-mode? true}
  ; állapotba is lép, mert a listaelemeken végezhető műveletek használatához szükséges
  ; a listaelemeket kijelölni.
  (as-> db % (assoc-in % [:plugins :plugin-handler/meta-items lister-id :actions-mode?] true)
             (r set-select-mode! % lister-id)))

(defn quit-actions-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (as-> db % (dissoc-in % [:plugins :plugin-handler/meta-items lister-id :actions-mode?])
             (r quit-select-mode! % lister-id)))

(defn set-reload-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items lister-id :reload-mode?] true))

(defn set-reorder-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items lister-id :reorder-mode?] true))

(defn quit-reorder-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (dissoc-in db [:plugins :plugin-handler/meta-items lister-id :reorder-mode?]))

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items lister-id :error-mode?] true))

(defn set-memory-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  ; Ha az item-lister plugin elhagyása előtt a set-memory-mode! függvény alkalmazásával
  ; a {:memory-mode? true} állapot beállításra kerül, akkor az item-lister plugin legközelebbi
  ; megnyitásakor a listaelemek a legutóbbi keresési és rendezési beállítások szerint töltődnek majd le.
  (assoc-in db [:plugins :plugin-handler/meta-items lister-id :memory-mode?] true))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  ; Ha az item-lister plugin elhagyása előtt, a {:memory-mode? true} állapot ...
  ; ... beállításra került, akkor megjegyzi a legutóbb használt keresési és rendezési beállításokat,
  ;     így a plugin újbóli megnyitásakor, a listaelemek a legutolsó állapot szerint töltődnek majd le,
  ;     így a felhasználó az egyes elemek megtekintése/szerkesztése/... után visszatérhet
  ;     a lista legutóbbi állapotához.
  ; ... NEM került beállításra, akkor a plugin újbóli megnyitásakor a listaelemek az alapértelmezett
  ;     beállítások szerint töltődnek majd le.
  (if-let [memory-mode? (r core.subs/get-meta-item db lister-id :memory-mode?)]
          (let [meta-items (get-in db [:plugins :plugin-handler/meta-items lister-id])]
               (as-> db % (dissoc-in % [:plugins :plugin-handler/meta-items lister-id])
                          (assoc-in  % [:plugins :plugin-handler/meta-items lister-id]
                                       (select-keys meta-items [:order-by :search-term]))))
          (dissoc-in db [:plugins :plugin-handler/meta-items lister-id])))

(defn reset-selections!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (dissoc-in db [:plugins :plugin-handler/meta-items lister-id :selected-items]))

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (let [items-path (r body.subs/get-body-prop db lister-id :items-path)]
       (-> db (dissoc-in items-path)
              (dissoc-in [:plugins :plugin-handler/meta-items lister-id :document-count])
              (dissoc-in [:plugins :plugin-handler/meta-items lister-id :received-count])
              (dissoc-in [:plugins :plugin-handler/meta-items lister-id :items-received?]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-default-order-by!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  ; - Ha az item-lister plugin betöltésekor az {:order-by ...} beállítás nem elérhető,
  ;   akkor a body-did-mount függvény által alkalmazott set-default-order-by! függvény beállítja
  ;   az order-by-options vektor első elemét aktuális order-by beállításként.
  ;
  ; - Ha az item-lister plugin betöltésekor nem lenne beállítva az {:order-by ...} tulajdonság, akkor ...
  ; ... a sort-items-select kirenderelésekor nem lenne a select-options felsorolásban aktív listaelem!
  ; ... a listaelemek letöltésekor a szerver nem kapná meg az {:order-by ...} tulajdonság értékét!
  (if-let [order-by (r core.subs/get-meta-item db lister-id :order-by)]
          (return db)
          (let [order-by-options (r body.subs/get-body-prop db lister-id :order-by-options)]
               (assoc-in db [:plugins :plugin-handler/meta-items lister-id :order-by] (first order-by-options)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) filter-pattern
  ;
  ; @return (map)
  [db [_ lister-id filter-pattern]]
  (as-> db % (r reset-downloads!  % lister-id)
             (r reset-selections! % lister-id)
             (assoc-in % [:plugins :plugin-handler/meta-items lister-id :active-filter] filter-pattern)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:item-lister/set-search-mode! :my-lister]
(a/reg-event-db :item-lister/set-search-mode! set-search-mode!)

; @usage
;  [:item-lister/quit-search-mode! :my-lister]
(a/reg-event-db :item-lister/quit-search-mode! quit-search-mode!)

; @usage
;  [:item-lister/set-select-mode! :my-lister]
(a/reg-event-db :item-lister/set-select-mode! set-select-mode!)

; @usage
;  [:item-lister/quit-select-mode! :my-lister]
(a/reg-event-db :item-lister/quit-select-mode! quit-select-mode!)

; @usage
;  [:item-lister/set-actions-mode! :my-lister]
(a/reg-event-db :item-lister/set-actions-mode! set-actions-mode!)

; @usage
;  [:item-lister/quit-actions-mode! :my-lister]
(a/reg-event-db :item-lister/quit-actions-mode! quit-actions-mode!)

; @usage
;  [:item-lister/set-reorder-mode! :my-lister]
(a/reg-event-db :item-lister/set-reorder-mode! set-reorder-mode!)

; @usage
;  [:item-lister/quit-reorder-mode! :my-lister]
(a/reg-event-db :item-lister/quit-reorder-mode! quit-reorder-mode!)

; @usage
;  [:item-lister/set-error-mode! :my-lister]
(a/reg-event-db :item-lister/set-error-mode! set-error-mode!)

; @usage
;  [:item-lister/set-memory-mode! :my-lister]
(a/reg-event-db :item-lister/set-memory-mode! set-memory-mode!)

; @usage
;  [:item-lister/reset-selections! :my-lister]
(a/reg-event-db :item-lister/reset-selections! reset-selections!)
