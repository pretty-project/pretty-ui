
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.events
    (:require [mid-fruits.candy                   :refer [return]]
              [mid-fruits.map                     :refer [dissoc-in]]
              [plugins.item-browser.core.subs     :as core.subs]
              [plugins.item-browser.mount.subs    :as mount.subs]
              [plugins.item-browser.transfer.subs :as transfer.subs]
              [plugins.item-lister.core.events    :as plugins.item-lister.core.events]
              [x.app-core.api                     :as a :refer [r]]
              [x.app-ui.api                       :as ui]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.core.events
(def set-error-mode! plugins.item-lister.core.events/set-error-mode!)
(def use-filter!     plugins.item-lister.core.events/use-filter!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (dissoc-in db [extension-id :item-browser/meta-items]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-root-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; A {:root-item-id "..."} tulajdonság értékét eltárolja az aktuálisan böngészett elem azonosítójaként ...
  ; ... ha a body komponens paraméterként megkapja a {:root-item-id "..."} tulajdonságot
  ; ... az [:item-browser/load-browser! ...] esemény nem tárolta el az útvonalból származtatott
  ;     :item-id értékét, mert nem az ".../:item-id" útvonal az aktuális.
  (if-let [item-id (get-in db [:plugins :item-lister/meta-items extension-id :item-id])]
          (return   db)
          (assoc-in db [:plugins :item-lister/meta-items extension-id :item-id]
                       (r mount.subs/get-body-prop db extension-id item-namespace :root-item-id))))

(defn store-derived-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [derived-item-id (r core.subs/get-derived-item-id db extension-id item-namespace)]
       (assoc-in db [:plugins :item-lister/meta-items extension-id :item-id] derived-item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace browser-props]]
  (let [route-title (r transfer.subs/get-transfer-item db extension-id item-namespace :route-title)]
       (cond-> db :store-derived-item-id (as-> % (r store-derived-item-id! % extension-id item-namespace))
                  route-title            (as-> % (r ui/set-header-title!   % route-title)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-browser/set-error-mode! set-error-mode!)
