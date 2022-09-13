
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



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

(defn set-reload-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items lister-id :reload-mode?] true))

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
              (dissoc-in [:plugins :plugin-handler/meta-items lister-id :data-received?]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-default-order-by!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (let [default-order-by (r body.subs/get-body-prop db lister-id :default-order-by)]
       (assoc-in db [:plugins :plugin-handler/meta-items lister-id :order-by] default-order-by)))

; WARNING! DEPRECATED! DO NOT USE!
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
  ; WARNING! DEPRECATED! DO NOT USE!



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

(defn search-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) search-props
  ;  {:search-keys (keywords in vector)}
  ; @param (string) search-term
  ;
  ; @return (map)
  [db [_ lister-id {:keys [search-keys]} search-term]]
  (as-> db % (r reset-downloads! % lister-id)
             (r set-meta-item!   % lister-id :search-keys search-keys)
             (r set-meta-item!   % lister-id :search-term search-term)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn order-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (namespaced keyword) order-by
  ;
  ; @return (map)
  [db [_ lister-id order-by]]
  (as-> db % (r reset-downloads! % lister-id)
             (r set-meta-item!   % lister-id :order-by order-by)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/set-error-mode! :my-lister]
(a/reg-event-db :item-lister/set-error-mode! set-error-mode!)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/set-memory-mode! :my-lister]
(a/reg-event-db :item-lister/set-memory-mode! set-memory-mode!)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/reset-selections! :my-lister]
(a/reg-event-db :item-lister/reset-selections! reset-selections!)
