
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.items.events
    (:require [plugins.item-browser.items.subs     :as items.subs]
              [plugins.plugin-handler.items.events :as items.events]
              [x.app-core.api                      :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn disable-items!
  ; @param (keyword) browser-id
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ;  (r item-browser/disable-items! db :my-browser [0 1 4])
  ;
  ; @return (map)
  [db [_ browser-id item-dexes]]
  (r items.events/disable-items! browser-id item-dexes))

(defn enable-items!
  ; @param (keyword) browser-id
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ;  (r item-browser/enable-items! db :my-browser [0 1 4])
  ;
  ; @return (map)
  [db [_ browser-id item-dexes]]
  (r items.events/enable-items! browser-id item-dexes))

(defn enable-all-items!
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (r item-browser/enable-all-items! db :my-browser)
  ;
  ; @return (map)
  [db [_ browser-id]]
  (r items.events/enable-all-items! browser-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn disable-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-browser/disable-item! db :my-browser "my-item")
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (let [item-dex (r items.subs/get-item-dex db browser-id item-id)]
       (r disable-items! db browser-id [item-dex])))

(defn enable-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-browser/enable-item! db :my-browser "my-item")
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (let [item-dex (r items.subs/get-item-dex db browser-id item-id)]
       (r enable-items! db browser-id [item-dex])))
