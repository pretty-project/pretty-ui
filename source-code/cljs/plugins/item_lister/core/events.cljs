
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
              [plugins.engine-handler.core.events :as core.events]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.engine-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-mode!          core.events/set-mode!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-reload-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (r set-mode! db lister-id :reload-mode?))

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (r set-mode! db lister-id :error-mode?))

(defn set-memory-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  ; Ha az item-lister engine elhagyása előtt a set-memory-mode! függvény alkalmazásával
  ; a {:memory-mode? true} állapot beállításra kerül, akkor az item-lister engine legközelebbi
  ; megnyitásakor a listaelemek a legutóbbi keresési és rendezési beállítások szerint töltődnek majd le.
  (r set-mode! db lister-id :memory-mode?))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  ; Ha az item-lister engine elhagyása előtt, a {:memory-mode? true} állapot ...
  ; ... beállításra került, akkor megjegyzi a legutóbb használt keresési és rendezési beállításokat,
  ;     így az engine újbóli megnyitásakor, a listaelemek a legutolsó állapot szerint töltődnek majd le,
  ;     így a felhasználó az egyes elemek megtekintése/szerkesztése/... után visszatérhet
  ;     a lista legutóbbi állapotához.
  ; ... NEM került beállításra, akkor az engine újbóli megnyitásakor a listaelemek az alapértelmezett
  ;     beállítások szerint töltődnek majd le.
  (if-let [memory-mode? (r core.subs/get-meta-item db lister-id :memory-mode?)]
          (let [meta-items (get-in db [:engines :engine-handler/meta-items lister-id])]
               (as-> db % (dissoc-in % [:engines :engine-handler/meta-items lister-id])
                          (assoc-in  % [:engines :engine-handler/meta-items lister-id]
                                       (select-keys meta-items [:order-by :search-term]))))
          (dissoc-in db [:engines :engine-handler/meta-items lister-id])))

(defn reset-selections!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (dissoc-in db [:engines :engine-handler/meta-items lister-id :selected-items]))

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (let [items-path (r body.subs/get-body-prop db lister-id :items-path)]
       (-> db (dissoc-in items-path)
              (dissoc-in [:engines :engine-handler/meta-items lister-id :all-item-count])
              (dissoc-in [:engines :engine-handler/meta-items lister-id :received-item-count])
              (dissoc-in [:engines :engine-handler/meta-items lister-id :data-received?]))))



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
       (assoc-in db [:engines :engine-handler/meta-items lister-id :order-by] default-order-by)))



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
             (assoc-in % [:engines :engine-handler/meta-items lister-id :active-filter] filter-pattern)))



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
(r/reg-event-db :item-lister/set-error-mode! set-error-mode!)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/set-memory-mode! :my-lister]
(r/reg-event-db :item-lister/set-memory-mode! set-memory-mode!)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/reset-selections! :my-lister]
(r/reg-event-db :item-lister/reset-selections! reset-selections!)
