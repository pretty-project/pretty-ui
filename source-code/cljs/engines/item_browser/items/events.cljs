
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.items.events
    (:require [engines.engine-handler.items.events :as items.events]
              [engines.item-browser.items.subs     :as items.subs]
              [re-frame.api                        :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn disable-items!
  ; @param (keyword) browser-id
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ; (r disable-items! db :my-browser [0 1 4])
  ;
  ; @return (map)
  [db [_ browser-id item-dexes]]
  (r items.events/disable-items! db browser-id item-dexes))

(defn enable-items!
  ; @param (keyword) browser-id
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ; (r enable-items! db :my-browser [0 1 4])
  ;
  ; @return (map)
  [db [_ browser-id item-dexes]]
  (r items.events/enable-items! db browser-id item-dexes))

(defn enable-all-items!
  ; @param (keyword) browser-id
  ;
  ; @usage
  ; (r enable-all-items! db :my-browser)
  ;
  ; @return (map)
  [db [_ browser-id]]
  (r items.events/enable-all-items! db browser-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn disable-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ; (r disable-item! db :my-browser "my-item")
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
  ; (r enable-item! db :my-browser "my-item")
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (let [item-dex (r items.subs/get-item-dex db browser-id item-id)]
       (r enable-items! db browser-id [item-dex])))
