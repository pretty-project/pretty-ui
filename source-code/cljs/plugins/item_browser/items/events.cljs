
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.items.events
    (:require [plugins.item-browser.items.subs  :as items.subs]
              [plugins.item-lister.items.events :as items.events]
              [x.app-core.api                   :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.items.events
(def disable-items!         items.events/disable-items!)
(def enable-items!          items.events/enable-items!)
(def enable-all-items!      items.events/enable-all-items!)
(def toggle-item-selection! items.events/toggle-item-selection!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn disable-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (let [item-dex (r items.subs/get-item-dex db browser-id item-id)]
       (r disable-items! db browser-id [item-dex])))

(defn enable-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (let [item-dex (r items.subs/get-item-dex db browser-id item-id)]
       (r enable-items! db browser-id [item-dex])))
