
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.events
    (:require [mid-fruits.candy              :refer [return]]
              [mid-fruits.map                :refer [dissoc-in]]
              [plugins.item-lister.core.subs :as core.subs]
              [x.app-core.api                :as a :refer [r]]
              [x.app-db.api                  :as db]))



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
  (update-in db [extension-id :item-lister/meta-items :search-mode?] not))

(defn toggle-select-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (as-> db % (update-in % [extension-id :item-lister/meta-items :select-mode?] not)
             (dissoc-in % [extension-id :item-lister/meta-items :selected-items])))

(defn toggle-reorder-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (update-in db [extension-id :item-lister/meta-items :reorder-mode?] not))

(defn toggle-reload-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (update-in db [extension-id :item-lister/meta-items :reload-mode?] not))

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (assoc-in db [extension-id :item-lister/meta-items :error-mode?] true))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [inherited-props (r core.subs/get-inherited-props db extension-id item-namespace)]
       (-> db (dissoc-in [extension-id :item-lister/meta-items])
              (assoc-in  [extension-id :item-lister/meta-items] inherited-props))))

(defn reset-search!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (dissoc-in db [extension-id :item-lister/meta-items :search-term]))



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
          (let [order-by-options (r core.subs/get-meta-item db extension-id item-namespace :order-by-options)]
               (assoc-in db [extension-id :item-lister/meta-items :order-by] (first order-by-options)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace body-props]]
  (r db/apply-item! db [extension-id :item-lister/meta-items] merge body-props))

(defn init-header!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace header-props]]
  (r db/apply-item! db [extension-id :item-lister/meta-items] merge header-props))

(defn init-body!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace body-props]]
  (as-> db % (r store-body-props!     % extension-id item-namespace body-props)
             (r set-default-order-by! % extension-id item-namespace)))

(defn destruct-body!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; Az item-lister plugin elhagyásakor visszaállítja a plugin állapotát, így a következő betöltéskor
  ; az init-body! függvény lefutása előtt nem villan fel a legutóbbi állapot!
  (as-> db % (r reset-lister!    % extension-id item-namespace)
             (r reset-downloads! % extension-id item-namespace)))



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
  (as-> db % (r reset-downloads!  % extension-id item-namespace)
             (r reset-selections! % extension-id item-namespace)
             (assoc-in % [extension-id :item-lister/meta-items :active-filter] filter-pattern)))



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
