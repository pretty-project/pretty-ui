
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.items.events
    (:require [plugins.item-browser.items.subs  :as items.subs]
              [plugins.item-lister.items.events :as plugins.item-lister.items.events]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.items.events
(def disable-items!         plugins.item-lister.items.events/disable-items!)
(def enable-items!          plugins.item-lister.items.events/enable-items!)
(def toggle-item-selection! plugins.item-lister.items.events/toggle-item-selection!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn disable-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (let [item-dex (r items.subs/get-item-dex db extension-id item-namespace item-id)]
       (r disable-items! db extension-id item-namespace [item-dex])))

(defn enable-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (let [item-dex (r items.subs/get-item-dex db extension-id item-namespace item-id)]
       (r enable-items! db extension-id item-namespace [item-dex])))
